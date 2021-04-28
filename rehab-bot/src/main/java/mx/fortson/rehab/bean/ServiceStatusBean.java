package mx.fortson.rehab.bean;

import java.sql.Timestamp;

public class ServiceStatusBean {

	private boolean running;
	private Timestamp expiryDate;
	public boolean isRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public Timestamp getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}
}
