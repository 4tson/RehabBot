package mx.fortson.rehab.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.Service;
import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.tasks.KillServiceTask;
import mx.fortson.rehab.utils.FormattingUtils;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServiceStateMachine extends ListenerAdapter{

	private final BiddableServiceBean biddableService;
	
	private TimerTask bidTask = null;
	private final Timer timer = new Timer("BidTimer");
	
	public ServiceStateMachine(BiddableServiceBean biddableService) {
		this.biddableService = biddableService;
	}

	protected void removeListener() {
		RehabBot.getApi().removeEventListener(this);
	}
	
	private void initTask() {
		bidTask = new TimerTask() {
			@Override
			public void run() {
				Long timeToRun = (long) (1000 * 60 * 60 * biddableService.getLengthHours());
				Long expireTime = timeToRun + System.currentTimeMillis();
				TextChannel servicesChannel = RehabBot.getApi().getTextChannelsByName("services",true).get(0);
				
				ServiceListener sl = new ServiceListener(servicesChannel.getIdLong(), expireTime);
				
				Service serviceTask = new Service(biddableService.getWinnerID(),
						biddableService.getServiceName(),
						biddableService.getFarms(),
						servicesChannel.getIdLong(),
						false,
						0,
						sl);
				Timer serviceTimer = new Timer("ServiceTimer");
				serviceTimer.schedule(serviceTask, 0L, (1000 * 60 * biddableService.getIntervalMinutes()));
				
				Timer kstTimer = new Timer("KillService-BiddableTimer");
				KillServiceTask kst = new KillServiceTask(serviceTask);
				kstTimer.schedule(kst, new Date(expireTime));
				
				//We announce that the bid is over
				servicesChannel.sendMessage(MessageUtils.announceBidEnd(biddableService)).queue();
				bidTask.cancel();
				removeListener();
			}
		};
	}

	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		 if (event.getAuthor().isBot()) return; // don't respond to other bots
	        if (!event.getChannel().getName().equalsIgnoreCase("bidding-services")) return; // ignore other channels
	        MessageChannel channel = event.getChannel();
	        String content = event.getMessage().getContentDisplay();
	        if(content.equalsIgnoreCase("!status")) {
	        	channel.sendMessage(MessageUtils.getServiceBidStatus(biddableService)).allowedMentions(new ArrayList<>()).queue();
	        	return;
	        }
	        if(content.equalsIgnoreCase("!commands")){
	        	channel.sendMessage(MessageUtils.getAvailableRehabCommands(ChannelsEnum.BIDSERVICE)).queue();;
	        	return;
	        }
	        String[] contentSplit = content.split(" ");
	        if(contentSplit.length == 2) {
		        if(contentSplit[0].equalsIgnoreCase("!bid")) {
		        	if(event.getAuthor().getIdLong()==biddableService.getWinnerID()) {
		        		channel.sendMessage("You already have the winning bid so far.").queue();
		        	}else {
			        	String amountS = contentSplit[1];
			        	if(FormattingUtils.isValidAmount(amountS)) {
			        		Long amountL = FormattingUtils.parseAmount(amountS);
			        		if(amountL>biddableService.getBid()) {
				        		boolean bid = ServicesUtils.bid(event.getAuthor().getIdLong(),amountL);
				        		if(bid) {
				        			ServicesUtils.returnBid(biddableService);
				        			biddableService.setBid(amountL);
				        			biddableService.setWinnerID(event.getAuthor().getIdLong());
				        			updateTimer();
				        		}
				        		channel.sendMessage(MessageUtils.getBidResult(bid,biddableService)).allowedMentions(new ArrayList<>()).queue();
			        		}else {
			        			channel.sendMessage("Your bid has to be greater than the current bid of " + FormattingUtils.format(biddableService.getBid())).allowedMentions(new ArrayList<>()).queue();
			        		}
			        	}else {
			        		event.getMessage().delete().queue();
			        		channel.sendMessage(MessageUtils.announceWrongCommand(content)).allowedMentions(new ArrayList<>()).queue();
			        	}
		        	}
		        }
	        }else {
	        	event.getMessage().delete().queue();
	        }
	}

	private void updateTimer() {
		if(null!=bidTask) {
			bidTask.cancel();
			timer.purge();
			bidTask = null;
		}
		initTask();
		timer.schedule(bidTask, 1000L * 60);
	}
}
