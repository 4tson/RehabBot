package mx.fortson.rehab.enums;

import mx.fortson.rehab.channels.BotCommands;
import mx.fortson.rehab.channels.DuelArena;
import mx.fortson.rehab.channels.Farms;
import mx.fortson.rehab.channels.FreeFarms;
import mx.fortson.rehab.channels.HighLow;
import mx.fortson.rehab.channels.IChannel;
import mx.fortson.rehab.channels.Services;
import mx.fortson.rehab.channels.ServicesShop;
import mx.fortson.rehab.channels.Shop;

public enum ChannelsEnum {

	DUELARENA("duel-arena",CategoriesEnum.GAMBA,new DuelArena(),0, new RolesEnum[]{RolesEnum.DEGEN},new RolesEnum[]{RolesEnum.IRONMAN}),
	HIGHLOW("high-low",CategoriesEnum.GAMBA,new HighLow(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	BOTDISCUSSION("bot-discussion",CategoriesEnum.BOT,null,0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	BOTCOMMANDS("bot-commands",CategoriesEnum.BOT,new BotCommands(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	ANNOUNCEMENTS("announcements",CategoriesEnum.BOT,null,0,new RolesEnum[]{},new RolesEnum[] {RolesEnum.EVERYONE}),
	SERVICESSHOP("services-shop",CategoriesEnum.SERVICES,new ServicesShop(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	SERVICES("services",CategoriesEnum.SERVICES,new Services(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	BIDSERVICE("bidding-services",CategoriesEnum.SERVICES,null,0,new RolesEnum[]{RolesEnum.DEGEN},new RolesEnum[]{}),
	SHOP("shop",CategoriesEnum.SHOP,new Shop(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),		
	FARMS("farms",CategoriesEnum.FARMS,new Farms(),0,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	FREEFARMS("free-farms",CategoriesEnum.FARMS,new FreeFarms(),120,new RolesEnum[]{RolesEnum.DEGEN,RolesEnum.IRONMAN},new RolesEnum[] {}),
	ALL("",null,null,0,null,null)
	;
	
	private String name;
	private IChannel channel;
	private CategoriesEnum category;
	private int slowmode;
	private RolesEnum[] permitedRoles;
	private RolesEnum[] readOnlyRoles;
	
	private ChannelsEnum(String name,CategoriesEnum category, IChannel channel, int slowmode, RolesEnum[] permitedRoles,RolesEnum[] readOnlyRoles) {
		this.name = name;
		this.channel = channel;
		this.category = category;
		this.slowmode = slowmode;
		this.permitedRoles = permitedRoles;
		this.readOnlyRoles = readOnlyRoles;
	}
	
	
	public RolesEnum[] getReadOnlyRoles() {
		return readOnlyRoles;
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
	
	public CategoriesEnum getCategory() {
		return category;
	}


	public String getName() {
		return name;
	}

	public IChannel getChannel() {
		return channel;
	}
}
