package mx.fortson.rehab.bean;

public class DuelResultBean {

	private String winner;
	private String loser;
	private Long amount;
	private String flavourText;
	private boolean freeFarm;
	private int freeFarms;
	
	public int getFreeFarms() {
		return freeFarms;
	}
	public void setFreeFarms(int freeFarms) {
		this.freeFarms = freeFarms;
	}
	public boolean isFreeFarm() {
		return freeFarm;
	}
	public void setFreeFarm(boolean freeFarm) {
		this.freeFarm = freeFarm;
	}
	public String getFlavourText() {
		return flavourText;
	}
	public void setFlavourText(String flavourText) {
		this.flavourText = flavourText;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getLoser() {
		return loser;
	}
	public void setLoser(String loser) {
		this.loser = loser;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
}
