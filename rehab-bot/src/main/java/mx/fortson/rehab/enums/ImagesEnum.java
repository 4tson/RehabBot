package mx.fortson.rehab.enums;

public enum ImagesEnum {

	INVENTORY("/inventory.png"),
	SHOP_INDICATOR("/shopindicator.png"),
	LEADERBOARD("/leaderboard.png"),
	IRON_INDICATOR("/btwHelmet.png"),
	;
	
	private String fileName;
	
	private ImagesEnum(String fileName) {
		this.fileName = fileName;
	}


	public String getFileName() {
		return fileName;
	}
}
