package mx.fortson.rehab.enums;

public enum ImagesEnum {

	INVENTORY("/inventory.png"),
	LEVELICON("/levelicon.png"),
	SHOP_INDICATOR("/shopindicator.png");
	;
	
	private String fileName;
	
	private ImagesEnum(String fileName) {
		this.fileName = fileName;
	}


	public String getFileName() {
		return fileName;
	}
}
