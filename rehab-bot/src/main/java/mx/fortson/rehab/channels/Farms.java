package mx.fortson.rehab.channels;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.bean.MessageUtilsResultBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Farms implements IChannel{

	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.FARMS);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case FARM:
					int farms = 1;
					String[] splitContent = messageContent.split(" ");
					if(splitContent.length>1) {
						String secondArg = splitContent[1];
						if(StringUtils.isNumeric(secondArg)) {
							farms = Integer.parseInt(secondArg);
						}
					}
					MessageUtilsResultBean messageUtilsResultBean = MessageUtils.getFarmResult(FarmUtils.farmForUser(author.getIdLong(),farms), author.getIdLong());
					channel.sendMessage(messageUtilsResultBean.getMessage()).allowedMentions(messageUtilsResultBean.getPingList()).queue();
				break;
				default:
					BotCommands.commonCommands(ChannelsEnum.FARMS,commandEnum, event);
					break;	
				}
			}else {
				event.getMessage().delete().queue();
				channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
			}
		}else {
			event.getMessage().delete().queue();
		}
	}

}
