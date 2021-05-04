package mx.fortson.rehab.listeners;

import java.util.ArrayList;
import java.util.Timer;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.LevelBean;
import mx.fortson.rehab.bean.LevelUpResultBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.RemoveListenerTask;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelUpStateMachine extends ListenerAdapter{

	private final Long userId;
	private final LevelBean level;
	private final RemoveListenerTask task;
	private final Timer timer = new Timer("LevelTimer");
	private final Long messageId;
	
	public LevelUpStateMachine(long userId, LevelBean level, Long messageId) {
		this.userId = userId;
		this.level = level;
		this.messageId = messageId;
		task = new RemoveListenerTask(this);
	    long delay = 1000L * 10;
	    timer.schedule(task, delay);
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return; // dont respond to other people
		if(event.getMessageIdLong()==messageId) return;// apparently it gets the same message even tho i registered the listener after the message got here idk
        if (!event.getChannel().getName().equalsIgnoreCase(RehabBot.getOrCreateChannel(ChannelsEnum.BOTCOMMANDS).getName())) return; // ignore other channels
       
        if(event.getAuthor().getIdLong() == userId) {
        	MessageChannel channel = event.getChannel();
	        String content = event.getMessage().getContentDisplay();
	        if(content.equalsIgnoreCase("Y")) {
	        	LevelUpResultBean result = RehabBot.levelUp(userId, level);
	        	if(result.isLevelUp()) {
	        		channel.sendMessage(MessageUtils.announceLevelUp(result, userId)).allowedMentions(new ArrayList<>()).complete();
	        	}else {
	        		channel.sendMessage("There was an issue leveling you up.").queue();
	        	}
	        }else {
	        	channel.sendMessage("<@" + userId +  "> Level up request cancelled.").allowedMentions(new ArrayList<>()).queue();
	        }
	        timer.cancel();
	    	timer.purge();
	    	event.getJDA().removeEventListener(this);
        }
	}
	
}
