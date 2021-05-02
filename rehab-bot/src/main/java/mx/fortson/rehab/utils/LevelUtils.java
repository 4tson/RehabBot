package mx.fortson.rehab.utils;

import java.sql.SQLException;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.LevelBean;

public class LevelUtils {

	public static LevelBean getNextLevel(long discId) {
		try {
			return DatabaseDegens.getDegenNextLevel(discId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
