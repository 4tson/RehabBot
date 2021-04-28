package mx.fortson.rehab.utils;

import java.sql.SQLException;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.BankBean;

public class FundUtils {

	public static Long getFunds(long id) {
		try {
			return DatabaseDegens.getFundsById(id);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BankBean getBankValue(long id) {
		try {
			BankBean result = new BankBean();
			result.setCash(DatabaseDegens.getFundsById(id));
			result.setInventoryWorth(DatabaseDegens.getInventoryWorth(id));
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
