package mx.fortson.rehab.channels;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ServicesShop implements IChannel {
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentRaw();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.SERVICESSHOP);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case BUYSERVICESHOP:
					String[] splitContent = messageContent.split(" ");
					if(splitContent.length==2 && StringUtils.isNumeric(splitContent[1])) {
						event.getChannel().sendMessage(MessageUtils.announcePredSale(ServicesUtils.buyPredeterminedService(Integer.parseInt(splitContent[1]),author.getIdLong()),author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					}else {
						channel.sendMessage(MessageUtils.announceWrongCommand(event.getMessage().getContentDisplay())).allowedMentions(new ArrayList<>()).queue();
					}
				break;
				default:
					BotCommands.commonCommands(ChannelsEnum.SERVICESSHOP,commandEnum, event);
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

}
