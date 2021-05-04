package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.PlayerWinsBean;

public class HighLowUtils {

	public static void saveState(Map<Long, PlayerWinsBean> playersWins) {
		try {
			DatabaseDegens.clearHighLowState();
			if(!playersWins.isEmpty()) {
				DatabaseDegens.insertHighLowState(playersWins);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Map<Long, PlayerWinsBean> loadState() {
		Map<Long,PlayerWinsBean> state = new HashMap<>();
		try {
			state = DatabaseDegens.selectHighLowState();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

}
