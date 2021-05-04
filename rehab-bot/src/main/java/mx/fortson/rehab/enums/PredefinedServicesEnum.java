package mx.fortson.rehab.enums;

public enum PredefinedServicesEnum {

	LOW("Low tier service",2,1.00,3,"1B",1),
	MID("Mid tier service",3,5.00,4,"2B",1),
	HIGH("High tier service",2,7.00,3,"6B",1),
	ULTRA("Ultra tier service",10,1.00,7,"10B",3),
	MEGA("Mega tier service",10,1.00,6,"15B",6),
	DIAMOND("Diamond tier service",10,1.20,5,"20B",10),
	DRAGON("Dragon tier service",10,4.00,4,"100B",25),
	;
	private String name;
	private int farms;
	private double durationHours;
	private int rate;
	private String price;
	private int level;
	
	
	public int getLevel() {
		return level;
	}
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
	private PredefinedServicesEnum(String name, int farms, double durationHours, int rate, String price, int level) {
		this.name = name;
		this.farms = farms;
		this.durationHours = durationHours;
		this.rate = rate;
		this.price = price;
		this.level = level;
	}
	
	
}
