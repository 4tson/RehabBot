package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.text.translate.UnicodeUnescaper;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.SlotsResultBean;
import mx.fortson.rehab.bean.SlotsRollBean;

public class SlotsUtils {

	public static SlotsResultBean roll(long idLong) {
		SlotsResultBean result = new SlotsResultBean();
		List<SlotsRollBean> rolls = getCurrentPayouts();
		result.setDiscId(idLong);
		if(null == rolls) {
			result.setFlavourText("There was an error rolling, you were not charged.");
		}else {
			if(FarmUtils.getFarms(idLong)<5) {
				result.setFlavourText("You need 5 farms to play slots.");
			}else {
				//remove the farms
				FarmUtils.addSetFarmsToUser(idLong, -5);
				//roll
				SlotsRollBean[] rollResult = resolveRoll(3,rolls);
				if(isWin(rollResult)) {
					result.setWon(true);
					result.setFarmsWon(rollResult[0].getPayout());
					boolean isJackpot = rollResult[0].getName().equalsIgnoreCase("Jackpot");
					result.setJackpot(isJackpot);
					FarmUtils.addSetFarmsToUser(idLong, rollResult[0].getPayout());
					if(isJackpot) {
						resetJackpot();
					}else {
						addToJackpot();
					}
				}else {
					addToJackpot();
				}
				result.setFlavourText(getRollEmojis(rollResult));
			}
		}
		return result;
	}

	private static void addToJackpot() {
		try {
			DatabaseDegens.addToJackpot();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	
	}

	private static void resetJackpot() {
		try {
			DatabaseDegens.resetJackpot();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static List<SlotsRollBean> getCurrentPayouts() {
		List<SlotsRollBean> rolls = null;
		try {
			rolls = DatabaseDegens.getSlotsRolls();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rolls;
	}

	private static String getRollEmojis(SlotsRollBean[] rollResult) {
		StringBuilder sb = new StringBuilder();
		for(SlotsRollBean slotRoll : rollResult) {
			sb.append(new UnicodeUnescaper().translate(slotRoll.getUnicode()));
		}
		return sb.toString();
	}

	private static boolean isWin(SlotsRollBean[] rollResult) {
		SlotsRollBean first = rollResult[0];
		for(int i = 1; i < rollResult.length; i++) {
			if(!rollResult[i].equals(first)) {
				return false;
			}
		}
		return true;
	}

	private static SlotsRollBean[] resolveRoll(int size, List<SlotsRollBean> rolls) {
		SlotsRollBean[] result = new SlotsRollBean[size];
		for(int i = 0; i<size; i++) {
			result[i] = RandomUtils.randomSlotRoll(rolls);
		}
		return result;
	}

	public static int getCurrentJackpot() {
		int jackpot = 0;
		try {
			jackpot = DatabaseDegens.getCurrentJackpot();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return jackpot;
	}
}
