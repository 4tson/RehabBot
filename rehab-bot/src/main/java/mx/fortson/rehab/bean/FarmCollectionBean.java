package mx.fortson.rehab.bean;

import java.util.List;

public class FarmCollectionBean {

	private List<FarmResultBean> farms;
	private boolean exists;
	private Long farmedAmount;
	private Long newFunds;
	private boolean attempts;
	private int newAttempts;
	
	public int getNewAttempts() {
		return newAttempts;
	}
	public void setNewAttempts(int newAttempts) {
		this.newAttempts = newAttempts;
	}
	public List<FarmResultBean> getFarms() {
		return farms;
	}
	public void setFarms(List<FarmResultBean> farms) {
		this.farms = farms;
	}
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
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
	public boolean isAttempts() {
		return attempts;
	}
	public void setAttempts(boolean attempts) {
		this.attempts = attempts;
	}	
}
