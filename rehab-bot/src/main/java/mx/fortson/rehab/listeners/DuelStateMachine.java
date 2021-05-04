package mx.fortson.rehab.listeners;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.RemoveListenerTask;
import mx.fortson.rehab.utils.DuelUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DuelStateMachine extends ListenerAdapter {

	private final Long challengedId, challengerId, amount;
	
	private TimerTask task;
	private final Timer timer = new Timer("DuelTimer");
	
	public DuelStateMachine(Long challengedId, Long challengerId, Long amount) {
		this.challengedId = challengedId;
		this.challengerId = challengerId;
		this.amount = amount;
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if(null==task) {
			task = new RemoveListenerTask(this);
		    long delay = 1000L * 20;
		    timer.schedule(task, delay);
		}
		 if (event.getAuthor().isBot()) return; // don't respond to other bots
	        if (!event.getChannel().getName().equalsIgnoreCase(RehabBot.getOrCreateChannel(ChannelsEnum.DUELARENA).getName())) return; // ignore other channels
	        MessageChannel channel = event.getChannel();
	        String content = event.getMessage().getContentRaw();
	        if (event.getAuthor().getIdLong() == challengedId) {
	        	if(content.startsWith("!accept")) {
		        	if(event.getMessage().getMentionedMembers().isEmpty()) return;
		        	if(event.getMessage().getMentionedMembers().get(0).getIdLong() == challengerId) {
		        		channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.duel(challengerId,challengedId,amount))).allowedMentions(new ArrayList<>()).queue();
		        		event.getJDA().removeEventListener(this);
		        		timer.cancel();
		        		timer.purge();
		        	}
	        	}
	        }
	}
}
