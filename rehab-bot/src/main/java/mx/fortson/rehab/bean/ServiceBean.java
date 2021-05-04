package mx.fortson.rehab.bean;

public class ServiceBean extends ItemBean{

	private int serviceId;
	private int farms;
	private double length;
	private String name;
	private int interval;
	private boolean active;
	
	
	public String info() {
		StringBuilder sb = new StringBuilder();
		sb.append("`")
		.append(name)
		.append("` will be generating `")
		.append(farms)
		.append("` farm(s) every `")
		.append(interval)
		.append("` minute(s) for `")
		.append(length)
		.append("` hour(s) for <@")
		.append(this.getOwnerDiscordId())
		.append(">.");
		return sb.toString();
	}

	public ServiceBean() {
		super();
	}


	public ServiceBean(int serviceId, int farms, double length, String name, int interval, boolean active) {
		this.serviceId = serviceId;
		this.farms = farms;
		this.length = Double.parseDouble(String.format("%.1f",length));
		this.name = name;
		this.interval = interval;
		this.active = active;
	}


	@Override
	public boolean isForSale() {
		return super.isForSale() && !this.active;
	}
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getFarms() {
		return farms;
	}
	public void setFarms(int farms) {
		this.farms = farms;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = Double.parseDouble(String.format("%.1f",length));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
}
