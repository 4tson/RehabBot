package mx.fortson.rehab.bean;

import java.util.Timer;

import mx.fortson.rehab.tasks.KillServiceTask;

public class ServiceTimerTaskPair {

	private final Timer kstTimer;
	private final KillServiceTask kst;
	
	public ServiceTimerTaskPair(Timer kstTimer, KillServiceTask kst) {
		this.kstTimer = kstTimer;
		this.kst = kst;
	}
	
	public Timer getKstTimer() {
		return kstTimer;
	}
	public KillServiceTask getKst() {
		return kst;
	}
}
