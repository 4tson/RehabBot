package mx.fortson.rehab.enums;

public enum RehabCommandsEnum {

	COMMANDS("!commands","Wait, really?",ChannelsEnum.ALL, true),
	
	INVENTORY("!inv","Allows you to take a peek into your inventory.",ChannelsEnum.ALL, true),
	
	FUNDS("!bank","Displays your current bank value.",ChannelsEnum.ALL, true),
	
	FARMS("!farms","Displays your current available farms.",ChannelsEnum.ALL, true),
	
	DEACTIVATE("!inactive","Removes your rolls and stops all your running services. You can come back at any time with `!degen` or `!ironman`",ChannelsEnum.BOTCOMMANDS, true),
	
	WIPE("!wipe","Deletes all your data, you can still come back at any time. **THIS ACTION CANNOT BE UNDONE**",ChannelsEnum.BOTCOMMANDS, true),
	
	REGISTERDEGEN("!degen","Registers you as a degen. Degens have access to all features of the bot.",ChannelsEnum.BOTCOMMANDS, true),
	
	REGISTERIRON("!ironman","Registers you as an ironman. Ironmen have limited access to the bot's features.",ChannelsEnum.BOTCOMMANDS, true),
	
	ADDANNOUNCEMENTROLE("!announcements","Adds the announcement role. You will get pinged when there are any announcements.",ChannelsEnum.BOTCOMMANDS, true),
	
	DUEL("!duel [@mention] [amount]","Begins a duel, if you don't mention a person to duel you will stake a random from the arena. "
							+ "Can only duel with available funds. If no amount is defined, a random amount will be staked from the available funds.",ChannelsEnum.DUELARENA, true),
	
	CHUCK("!chuck","Also known as `!duel allin`, starts a duel with all your available funds",ChannelsEnum.DUELARENA,true),	

	GIFTCHUCK("!giftchuck [@mention] [amount]", "Allows you to give a gift chuck, it is a duel but the winnings to to the person mentioned, the person doing the command take the loss though.",ChannelsEnum.DUELARENA,true),
	
	FARM("!farm [x]","Allows you to go on an adventure with a chance at winning big. Optionally define how many farms you want to execute.",ChannelsEnum.FARMS, true),
	
	LDRBOARD("!leaderboard", "Shows you a list of all current degens and their bank values",ChannelsEnum.BOTCOMMANDS, true),
	
	ADDSERVICES("!addrole","Adds the services role, this means you will get pinged when a new service is up for bid.",ChannelsEnum.SERVICES, true),
	
	REMSERVICES("!remrole","Removes the services role, this means you wont get pinged when a new service is up for bid.",ChannelsEnum.SERVICES, true),
	
	SERVICESTATUS("!status","Gets the remaining time for the active status if there is one.",ChannelsEnum.BIDSERVICE, true),
	
	BIDSERVICE("!bid [amount]", "Bids an amount of GP for the current service.",ChannelsEnum.BIDSERVICE, true),
	
	BUYSERVICESHOP("!buy [id]", "Buys one of the predetermined services in the shop.", ChannelsEnum.SERVICESSHOP, true),
	
	ACTIVATESERVICE("!activate [id]","Allows you to activate a service based on its ID",ChannelsEnum.SERVICES,true),
	
	SHOPAVAIL("!shop", "Lets you see all the items for sale, their IDs, and who is selling them.",ChannelsEnum.SHOP, true),
	
	BUYSHOP("!buy [id]", "Lets you buy an item based on its ID, provided you have the funds to do so.", ChannelsEnum.SHOP, true),
	
	SELLSHOP("!sell [id] [price]", "Lets you put an item for sale in the shop for other players based on its ID. If you don't define a price, the value will be taken as price.", ChannelsEnum.SHOP, true),
	
	CANCEL_SALE("!cancel [id]", "Lets you cancel a sale.",ChannelsEnum.SHOP, true),
	
	ADDROLESHOP("!addrole","Adds the `shopper` role, this means you will get pinged when a new item is put for sale",ChannelsEnum.SHOP, true),
	
	REMROLESHOP("!remrole","Removes the `shopper` role, this means you wont get pinged when a new item is put for sale",ChannelsEnum.SHOP, true),
	
	SELLSERVICE("!sellservice [id] [price]", "Lets you put a service for sale in the shop for other players based on its ID. You need to define a price.",ChannelsEnum.SHOP, true),
	
	BUY_SERVICE("!buyservice [id]", "Lets you buy a service based on its ID, provided you have the funds to do so.",ChannelsEnum.SHOP, true),
	
	HIGH("!high", "You bet that the next number is higher than the previous one.",ChannelsEnum.HIGHLOW, true),
	
	LOW("!low", "You bet that the next number is lower than the previous one.",ChannelsEnum.HIGHLOW, true),
	
	STATUSHIGHLOW("!status", "Check how many farms you are about to lose.",ChannelsEnum.HIGHLOW,true),
	
	CASHOUT("!cashout","Take your farms and get out of here.",ChannelsEnum.HIGHLOW,true),
	
	LEVELUP("!levelup","Pay a bit of money to level up and unlock some perks by doing so.",ChannelsEnum.BOTCOMMANDS,true),
	
	CHECKLEVEL("!level","Get your current level.", ChannelsEnum.BOTCOMMANDS, true),
	
	ADDUNIQUES("!uniques","Adds the uniques role, this means you will get pinged when a unique or service is farmed by someone.",ChannelsEnum.BOTCOMMANDS, true),
	
	REMUNIQUES("!remuniques","Removes the uniques role, this means you wont get pinged when a unique or service is farmed by someone.",ChannelsEnum.BOTCOMMANDS, true),
	
	TRADEINDUPE("!tradein [id]","Trades a duplicate item for farms. The amount of farms you get is calculated from the rarity of the item as well as its value.",ChannelsEnum.DUPETRADEIN, true),
	
	ROLL("!roll","Place your 5 farms in the slot machine and roll like the degenerate you are.",ChannelsEnum.SLOTS, true),
	
	PAYOUTS("!payouts","Show the information for the payouts of each roll.",ChannelsEnum.SLOTS, true),
	
	JACKPOT("!jackpot","Displays the current jackpot value.",ChannelsEnum.SLOTS, true),
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
