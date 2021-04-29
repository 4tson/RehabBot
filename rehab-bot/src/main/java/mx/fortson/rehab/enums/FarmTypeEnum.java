package mx.fortson.rehab.enums;

import mx.fortson.rehab.constants.RehabBotConstants;

public enum FarmTypeEnum {

	CASH(new String[] {}),
	ITEM_GREAT(null),
	ITEM_MEH(null),
	ITEM_UNIQUE(null),
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
