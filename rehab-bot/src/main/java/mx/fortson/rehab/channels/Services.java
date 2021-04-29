package mx.fortson.rehab.channels;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Services implements IChannel {
	
	private static Role servicesRole;
	
	static {
		servicesRole = RehabBot.getOrCreateRole("services");
	}
	
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.SERVICES);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case ADDSERVICES:
					event.getGuild().addRoleToMember(event.getMember(), servicesRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),servicesRole.getName(),"added")).queue();
					break;
				case REMSERVICES:
					event.getGuild().removeRoleFromMember(event.getMember(), servicesRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),servicesRole.getName(),"removed")).queue();
					break;
				case ACTIVATESERVICE:
					String[] splitContent = messageContent.split(" ");
					if(splitContent.length==2) {
						String id = splitContent[1];
						if(StringUtils.isNumeric(id)) {
							channel.sendMessage(ServicesUtils.activateService(author.getIdLong(),Long.parseLong(id))).queue();
							break;
						}
					}	
					channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					break;
				case SERVICESTATUS:
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.SERVICES,commandEnum, event);
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
