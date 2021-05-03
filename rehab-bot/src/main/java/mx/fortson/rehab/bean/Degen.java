package mx.fortson.rehab.bean;

public class Degen{

	private Long bank;
	private String name;
	private int farmAttempts;
	private int timesFarmed;
	private int wins;
	private int losses;
	private Long peak;
	private boolean ironman;
	private Integer level;
	
	public boolean isIronman() {
		return ironman;
	}
	public void setIronman(boolean ironman) {
		this.ironman = ironman;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
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
	
}
