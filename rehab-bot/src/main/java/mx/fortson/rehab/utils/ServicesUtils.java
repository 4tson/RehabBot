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
import mx.fortson.rehab.enums.CategoriesEnum;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RolesEnum;
import mx.fortson.rehab.listeners.ServiceListener;
import mx.fortson.rehab.listeners.ServiceStateMachine;
import mx.fortson.rehab.tasks.KillServiceTask;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class ServicesUtils {
	
	private static Map<Integer,ServiceTimerTaskPair> runningServices;
	public static Map<Integer,Integer> BIDDABLE_SERVICE_IDS = new HashMap<>();
	
	static{
		runningServices = new HashMap<>();
	}
	
	public static void endService(int serviceId) {
		ServiceTimerTaskPair timerTaskPair = runningServices.get(serviceId);
		if(null!=timerTaskPair) {
			timerTaskPair.getKst().cancel();
			timerTaskPair.getKstTimer().purge();
			KillServiceTask newKst = new KillServiceTask(timerTaskPair.getKst().getService());
			timerTaskPair.getKstTimer().schedule(newKst, 0L);
			runningServices.remove(serviceId,timerTaskPair);
		}
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
						if(service.getRequiredLevel()>DatabaseDegens.getDegenNextLevel(discId).getLevel()) {
							result = "You do not have the required level of `" + service.getRequiredLevel() + "` to activate this service.";
						}else {
							if(service.isActive()) {
								//send message can't activate service that is already active
								result = "You cannot activate a service that is already running! Look for it under the services category. Otherwise ping a dev cause that shid is messed up.";
							}else {
								if(DatabaseDegens.getActiveServicesCount()==50) {
									result = "There are too many active services right now. Please wait for another service to finish.";
								}else {
									if(DatabaseDegens.getUserActiveServicesCount(DatabaseDegens.getDegenId(discId))>=5) {
										result = "You have reached your limit of 5 active services. Either wait for one of them to finish or `!cancel` it.";
									}else {
										activateService(service);
										result = "Service activated!";
									}
								}
							}
						}
					}
				}
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
			result = "There was a problem with your request, try again later.";
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
			if(DatabaseDegens.getActiveServicesCount()==50) {
				result.setFlavourText("There are too many active services right now. Please wait for another service to finish.");
			}else if(DatabaseDegens.getUserActiveServicesCount(DatabaseDegens.getDegenId(idLong))>=5) {
				result.setFlavourText("You have reached your limit of 5 active services. Either wait for one of them to finish or `!cancel` it.");
			}else if(null!=service) {
				Long degenFunds = DatabaseDegens.getFundsById(idLong);
				if(DatabaseDegens.getDegenNextLevel(idLong).getLevel()>=service.getRequiredLevel()) {
					if(degenFunds>service.getPrice()) {
						int userPredServices = DatabaseDegens.countDegenPredService(idLong);
						if(userPredServices == 0) {
							int degenId = DatabaseDegens.getDegenId(idLong);
							DatabaseDegens.updateFundsSum(-service.getPrice(), degenId);
							DatabaseDegens.createService(RandomUtils.randomStringFromArray(RehabBotConstants.SERVICE_NAMES), service.getFarms(), service.getLength(), service.getInterval(), degenId, 2,service.getRequiredLevel());
							ServiceBean createdService = DatabaseDegens.getDegenPredeterminedService(degenId);
							activateService(createdService);
							result.setSale(true);
							result.setService(createdService);
						}else {
							result.setFlavourText("You already own a service from the service shop. Look under the `MY-SERVICES` category or in your `!inv`");
						}
					}else {
						result.setFlavourText("You do not have the bank value to buy this service.");	
					}
				}else {
					result.setFlavourText("You do not have the level required to buy this service.");
				}
			}else{
				result.setFlavourText("The service you are trying to buy does not exist.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void activateService(ServiceBean service) throws SQLException {
		//everything is good, let's enable it
		Long timeToRun = (long) (1000 * 60 * 60 * service.getLength());
		Long expireTime = timeToRun + System.currentTimeMillis();
		TextChannel createdChannel = RehabBot.getOrCreateChannel(service.getServiceId() + "-" + service.getName() + "-" + service.getOwnerName(),RehabBot.getOrCreateCategory(CategoriesEnum.MYSERVICES),0, new RolesEnum[] {}, new RolesEnum[] {});
		
		createdChannel.putPermissionOverride(RehabBot.getOrCreateRole(RolesEnum.EVERYONE)).deny(Permission.VIEW_CHANNEL).complete();
		createdChannel.putPermissionOverride(RehabBot.getOrCreateRole(RolesEnum.DEGEN)).deny(Permission.VIEW_CHANNEL).complete();
		createdChannel.putPermissionOverride(RehabBot.getOrCreateRole(RolesEnum.IRONMAN)).deny(Permission.VIEW_CHANNEL).complete();
		createdChannel.putPermissionOverride(createdChannel.getGuild().getMemberById(service.getOwnerDiscordId())).setAllow(Permission.VIEW_CHANNEL).complete();
		
		StringBuilder greeting = new StringBuilder();
		greeting.append(service.info())
		.append(" You can check the status at any time using !status");
		createdChannel.sendMessage(greeting.toString()).complete();
		ServiceListener sl = new ServiceListener(createdChannel.getIdLong(), expireTime, service.getServiceId());
		
		Service serviceTask = new Service(service.getOwnerDiscordId(),
				service.getName(),
				service.getFarms(),
				createdChannel.getIdLong(),
				true,
				service.getServiceId(),
				sl,
				expireTime,
				service.getType());
		
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

	public static void updateServiceTimeLeft(int serviceID, Double timeLeft) {
		try {
			DatabaseDegens.updateService(serviceID, timeLeft);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	
	public static void createNewService(BiddableServiceBean biddableService) {
		TextChannel servicesChannel = biddableService.getType()==1 ? RehabBot.getOrCreateChannel(ChannelsEnum.BIDSERVICE) : RehabBot.getOrCreateChannel(ChannelsEnum.BLINDBIDSERVICE);
		servicesChannel.getManager().setSlowmode(0).queue();
		ServicesUtils.updateBiddableService(biddableService);
		servicesChannel.sendMessage(MessageUtils.announceNewService(biddableService,RehabBot.getOrCreateRole(RolesEnum.SERVICES).getIdLong(),biddableService.getType()==1)).queue();
		RehabBot.getApi().addEventListener(new ServiceStateMachine(biddableService));
	}
	


	public static void updateBiddableService(BiddableServiceBean biddableService) {
		try {
			DatabaseDegens.updateBiddable(biddableService);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void updateBiddableServiceActive() {
		try {
			DatabaseDegens.updateBiddableActive();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void stopService(int serviceId) {
		ServiceTimerTaskPair timerTaskPair = runningServices.get(serviceId);
		if(null!=timerTaskPair) {
			timerTaskPair.getKst().cancel();
			timerTaskPair.getKstTimer().purge();
			timerTaskPair.getKst().getService().stopService();
			runningServices.remove(serviceId,timerTaskPair);
		}
	}


	public static boolean canActivateService(long id) throws SQLException {
		return DatabaseDegens.getActiveServicesCount()<50 && DatabaseDegens.getUserActiveServicesCount(DatabaseDegens.getDegenId(id))<5;
	}


	public static void restoreBiddableServices() {
		try {
			int[] biddableTypes = {1,4};
			for(int type : biddableTypes) {
				ServiceBean biddableService = DatabaseDegens.selectBiddableService(type);
				BIDDABLE_SERVICE_IDS.put(type, DatabaseDegens.getBiddableServiceId(type));
				if(biddableService!=null) {
					if(biddableService.getOwnerDiscordId().equals(RehabBot.getBotId())|| !biddableService.isActive()) {
						//The biddable service was not active or belonged to the bot, so we create it to be bid on
						createNewService(new BiddableServiceBean(biddableService,biddableService.getOwnerDiscordId(),type));
					}else if(biddableService.isActive()) {
						Long timeToRun = (long) (1000 * 60 * 60 * biddableService.getLength());
						Long expireTime = timeToRun + System.currentTimeMillis();
						
						TextChannel servicesChannel = biddableService.getType()==1 ? RehabBot.getOrCreateChannel(ChannelsEnum.BIDSERVICE) : RehabBot.getOrCreateChannel(ChannelsEnum.BLINDBIDSERVICE);
						ServiceListener sl = new ServiceListener(servicesChannel.getIdLong(), expireTime, 0);
						
						Service serviceTask = new Service(biddableService.getOwnerDiscordId(),
								biddableService.getName(),
								biddableService.getFarms(),
								servicesChannel.getIdLong(),
								false,
								BIDDABLE_SERVICE_IDS.get(type),
								sl,
								expireTime,
								biddableService.getType());
						
						Timer serviceTimer = new Timer("ServiceTimer");
						serviceTimer.schedule(serviceTask, 0L, (1000 * 60 * biddableService.getInterval()));
						
						Timer kstTimer = new Timer("KillService-BiddableTimer");
						KillServiceTask kst = new KillServiceTask(serviceTask);
						kstTimer.schedule(kst, new Date(expireTime));
						servicesChannel.sendMessage(biddableService.info()).allowedMentions(new ArrayList<>()).queue();
					}
				}else {
					//The biddable service did not exist so we just create a new one and insert it into the database
					BiddableServiceBean newBiddableService = new BiddableServiceBean(RehabBot.getBotId(),type);
					createNewService(newBiddableService);
					DatabaseDegens.insertBiddableService(newBiddableService);
					BIDDABLE_SERVICE_IDS.put(type, DatabaseDegens.getBiddableServiceId(type));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
