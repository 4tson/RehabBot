package mx.fortson.rehab.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.RemoveListenerTask;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TradeInStateMachine extends ListenerAdapter{
	
	private final Long userId;
	private final int farms;
	private final Long messageId;
	private final int itemId;
	
	private final RemoveListenerTask task;
	private final Timer timer = new Timer("TradeInTimer");
	
	public TradeInStateMachine(long userId, int farms, Long messageId, int itemId) {
		this.userId = userId;
		this.farms = farms;
		this.itemId = itemId;
		this.messageId = messageId;
		
		task = new RemoveListenerTask(this);
	    long delay = 1000L * 10;
	    timer.schedule(task, delay);
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return; // dont respond to other people
		if(event.getMessageIdLong()==messageId) return;// apparently it gets the same message even tho i registered the listener after the message got here idk
        if (!event.getChannel().getName().equalsIgnoreCase(RehabBot.getOrCreateChannel(ChannelsEnum.DUPETRADEIN).getName())) return; // ignore other channels
        
        if(event.getAuthor().getIdLong() == userId) {
        	MessageChannel channel = event.getChannel();
	        String content = event.getMessage().getContentDisplay();
	        if(content.equalsIgnoreCase("Y")) {
	        	boolean result = tradeInItem();
	        	if(result) {
	        		channel.sendMessage(MessageUtils.announceTradeIn(userId, farms)).allowedMentions(new ArrayList<>()).complete();
	        	}else {
	        		channel.sendMessage("<@"+userId+"> There was an issue trading in your item.").queue();
	        	}
	        }else {
	        	channel.sendMessage("<@" + userId +  "> Trade in request cancelled.").allowedMentions(new ArrayList<>()).queue();
	        }
	        timer.cancel();
	    	timer.purge();
	    	event.getJDA().removeEventListener(this);
        }
	}

	private boolean tradeInItem() {
		try {
			if(DatabaseDegens.selectDuplicateItemsAndValue(userId,itemId)!=null) {
				DatabaseDegens.deleteItem(itemId);
				DatabaseDegens.addFarmAtt(farms, DatabaseDegens.getDegenId(userId));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
