package mx.fortson.rehab.bean;

public class PlayerWinsBean {

	private int currentFarms;
	private double rate;

	public PlayerWinsBean() {
		this.currentFarms = 0;
		this.rate = 2;
	}
	
	public PlayerWinsBean(int currentFarms, double rate) {
		this.currentFarms = currentFarms;
		this.rate = rate;
	}

	public int getCurrentFarms() {
		return currentFarms;
	}

	public void setCurrentFarms(int currentFarms) {
		this.currentFarms = currentFarms;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void updateFarms() {
		if(this.currentFarms==0) {
			this.currentFarms = 1;
		}else{
			this.currentFarms = (int) (Math.ceil(this.currentFarms * this.rate));
		}
	}

	public void lowerRate() {
		if(this.rate>.30) {
			this.rate = this.rate -.30;
		}
	}
	
	public void raiseRate() {
		if(this.rate < 2) {
			this.rate = this.rate + .10;
		}
	}

	public int cashOut() {
		return this.currentFarms + 10;
	}	
}
