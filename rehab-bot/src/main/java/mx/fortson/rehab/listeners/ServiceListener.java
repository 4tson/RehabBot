package mx.fortson.rehab.listeners;

import java.time.Duration;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServiceListener extends ListenerAdapter{

	private final Long channelId, expiry;
	
	public ServiceListener(Long channelId, Long expiry) {
		this.channelId = channelId;
		this.expiry = expiry;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		 if (event.getAuthor().isBot()) return; // don't respond to other bots
	        if (!channelId.equals(event.getChannel().getIdLong())) return; // ignore other channels
	        if(event.getMessage().getContentRaw().equalsIgnoreCase("!status")) {
	        	Duration duration = Duration.ofMillis(expiry - System.currentTimeMillis());
	        	event.getChannel().sendMessage("Service has " + duration.toHoursPart() + " hour(s), " + duration.toMinutesPart() + " minute(s), and " + duration.toSecondsPart() + " second(s) left.").queue();
	        }
	        
	}
}
