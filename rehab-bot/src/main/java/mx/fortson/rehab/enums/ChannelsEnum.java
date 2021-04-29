package mx.fortson.rehab.enums;

import mx.fortson.rehab.channels.BotCommands;
import mx.fortson.rehab.channels.DuelArena;
import mx.fortson.rehab.channels.Farms;
import mx.fortson.rehab.channels.FreeFarms;
import mx.fortson.rehab.channels.IChannel;
import mx.fortson.rehab.channels.Services;
import mx.fortson.rehab.channels.ServicesShop;
import mx.fortson.rehab.channels.Shop;

public enum ChannelsEnum {

	DUELARENA("duel-arena","arena",new DuelArena(),3, RolesEnum.DEGEN),
	BOTDISCUSSION("bot-discussion","bot",null,0,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	BOTCOMMANDS("bot-commands","bot",new BotCommands(),0,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	SERVICESSHOP("services-shop","services",new ServicesShop(),0,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	SERVICES("services","services",new Services(),0,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	SHOP("shop","shop",new Shop(),0,RolesEnum.DEGEN,RolesEnum.IRONMAN),		
	FARMS("farms","farms",new Farms(),0,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	FREEFARMS("free-farms","farms",new FreeFarms(),120,RolesEnum.DEGEN,RolesEnum.IRONMAN),
	BIDSERVICE("bidding-services","services",null,0,RolesEnum.DEGEN),
	ALL("",null,null,0)
	;
	
	private String name;
	private IChannel channel;
	private String category;
	private int slowmode;
	private RolesEnum[] permitedRoles;
	
	private ChannelsEnum(String name,String category, IChannel channel, int slowmode, RolesEnum...permitedRoles) {
		this.name = name;
		this.channel = channel;
		this.category = category;
		this.slowmode = slowmode;
		this.permitedRoles = permitedRoles;
	}
	
	public RolesEnum[] getPermitedRoles() {
		return permitedRoles;
	}


	public static ChannelsEnum fromName(String name) {
		for(ChannelsEnum channel : ChannelsEnum.values()) {
			if(channel.getName().equals(name)) {
				return channel;
			}
		}
		return null;
	}
	
	public int getSlowmode() {
		return slowmode;
	}

	public String getCategory() {
		return category;
	}

	
	
	
	public String getName() {
		return name;
	}

	public IChannel getChannel() {
		return channel;
	}
}
