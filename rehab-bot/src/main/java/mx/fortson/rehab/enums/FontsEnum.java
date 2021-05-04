package mx.fortson.rehab.enums;

public enum FontsEnum {

	OSRS("/fonts/runescape_uf.ttf")
	;
	private String fileName;

	private FontsEnum(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	
}
