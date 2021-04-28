package mx.fortson.rehab.enums;

import mx.fortson.rehab.utils.FormattingUtils;

public enum RehabCommandsEnum {

	COMMANDS("!commands","Wait, really?", true),
	
	REGISTER("!degen","Allows you to use the other commands from this bot.", true),
	
	DUEL("!duel [@mention] [amount]","Begins a duel, if you don't mention a person to duel you will stake a random from the arena. "
							+ "Can only duel with available funds. If no amount is defined, a random amount will be staked from the available funds.", true),
	
	DUELRANDO("!duel","ThisShouldntBeShown",false),
	
	DUELSETAMOUNT("!duel [amount]","AlsoNotShown",false),
	
	FUNDS("!bank","Displays your current bank value.", true),
	
	FARM("!farm [x]","Allows you to get a random amount of GP. Limit 3 per day! Optionally define how many farms you want to execute (max 15)", true),
	
	INVENTORY("!inv","Allows you to take a peek into your inventory.", true),
	
	LDRBOARD("!leaderboard", "Shows you a list of all current degens and their bank values", true),
	
	GIFTCHUCK("!giftchuck [@mention] [amount]", "Allows you to give a gift chuck, it is a duel but the winnings to to the person mentioned, the person doing the command take the loss though.",true),
	
	CHUCK("!chuck","Also known as `!duel allin`, starts a duel with all your available funds",true),
	
	ACCEPT("!accept","",false),
	
	ACTIVATESERVICE("!activate [id]","Allows you to activate a service based on its ID",true),
	
	NON_EXISTANT("","",false),
	;
	
	
	String command;
	String description;
	boolean active;
	
	private RehabCommandsEnum(String command, String description, boolean active) {
		this.command = command;
		this.description = description;
		this.active = active;
	}
	
	public static RehabCommandsEnum fromCommand(String command) {
		//Checking for duel command
		String[] splitCommand = command.split(" ");
		if(splitCommand.length==3) {
			if(splitCommand[0].equalsIgnoreCase("!duel") &&
					splitCommand[1].startsWith("<@") &&
					 FormattingUtils.isValidAmount(splitCommand[2])) {
				return DUEL;
			}
			if(splitCommand[0].equalsIgnoreCase("!giftchuck") &&
					splitCommand[1].startsWith("<@") &&
					FormattingUtils.isValidAmount(splitCommand[2])) {
				return GIFTCHUCK;
			}
		}else if(splitCommand.length==2){
			if(splitCommand[0].equalsIgnoreCase("!duel") &&
					(FormattingUtils.isValidAmount(splitCommand[1]) || splitCommand[1].equalsIgnoreCase("allin"))) {
				return DUELSETAMOUNT;
			}
			if(splitCommand[0].equalsIgnoreCase("!accept")) {
				return ACCEPT;
			}
			if(splitCommand[0].equalsIgnoreCase("!activate")) {
				return ACTIVATESERVICE;
			}
			if(splitCommand[0].equalsIgnoreCase("!farm")) {
				return FARM;
			}
		}else if(splitCommand.length == 1){
			if(splitCommand[0].equalsIgnoreCase("!farm")) {
				return FARM;
			}else {
				for(RehabCommandsEnum commandEnum : RehabCommandsEnum.values()) {
					if(commandEnum.getCommand().equalsIgnoreCase(splitCommand[0])) {
						return commandEnum;
					}
				}
			}
		}
		return NON_EXISTANT;
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
}
