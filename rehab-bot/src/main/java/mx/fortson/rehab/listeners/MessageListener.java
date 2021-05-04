package mx.fortson.rehab.listeners;

import mx.fortson.rehab.channels.BotCommands;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RolesEnum;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {	
		if(!event.getAuthor().isBot()) {
			ChannelsEnum channel = ChannelsEnum.fromName(event.getChannel().getName());
			if(null!=channel && null!=channel.getChannel()) {
				channel.getChannel().processMessage(event);
			}else {
				String messageContent = event.getMessage().getContentDisplay();
				for(RolesEnum role : RolesEnum.values()) {
					if(messageContent.equalsIgnoreCase("!" + role.getName())) {
						BotCommands botCommands = new BotCommands();
						botCommands.processMessage(event);
					}
				}
			}
		}
	}
}
