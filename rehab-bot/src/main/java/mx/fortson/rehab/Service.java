package mx.fortson.rehab;

import java.util.ArrayList;
import java.util.TimerTask;

import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.listeners.ServiceListener;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;

public class Service extends TimerTask {

	private final Long ownerID;
	private final String serviceName;
	private final int farms;
	private final Long channelId;
	private final boolean delete;
	private final int serviceID;
	private final ServiceListener sl;
	private final Long expiry;
	private final int type;
	
	public Service(Long ownerID, String serviceName, int farms, Long channelId, boolean delete, int serviceID, ServiceListener sl, Long expiry, int type) {
		this.ownerID = ownerID;
		this.serviceName = serviceName;
		this.farms = farms;
		this.channelId = channelId;
		this.delete = delete;
		this.serviceID = serviceID;
		this.sl = sl;
		this.expiry = expiry;
		this.type = type;
		RehabBot.getApi().addEventListener(sl);
		RehabBot.getApi().getGuildChannelById(channelId).getManager().setSlowmode(15).queue();
	}	

	@Override
	public void run() {
		//Announce getting farm
		double multiplier = FarmUtils.getMultiplier(ownerID);
		FarmUtils.addSetFarmsToUser(ownerID, Math.toIntExact(Math.round(farms * multiplier)));
		RehabBot.getApi().getTextChannelById(channelId).sendMessage(MessageUtils.getServiceResult(ownerID, serviceName, farms)).allowedMentions(new ArrayList<>()).queue();
		Long current = System.currentTimeMillis();
		Double timeLeft =  (expiry.doubleValue() - current.doubleValue()) / 1000 / 60 / 60;
		ServicesUtils.updateServiceTimeLeft(serviceID,timeLeft);
	}

	public void endService() {
		//Announce end of service
		RehabBot.getApi().getTextChannelById(channelId).sendMessage(MessageUtils.announceServiceEnd(ownerID, serviceName)).queue();
		//update database
		if(!delete) {
			RehabBot.getApi().getGuildChannelById(channelId).getManager().setSlowmode(0).complete();
			ServicesUtils.updateBiddableServiceActive();
			ServicesUtils.createNewService(new BiddableServiceBean(ownerID,type));
		}
		if(delete) {
			ServicesUtils.deleteService(serviceID);
			RehabBot.getApi().getTextChannelById(channelId).delete().queue();
		}
		RehabBot.getApi().removeEventListener(sl);
		cancel();
	}
	
	public void stopService() {
		RehabBot.getApi().getTextChannelById(channelId).delete().queue();
		RehabBot.getApi().removeEventListener(sl);
		cancel();
	}
}
