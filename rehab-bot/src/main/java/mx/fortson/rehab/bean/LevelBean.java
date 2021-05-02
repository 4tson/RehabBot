package mx.fortson.rehab.bean;

public class LevelBean {

	private Long cost;
	private boolean freeService;
	private int level;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Long getCost() {
		return cost;
	}
	public void setCost(Long cost) {
		this.cost = cost;
	}
	public boolean isFreeService() {
		return freeService;
	}
	public void setFreeService(boolean freeService) {
		this.freeService = freeService;
	}	
}
