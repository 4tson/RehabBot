package mx.fortson.rehab.enums;

public enum RolesEnum {

	DEGEN("degen"),
	IRONMAN("ironman"),
	;
	private String name;

	public String getName() {
		return name;
	}

	private RolesEnum(String name) {
		this.name = name;
	}
	
	
}
