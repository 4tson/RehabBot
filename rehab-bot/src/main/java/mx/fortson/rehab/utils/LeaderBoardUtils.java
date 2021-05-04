package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.Degen;

public class LeaderBoardUtils {

	public static List<Degen> getLeaderBoard() {
		try {
			
			List<Map<String,Object>> queryResult = DatabaseDegens.getAllDegens();
			List<Degen> result = new ArrayList<>();
			for(Map<String,Object> queryRecord : queryResult) {
				Degen degen = new Degen();
				degen.setBank((Long) queryRecord.get("FUNDS"));
				degen.setName((String) queryRecord.get("NAME"));
				degen.setFarmAttempts((int) queryRecord.get("FARMATT"));
				degen.setTimesFarmed((int) queryRecord.get("TIMESFARMED"));
				degen.setWins((int) queryRecord.get("WINS"));
				degen.setLosses((int) queryRecord.get("LOSSES"));
				degen.setPeak((Long) queryRecord.get("PEAK"));
				degen.setLevel((int) queryRecord.get("LEVEL"));
				degen.setIronman( (boolean) queryRecord.get("IRONMAN"));
				result.add(degen);
			}
			Collections.sort(result,Comparator.comparing(Degen::getLevel).thenComparing(Degen::getBank).thenComparing(Degen::getPeak).reversed());
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
