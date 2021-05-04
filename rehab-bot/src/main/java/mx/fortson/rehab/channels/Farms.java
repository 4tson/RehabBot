package mx.fortson.rehab.channels;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.bean.FarmCollectionBean;
import mx.fortson.rehab.bean.FarmResultBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Farms implements IChannel{

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentRaw();
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
					FarmCollectionBean farmCollBean  = FarmUtils.farmForUser(author.getIdLong(),farms);
					PagedMessageBean paging = MessageUtils.getFarmResult(farmCollBean,author.getIdLong());
					while(paging.isMoreRecords()) {
						farmCollBean.setFarms((List<FarmResultBean>)(Object)paging.getLeftOverRecords());
						channel.sendMessage(paging.getMessage()).allowedMentions(paging.getPingList()).queue();
						paging = MessageUtils.getFarmResult(farmCollBean,author.getIdLong());
					}
					channel.sendMessage(paging.getMessage()).allowedMentions(paging.getPingList()).queue();
				break;
				default:
					BotCommands.commonCommands(ChannelsEnum.FARMS,commandEnum, event);
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
