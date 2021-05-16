package mx.fortson.rehab.bean;

public class SlotsRollBean {

	private String name;
	private String unicode;
	private int payout;
	private double weight;
	public SlotsRollBean(String name, String unicode, int payout, double weight) {
		super();
		this.name = name;
		this.unicode = unicode;
		this.payout = payout;
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnicode() {
		return unicode;
	}
	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}
	public int getPayout() {
		return payout;
	}
	public void setPayout(int payout) {
		this.payout = payout;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}	
}
