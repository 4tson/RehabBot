package mx.fortson.rehab.utils;

import java.sql.SQLException;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.LevelBean;

public class LevelUtils {

	public static LevelBean getNextLevel(long discId) {
		try {
			LevelBean level = DatabaseDegens.getDegenNextLevel(discId);
			level.setHiddenMultiplier(DatabaseDegens.getFarmMultiplier(DatabaseDegens.getDegenId(discId)));
			return level;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
