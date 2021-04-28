package mx.fortson.rehab.bean;

public class Degen implements Comparable<Degen>{

	private Long bank;
	private String name;
	private int farmAttempts;
	private int timesFarmed;
	private int wins;
	private int losses;
	private Long peak;
	
	public Long getPeak() {
		return peak;
	}
	public void setPeak(Long peak) {
		this.peak = peak;
	}
	public int getTimesFarmed() {
		return timesFarmed;
	}
	public void setTimesFarmed(int timesFarmed) {
		this.timesFarmed = timesFarmed;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public Long getBank() {
		return bank;
	}
	public void setBank(Long bank) {
		this.bank = bank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFarmAttempts() {
		return farmAttempts;
	}
	public void setFarmAttempts(int farmAttempts) {
		this.farmAttempts = farmAttempts;
	}
	@Override
	public int compareTo(Degen arg0) {
		return arg0.getBank().compareTo(bank);
	}
	
}
