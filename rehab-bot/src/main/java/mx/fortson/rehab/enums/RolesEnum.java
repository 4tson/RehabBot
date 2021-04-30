package mx.fortson.rehab.enums;

public enum RolesEnum {

	DEGEN("degen"),
	IRONMAN("ironman"),
	EVERYONE("@everyone"),
	ANNOUNCEMENTS("announcements"),
	SERVICES("services"),
	SHOPPER("shopper"),
	;
	private String name;

	public String getName() {
		return name;
	}

	private RolesEnum(String name) {
		this.name = name;
	}
	
	
}
