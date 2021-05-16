package mx.fortson.rehab.channels;

import java.util.ArrayList;
import java.util.List;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.listeners.TradeInStateMachine;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DupeTradeIn implements IChannel{
	
	private static List<Long> activeTradeins = new ArrayList<>();

	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(activeTradeins.contains(event.getAuthor().getIdLong())) return;
		
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.DUPETRADEIN);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case TRADEINDUPE:
					String[] contentSplit = messageContent.split(" ");
					if(contentSplit.length>=2 && contentSplit[1].matches("^([1-9][0-9]*,)*[1-9][0-9]*")) {
						String[] idsS = contentSplit[1].split(",");
						int[] idsI = new int[idsS.length];
						for(int i = 0; i<idsS.length; i++) {
							idsI[i] = Integer.parseInt(idsS[i]);
						}
						activeTradeins.add(author.getIdLong());
						TradeInStateMachine tism = new TradeInStateMachine(author.getIdLong(), event.getMessageIdLong(),idsI);
						RehabBot.getApi().addEventListener(tism);
						tism.announceTradeIn();
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

	public static void removeActiveTradeIn(Long userId) {
		activeTradeins.remove(userId);
	}

}
