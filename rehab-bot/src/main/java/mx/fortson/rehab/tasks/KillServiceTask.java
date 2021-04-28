package mx.fortson.rehab.tasks;

import java.util.TimerTask;

import mx.fortson.rehab.Service;

public class KillServiceTask extends TimerTask{

	private final Service service;
	
	public KillServiceTask(Service service) {
		this.service = service;
	}
	
	@Override
	public void run() {
		service.endService();
		cancel();
	}

}
