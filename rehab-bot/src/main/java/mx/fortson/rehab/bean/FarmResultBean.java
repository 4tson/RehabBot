package mx.fortson.rehab.bean;

import mx.fortson.rehab.enums.FarmTypeEnum;

public class FarmResultBean {

	private boolean exists;
	private boolean attempts;
	private Long farmedAmount;
	private Long newFunds;
	private String flavourText;
	private int newAttempts;
	private FarmTypeEnum type;
	private String itemName;
	private int farms;
	private double rateHours;
	private String name;
	private int interval;
	private int level;
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getFarms() {
		return farms;
	}
	public void setFarms(int farms) {
		this.farms = farms;
	}
	public double getRateHours() {
		return rateHours;
	}
	public void setRateHours(double rateHours) {
		this.rateHours = rateHours;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public FarmTypeEnum getType() {
		return type;
	}
	public void setType(FarmTypeEnum type) {
		this.type = type;
	}
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public int getNewAttempts() {
		return newAttempts;
	}
	public void setNewAttempts(int newAttempts) {
		this.newAttempts = newAttempts;
	}
	public String getFlavourText() {
		return flavourText;
	}
	public void setFlavourText(String flavourText) {
		this.flavourText = flavourText;
	}
	public boolean hasAttempts() {
		return attempts;
	}
	public void setAttempts(boolean attempts) {
		this.attempts = attempts;
	}
	public Long getFarmedAmount() {
		return farmedAmount;
	}
	public void setFarmedAmount(Long farmedAmount) {
		this.farmedAmount = farmedAmount;
	}
	public Long getNewFunds() {
		return newFunds;
	}
	public void setNewFunds(Long newFunds) {
		this.newFunds = newFunds;
	}
}
