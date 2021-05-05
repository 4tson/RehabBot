package mx.fortson.rehab.channels;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.listeners.TradeInStateMachine;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class DupeTradeIn implements IChannel{

	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.DUPETRADEIN);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case TRADEINDUPE:
					String[] contentSplit = messageContent.split(" ");
					if(contentSplit.length>=2 && StringUtils.isNumeric(contentSplit[1])) {
						Pair<Long,Integer> tradeInPair = getTradeInPair(author.getIdLong(),Integer.parseInt(contentSplit[1])); 
						if(tradeInPair!=null) {
							int farms = calculateFarms(tradeInPair);
							channel.sendMessage(MessageUtils.confirmTradeIn(author.getIdLong(), farms)).allowedMentions(new ArrayList<>()).queue();
							RehabBot.getApi().addEventListener(new TradeInStateMachine(author.getIdLong(),farms, event.getMessageIdLong(),Integer.parseInt(contentSplit[1])));
						}else {
							channel.sendMessage("<@"+author.getIdLong() + "> You cannot trade in that item. Only items you own, you have duplicates of, and are not for sale can be traded in.").allowedMentions(new ArrayList<>()).queue();
						}
					}
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.DUPETRADEIN,commandEnum, event);
					break;	
				}
			}else {
				event.getMessage().delete().queue();
				channel.sendMessage(MessageUtils.announceWrongCommand(event.getMessage().getContentDisplay())).allowedMentions(new ArrayList<>()).queue();
			}
		}else {
			event.getMessage().delete().queue();
		}
	}

	private int calculateFarms(Pair<Long, Integer> tradeInPair) {
		if(tradeInPair.getRight()==1) {
			return 10 + Math.toIntExact(tradeInPair.getLeft()/1000000L);	
		}
		if(tradeInPair.getRight()==2) {
			return 20 + Math.toIntExact(tradeInPair.getLeft()/10000000L);
		}
		if(tradeInPair.getRight()==3) {
			return 900 + Math.toIntExact(tradeInPair.getLeft()/50000000L);
		}
		return 0;
	}

	private Pair<Long,Integer> getTradeInPair(long idLong, int itemid) {
		Pair<Long,Integer> result = null;
		try {
			result =  DatabaseDegens.selectDuplicateItemsAndValue(idLong,itemid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
