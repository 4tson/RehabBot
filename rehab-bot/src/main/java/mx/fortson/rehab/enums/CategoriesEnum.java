package mx.fortson.rehab.enums;

public enum CategoriesEnum {

	GAMBA("gamba",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	BOT("bot",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	SERVICES("services",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	SHOP("shop",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	FARMS("farms",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	MYSERVICES("my-services",new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN}),
	;
	private RolesEnum[] permitedRoles;
	private String name;

	public String getName() {
		return name;
	}

	public RolesEnum[] getPermitedRoles() {
		return permitedRoles;
	}

	private CategoriesEnum(String name,RolesEnum[] permitedRoles) {
		this.name = name;
		this.permitedRoles = permitedRoles;
	}
	
}
