package mx.fortson.rehab.bean;

public class LevelUpResultBean {

	private boolean levelUp;
	private ServiceBean service;
	private boolean freeService;
	private int newLevel;
	
	public int getNewLevel() {
		return newLevel;
	}
	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}
	public boolean isLevelUp() {
		return levelUp;
	}
	public void setLevelUp(boolean levelUp) {
		this.levelUp = levelUp;
	}
	public ServiceBean getService() {
		return service;
	}
	public void setService(ServiceBean service) {
		this.service = service;
	}
	public boolean isFreeService() {
		return freeService;
	}
	public void setFreeService(boolean freeService) {
		this.freeService = freeService;
	}
}
