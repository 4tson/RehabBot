package mx.fortson.rehab.bean;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.utils.RandomUtils;

public class BiddableServiceBean {
	
	

	private final String serviceName;
	private final int farms;
	private final double lengthHours;
	private final int intervalMinutes;
	private Long winnerID;
	private Long bid;
	
	public BiddableServiceBean() {
		this.serviceName = RandomUtils.randomStringFromArray(RehabBotConstants.SERVICE_NAMES);
		this.farms = RandomUtils.randomInt(5);
		this.lengthHours = Double.parseDouble(String.format("%.1f", RandomUtils.randomDouble(1.0, 2.5)));
		this.intervalMinutes = RandomUtils.randomInt(7);
		this.winnerID = RehabBot.getBotId();
		this.bid = RandomUtils.randomAmountFromRange(new Long[]{50000000L,200000000L});
		
	}
	public BiddableServiceBean(ServiceBean service) {
		this.serviceName = service.getName();
		this.farms = service.getFarms();
		this.lengthHours = service.getLength();
		this.intervalMinutes = service.getInterval();
		this.winnerID = service.getOwnerDiscordId();
		this.bid = service.getPrice();
	}
	public Long getWinnerID() {
		return winnerID;
	}

	public void setWinnerID(Long winnerID) {
		this.winnerID = winnerID;
	}

	public Long getBid() {
		return bid;
	}

	public void setBid(Long bid) {
		this.bid = bid;
	}

	public String getServiceName() {
		return serviceName;
	}
	public int getFarms() {
		return farms;
	}
	public double getLengthHours() {
		return lengthHours;
	}
	public int getIntervalMinutes() {
		return intervalMinutes;
	}
	
	
}
