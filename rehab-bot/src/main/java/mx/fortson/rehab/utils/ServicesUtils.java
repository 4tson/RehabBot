package mx.fortson.rehab.utils;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.Service;
import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.bean.PredeterminedServiceSaleBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.bean.ServiceTimerTaskPair;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.listeners.ServiceListener;
import mx.fortson.rehab.listeners.ServiceStateMachine;
import mx.fortson.rehab.tasks.KillServiceTask;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class ServicesUtils {
	
	private static Map<Integer,ServiceTimerTaskPair> runningServices;
	
	
	static{
		runningServices = new HashMap<>();
	}
	
	public static void stopService(int serviceId) {
		ServiceTimerTaskPair timerTaskPair = runningServices.get(serviceId);
		if(null!=timerTaskPair) {
			timerTaskPair.getKst().cancel();
			timerTaskPair.getKstTimer().purge();
			KillServiceTask newKst = new KillServiceTask(timerTaskPair.getKst().getService());
			timerTaskPair.getKstTimer().schedule(newKst, 0L);
			runningServices.remove(serviceId,timerTaskPair);
		}
	}
	
	public static void createNewService() {
		BiddableServiceBean biddableService = new BiddableServiceBean();
		TextChannel servicesChannel = RehabBot.getOrCreateChannel(ChannelsEnum.BIDSERVICE);
		servicesChannel.getManager().setSlowmode(0).queue();
		servicesChannel.sendMessage(MessageUtils.announceNewService(biddableService,RehabBot.getOrCreateRole("services").getIdLong())).queue();
		RehabBot.getApi().addEventListener(new ServiceStateMachine(biddableService));
	}

	public static boolean bid(long discID, Long bidAmount) {
		try {
			boolean bidResult = DatabaseDegens.getFundsById(discID)>=bidAmount;
			if(bidResult) {
				DatabaseDegens.updateFundsSum(-bidAmount, DatabaseDegens.getDegenId(discID));
			}
			return bidResult;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void returnBid(BiddableServiceBean biddableService) {
		try {
			
			DatabaseDegens.updateFundsSum(biddableService.getBid(), DatabaseDegens.getDegenId(biddableService.getWinnerID()));
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static String activateService(long discId, long serviceId) {
		String result = "";
		try {
			
			ServiceBean service = DatabaseDegens.getServiceById(serviceId);
			if(null==service) {
				//send message service doesn't exist
				result = "That service does not exist. Did you misstype the id? Double check with `!inv`";
			}else {
				if(!service.getOwnerDiscordId().equals(discId)) {
					result = "You cannot activate a service you do not own!";
				} else {
					if(service.isForSale()) {
						//send message can't activate service that is for sale
						result = "You cannot activate a service that is currently for sale. Try `!cancelservice [id]` in the shop.";
					}else {
						if(service.isActive()) {
							//send message can't activate service that is already active
							result = "You cannot activate a service that is already running! Look for it under the services category. Otherwise ping a dev cause that shid is messed up.";
						}else {
							activateService(service);
							result = "Service activated!";
						}
					}
				}
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void deleteService(int serviceID) {
		try {
			
			DatabaseDegens.deleteService(serviceID);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public static PredeterminedServiceSaleBean buyPredeterminedService(int serviceId, long idLong) {
		PredeterminedServiceSaleBean result = new PredeterminedServiceSaleBean();
		try {
			
			
			ServiceBean service = DatabaseDegens.getPredeterminedService(serviceId);
			if(null!=service) {
				Long degenFunds = DatabaseDegens.getFundsById(idLong);
				if(degenFunds>service.getPrice()) {
					int userPredServices = DatabaseDegens.countDegenPredService(idLong);
					if(userPredServices == 0) {
						int degenId = DatabaseDegens.getDegenId(idLong);
						DatabaseDegens.updateFundsSum(-service.getPrice(), degenId);
						DatabaseDegens.createService(RandomUtils.randomStringFromArray(RehabBotConstants.SERVICE_NAMES), service.getFarms(), service.getLength(), service.getInterval(), degenId, true);
						ServiceBean createdService = DatabaseDegens.getDegenPredeterminedService(degenId);
						activateService(createdService);
						result.setSale(true);
						result.setService(createdService);
					}else {
						result.setFlavourText("You already have a service from the service shop active. Look under the `MY-SERVICES` category.");
					}
				}else {
					result.setFlavourText("You do not have the bank value to buy this service.");
				}
			}else{
				result.setFlavourText("The service you are trying to buy does not exist.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static void activateService(ServiceBean service) throws SQLException {
		//everything is good, let's enable it
		Long timeToRun = (long) (1000 * 60 * 60 * service.getLength());
		Long expireTime = timeToRun + System.currentTimeMillis();
		TextChannel createdChannel = RehabBot.getOrCreateChannel(service.getServiceId() + "-" + service.getName() + "-" + service.getOwnerName(),RehabBot.getOrCreateCategory("my-services"),0);
		StringBuilder greeting = new StringBuilder();
		greeting.append(service.info())
		.append(" You can check the status at any time using !status");
		createdChannel.sendMessage(greeting.toString()).allowedMentions(new ArrayList<>()).complete();
		createdChannel.putPermissionOverride(RehabBot.getOrCreateRole("@everyone")).deny(Permission.VIEW_CHANNEL).complete();
		createdChannel.putPermissionOverride(createdChannel.getGuild().getMemberById(service.getOwnerDiscordId())).setAllow(Permission.VIEW_CHANNEL).complete();
		ServiceListener sl = new ServiceListener(createdChannel.getIdLong(), expireTime, service.getServiceId());
		
		Service serviceTask = new Service(service.getOwnerDiscordId(),
				service.getName(),
				service.getFarms(),
				createdChannel.getIdLong(),
				true,
				service.getServiceId(),
				sl);
		
		Timer serviceTimer = new Timer("Service-" + service.getServiceId() + "Timer");
		serviceTimer.schedule(serviceTask, 0L, (1000 * 60 * service.getInterval()));
		Timer kstTimer = new Timer("KillService-" + service.getServiceId() + "Timer");
		KillServiceTask kst = new KillServiceTask(serviceTask);
		kstTimer.schedule(kst, new Date(expireTime));
		DatabaseDegens.updateRunningService(service.getServiceId());
		addCancellableService(service.getServiceId(), new ServiceTimerTaskPair(kstTimer,kst));
	}
	
	public static void addCancellableService(int id, ServiceTimerTaskPair pair) {
		runningServices.put(id, pair);
	}

	public static ServiceBean getServiceById(int serviceId) {
		ServiceBean service = null;
		try {
			service= DatabaseDegens.getServiceById(serviceId);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return service;
	}

}
