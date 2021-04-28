package mx.fortson.rehab.enums;

public enum ShopCommandsEnum {
	COMMANDS("!commands","Wait, really? But in #shop", true),
	BUY("!buy id", "Lets you buy an item based on its ID, provided you have the funds to do so.", true),
	SELL("!sell id", "Lets you put an item for sale in the shop for other players based on its ID.", true),
	SHOP("!shop", "Lets you see all the items for sale, their IDs, and who is selling them.", true),
	CANCEL_SALE("!cancel id", "Lets you cancel a sale.", true),
	ADD_ROLE("!addrole","Adds the `shop` role, this means you will get pinged when a new item is put for sale",true),
	REM_ROLE("!remrole","Removes the `shop` role, this means you wont get pinged when a new item is put for sale",true),
	SELL_SERVICE("!sellservice id", "Lets you put a service for sale in the shop for other players based on its ID.", true),
	BUY_SERVICE("!buyservice id", "Lets you buy a service based on its ID, provided you have the funds to do so.", true),
	NON_EXISTANT("","",false),
	;

	String command;
	String description;
	boolean active;
	
	private ShopCommandsEnum(String command, String description, boolean active) {
		this.command = command;
		this.description = description;
		this.active = active;
	}
	public String getCommand() {
		return command;
	}

	public String getDescription() {
		return description;
	}

	public boolean isActive() {
		return active;
	}

	public static ShopCommandsEnum fromCommand(String command) {
		//Checking for duel command
		String[] splitCommand = command.split(" ");
		for(ShopCommandsEnum commandEnum : ShopCommandsEnum.values()) {
			if(commandEnum.getCommand().split(" ")[0].equalsIgnoreCase(splitCommand[0])) {
				return commandEnum;
			}
		}
		return NON_EXISTANT;
	}
}
