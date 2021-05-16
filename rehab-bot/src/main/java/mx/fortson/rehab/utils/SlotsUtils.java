package mx.fortson.rehab.utils;

import mx.fortson.rehab.bean.SlotsRollBean;
import mx.fortson.rehab.enums.SlotsEnum;

public class SlotsUtils {

	public static SlotsRollBean roll(long idLong) {
		SlotsRollBean result = new SlotsRollBean();
		result.setDiscId(idLong);
		if(FarmUtils.getFarms(idLong)<5) {
			result.setFlavourText("You need 5 farms to play slots.");
		}else {
			//remove the farms
			FarmUtils.addSetFarmsToUser(idLong, -5);
			//roll
			SlotsEnum[] rollResult = resolveRoll(3);
			if(isWin(rollResult)) {
				result.setWon(true);
				result.setFarmsWon(rollResult[0].getPayout());
				FarmUtils.addSetFarmsToUser(idLong, rollResult[0].getPayout());
			}
			result.setFlavourText(getRollEmojis(rollResult));
		}
		return result;
	}

	private static String getRollEmojis(SlotsEnum[] rollResult) {
		StringBuilder sb = new StringBuilder();
		for(SlotsEnum slotRoll : rollResult) {
			sb.append(slotRoll.getUnicodeEmoji());
		}
		return sb.toString();
	}

	private static boolean isWin(SlotsEnum[] rollResult) {
		SlotsEnum first = rollResult[0];
		for(int i = 1; i < rollResult.length; i++) {
			if(!rollResult[i].equals(first)) {
				return false;
			}
		}
		return true;
	}

	private static SlotsEnum[] resolveRoll(int size) {
		SlotsEnum[] result = new SlotsEnum[size];
		for(int i = 0; i<size; i++) {
			result[i] = RandomUtils.randomSlotRoll();
		}
		return result;
	}
}
