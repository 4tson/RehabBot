package mx.fortson.rehab.listeners;

import java.util.ArrayList;
import java.util.Timer;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.RemoveListenerTask;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class WipeStateMachine extends ListenerAdapter{

	private final Long userId;
	private final String confirmString;
	private final RemoveListenerTask task;
	private final Timer timer = new Timer("WipeTimer");
	private final Long messageId;
	
	{
		task = new RemoveListenerTask(this);
	    long delay = 1000L * 10;
	    timer.schedule(task, delay);
	}
	
	public WipeStateMachine(Long userId, String confirmString, Long messageId) {
		this.userId = userId;
		this.confirmString = confirmString;
		this.messageId = messageId;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return; // dont respond to other people
		if(event.getMessageIdLong()==messageId) return;// apparently it gets the same message even tho i registered the listener after the message got here idk
        if (!event.getChannel().getName().equalsIgnoreCase(RehabBot.getOrCreateChannel(ChannelsEnum.BOTCOMMANDS).getName())) return; // ignore other channels
       
        if(event.getAuthor().getIdLong() == userId) {
	        MessageChannel channel = event.getChannel();
	        String content = event.getMessage().getContentDisplay();
	        if(content.equals(confirmString)) {
	        	channel.sendMessage("<@" + userId + "> Your data is being wiped as we speak. Goodbye.").allowedMentions(new ArrayList<>()).complete();
	        	RehabBot.deleteDegen(userId);
	        }else {
	        	channel.sendMessage("<@" + userId +  "> Wipe cancelled.").allowedMentions(new ArrayList<>()).queue();
	        }
	        timer.cancel();
	    	timer.purge();
	    	event.getJDA().removeEventListener(this);
        }
	}
}
