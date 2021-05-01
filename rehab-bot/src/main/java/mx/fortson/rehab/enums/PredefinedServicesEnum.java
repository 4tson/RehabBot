package mx.fortson.rehab.enums;

public enum PredefinedServicesEnum {

	LOW("Low tier service",2,1.00,3,"1B"),
	MID("Mid tier service",3,5.00,5,"3B"),
	HIGH("High tier service",2,7.00,3,"6B"),
	ULTRA("Ultra tier service",10,1.00,5,"100B");
	private String name;
	private int farms;
	private double durationHours;
	private int rate;
	private String price;
	
	public String getPrice() {
		return price;
	}
	public String getName() {
		return name;
	}
	public int getFarms() {
		return farms;
	}
	public double getDurationHours() {
		return durationHours;
	}
	public int getRate() {
		return rate;
	}
	private PredefinedServicesEnum(String name, int farms, double durationHours, int rate, String price) {
		this.name = name;
		this.farms = farms;
		this.durationHours = durationHours;
		this.rate = rate;
		this.price = price;
	}
	
	
}
