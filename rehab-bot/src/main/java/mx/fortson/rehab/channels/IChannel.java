package mx.fortson.rehab.channels;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface IChannel {

	void processMessage(GuildMessageReceivedEvent event);
	
}
