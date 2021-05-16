package mx.fortson.rehab.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.channels.DupeTradeIn;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.RemoveListenerTask;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public class TradeInStateMachine extends ListenerAdapter{
	
	private int itemCounter;
	private final Long userId;
	private final Long messageId;
	private final int[] itemIds;
	
	private RemoveListenerTask task;
	private final Timer timer = new Timer("TradeInTimer");
	
	private Pair<Long, Integer> tradeInPair;

	public TradeInStateMachine(long idLong, long messageIdLong, int[] idsI) {
		this.userId = idLong;
		this.messageId = messageIdLong;
		this.itemIds = idsI;
		this.itemCounter = 0;
		task = new RemoveListenerTask(this);
		
		
	}

	public void announceTradeIn() {
		while(null==tradeInPair && itemCounter < itemIds.length) {
			try {
				tradeInPair = DatabaseDegens.selectDuplicateItemsAndValue(userId,itemIds[itemCounter]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if(tradeInPair!=null) {
				RehabBot.getOrCreateChannel(ChannelsEnum.DUPETRADEIN).sendMessage(MessageUtils.confirmTradeIn(userId, calculateFarms(),itemIds[itemCounter])).queue();
				updateTimer();
			}else {
				RehabBot.getOrCreateChannel(ChannelsEnum.DUPETRADEIN).sendMessage("<@"+userId + "> You cannot trade in item `" + itemIds[itemCounter] + "`. Only items you own, you have duplicates of, and are not for sale can be traded in.").allowedMentions(new ArrayList<>()).queue();
				itemCounter++;
			}
		}
		if(itemCounter==itemIds.length) {
			RehabBot.getOrCreateChannel(ChannelsEnum.DUPETRADEIN).sendMessage("<@"+userId + ">That was the last of your requested tradeins.").queue();
			task.cancel();
			timer.purge();
			RehabBot.getApi().removeEventListener(this);
			DupeTradeIn.removeActiveTradeIn(userId);
		}
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
	        		channel.sendMessage(MessageUtils.announceTradeIn(userId, calculateFarms(),itemIds[itemCounter])).allowedMentions(new ArrayList<>()).complete();
	        	}else {
	        		channel.sendMessage("<@"+userId+"> There was an issue trading in your item.").queue();
	        	}
	        }else {
	        	channel.sendMessage("<@" + userId +  "> Trade in request cancelled.").allowedMentions(new ArrayList<>()).queue();
	        }
	        tradeInPair = null;
	        updateTimer();
	        itemCounter++;
	        announceTradeIn();
        }
	}

	private void updateTimer() {
		if(null!=task) {
			task.cancel();
			timer.purge();
			task = null;
		}
		task = new RemoveListenerTask(this);
		timer.schedule(task, 1000L * 60);
	}
	
	private boolean tradeInItem() {
		try {
			tradeInPair = DatabaseDegens.selectDuplicateItemsAndValue(userId,itemIds[itemCounter]);
			if(tradeInPair!=null) {
				DatabaseDegens.deleteItem(itemIds[itemCounter]);
				DatabaseDegens.addFarmAtt(calculateFarms(), DatabaseDegens.getDegenId(userId));
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private int calculateFarms() {
		if(tradeInPair.getRight()==1) {
			return 10 + Math.toIntExact(tradeInPair.getLeft()/1000000L);	
		}
		if(tradeInPair.getRight()==2) {
			return 20 + Math.toIntExact(tradeInPair.getLeft()/10000000L);
		}
		if(tradeInPair.getRight()==3) {
			return 900 + Math.toIntExact(tradeInPair.getLeft()/50000000L);
		}
		return 0;
	}
}
