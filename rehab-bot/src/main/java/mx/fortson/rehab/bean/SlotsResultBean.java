package mx.fortson.rehab.bean;

public class SlotsResultBean {

	private long discId;
	private boolean won;
	private String flavourText;
	private int farmsWon;
	private boolean jackpot;
	
	public boolean isJackpot() {
		return jackpot;
	}
	public void setJackpot(boolean jackpot) {
		this.jackpot = jackpot;
	}
	public long getDiscId() {
		return discId;
	}
	public void setDiscId(long discId) {
		this.discId = discId;
	}
	public boolean isWon() {
		return won;
	}
	public void setWon(boolean won) {
		this.won = won;
	}
	public String getFlavourText() {
		return flavourText;
	}
	public void setFlavourText(String flavourText) {
		this.flavourText = flavourText;
	}
	public int getFarmsWon() {
		return farmsWon;
	}
	public void setFarmsWon(int farmsWon) {
		this.farmsWon = farmsWon;
	}
	
}
