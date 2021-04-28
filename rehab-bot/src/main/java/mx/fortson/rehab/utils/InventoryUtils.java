package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.ItemBean;

public class InventoryUtils {

	public static List<ItemBean> getInventory(long discId) {
		List<ItemBean> inventory = new ArrayList<>();
		try {
			inventory.addAll(DatabaseDegens.getDegenInventory(discId));
			inventory.addAll(DatabaseDegens.getDegenServices(discId));
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return inventory;
	}

}
