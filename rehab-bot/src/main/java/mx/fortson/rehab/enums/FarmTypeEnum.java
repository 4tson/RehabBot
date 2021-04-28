package mx.fortson.rehab.enums;

import mx.fortson.rehab.constants.RehabBotConstants;

public enum FarmTypeEnum {

	CASH(new String[] {}),
	ITEM_GREAT(new String[] {"Twisted Bow", "Scythe of vitur", "3a druidic top", "3a druidic bot", "Blue Partyhat", "Purple Partyhat", "Red Partyhat", "Yellow Partyhat", "Green Partyhat", "White Partyhat", "Black Partyhat","Ranger boots","Hydra's claw"}),
	ITEM_MEH(new String[] {"Blowpipe", "Staff of the dead","Zamorakian Hasta","Hydra leather","Bando's chestplate","Ring of the gods","Zenyte shard", "Godsword Shard 1", "Godsword Shard 1"}),
	ITEM_UNIQUE(new String[] {"Customer Support", "Expensive rock","Exodia R. Arm","Exodia L. Arm","Exodia R. Leg","Exodia L. Leg","Exodia Head"}),
	SERVICE(RehabBotConstants.SERVICE_NAMES),
	;

	String[] possibleItems;
	
	public String[] getPossibleItems() {
		return possibleItems;
	}

	private FarmTypeEnum(String[] possibleItems) {
		this.possibleItems = possibleItems;
	}
}
