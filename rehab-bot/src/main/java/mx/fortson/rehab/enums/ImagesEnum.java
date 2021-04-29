package mx.fortson.rehab.enums;

public enum ImagesEnum {

	INVENTORY("/inventory.png"),
	;
	
	private String fileName;
	
	private ImagesEnum(String fileName) {
		this.fileName = fileName;
	}


	public String getFileName() {
		return fileName;
	}
}
