package mx.fortson.rehab.listeners;

import java.time.Duration;

import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServiceListener extends ListenerAdapter{

	private final Long channelId, expiry;
	private final int serviceId;
	private boolean cancelling;
	
	public ServiceListener(Long channelId, Long expiry, int serviceId) {
		this.channelId = channelId;
		this.expiry = expiry;
		this.serviceId = serviceId;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		 if (event.getAuthor().isBot()) return; // don't respond to other bots
	        if (!channelId.equals(event.getChannel().getIdLong())) return; // ignore other channels
	        if(event.getMessage().getContentRaw().equalsIgnoreCase("!status")) {
	        	Duration duration = Duration.ofMillis(expiry - System.currentTimeMillis());
	        	event.getChannel().sendMessage("Service has " + duration.toHoursPart() + " hour(s), " + duration.toMinutesPart() + " minute(s), and " + duration.toSecondsPart() + " second(s) left.").queue();
	        }else if(event.getMessage().getContentRaw().equalsIgnoreCase("!cancel") && serviceId!=0) {
	        	event.getChannel().sendMessage(MessageUtils.confirmServiceCancel(ServicesUtils.getServiceById(serviceId))).queue();;
	        	cancelling = true;
	        }else if(event.getMessage().getContentRaw().equalsIgnoreCase("y") && cancelling) {
	        	ServicesUtils.stopService(serviceId);
	        }else if(cancelling){
	        	event.getChannel().sendMessage("Cancel request cancelled by cancelling the cancel request.").queue();
	        	cancelling = false;
	        }
	        
	}
}
