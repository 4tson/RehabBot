package mx.fortson.rehab.enums;

public enum RehabCommandsEnum {

	COMMANDS("!commands","Wait, really?",ChannelsEnum.ALL, true),
	
	INVENTORY("!inv","Allows you to take a peek into your inventory.",ChannelsEnum.ALL, true),
	
	FUNDS("!bank","Displays your current bank value.",ChannelsEnum.ALL, true),
	
	REGISTERDEGEN("!degen","Registers you as a degen. Degens have access to all features of the bot.",ChannelsEnum.BOTCOMMANDS, true),
	
	REGISTERIRON("!ironman","Registers you as an ironman. Ironmen have limited access to the bot's features.",ChannelsEnum.BOTCOMMANDS, true),
	
	DUEL("!duel [@mention] [amount]","Begins a duel, if you don't mention a person to duel you will stake a random from the arena. "
							+ "Can only duel with available funds. If no amount is defined, a random amount will be staked from the available funds.",ChannelsEnum.DUELARENA, true),
	
	CHUCK("!chuck","Also known as `!duel allin`, starts a duel with all your available funds",ChannelsEnum.DUELARENA,true),	

	GIFTCHUCK("!giftchuck [@mention] [amount]", "Allows you to give a gift chuck, it is a duel but the winnings to to the person mentioned, the person doing the command take the loss though.",ChannelsEnum.DUELARENA,true),
	
	FARM("!farm [x]","Allows you to go on an adventure with a chance at winning big. Optionally define how many farms you want to execute (max 15)",ChannelsEnum.FARMS, true),
	
	LDRBOARD("!leaderboard", "Shows you a list of all current degens and their bank values",ChannelsEnum.BOTCOMMANDS, true),
	
	ADDSERVICES("!addrole","Adds the services role, this means you will get pinged when a new service is up for bid.",ChannelsEnum.SERVICES, true),
	
	REMSERVICES("!remrole","Removes the services role, this means you wont get pinged when a new service is up for bid.",ChannelsEnum.SERVICES, true),
	
	SERVICESTATUS("!status","Gets the remaining time for the active status if there is one.",ChannelsEnum.BIDSERVICE, true),
	
	BIDSERVICE("!bid [amount]", "Bids an amount of GP for the current service.",ChannelsEnum.BIDSERVICE, true),
	
	ACTIVATESERVICE("!activate [id]","Allows you to activate a service based on its ID",ChannelsEnum.SERVICES,true),
	;
	
	
	String command;
	String description;
	boolean active;
	ChannelsEnum channel;
	
	private RehabCommandsEnum(String command, String description, ChannelsEnum channel,boolean active) {
		this.command = command;
		this.description = description;
		this.active = active;
		this.channel = channel;
	}
	
	public static RehabCommandsEnum fromCommand(String command, ChannelsEnum channel) {
		String[] splitCommand = command.split(" ");
		for(RehabCommandsEnum commandEnum : RehabCommandsEnum.values()) {
			String[] splitCommandEnum = commandEnum.getCommand().split(" ");
			if(splitCommandEnum[0].equalsIgnoreCase(splitCommand[0])
					&& (commandEnum.getChannel().equals(channel) || commandEnum.getChannel().equals(ChannelsEnum.ALL))
					&& commandEnum.isActive()) {
				return commandEnum;
			}
		}
		return null;
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

	public ChannelsEnum getChannel() {
		return channel;
	}
	
}
