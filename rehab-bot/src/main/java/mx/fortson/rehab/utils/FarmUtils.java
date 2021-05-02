package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.FarmCollectionBean;
import mx.fortson.rehab.bean.FarmResultBean;
import mx.fortson.rehab.enums.FarmResultEnum;

public class FarmUtils {

	public static FarmCollectionBean farmForUser(Long id, int farms) {
		FarmCollectionBean result = new FarmCollectionBean();
		List<FarmResultBean> farmList = new ArrayList<>();
		try {
			
			if(DatabaseDegens.existsById(id)) {
				
				result.setExists(true);
				Map<String, Object> degenData =  DatabaseDegens.getDegenData(id);
				int oldAttempts = (int) degenData.get("FARMATT");
				Long oldFunds = (Long) degenData.get("FUNDS");
				if(oldAttempts > 0) {
					if(farms > oldAttempts) {
						farms = oldAttempts;
					}
					int degenLevel = DatabaseDegens.getDegenNextLevel(id).getLevel();
					result.setAttempts(true);
					int newAttempts = oldAttempts;
					Long totalFarmed = 0L;
					Long newFunds = oldFunds;
					for(int i = 0; i<farms; i++) {
						FarmResultBean farmResultBean = new FarmResultBean();
						newAttempts = newAttempts -1;
						int itemType = 1;
						FarmResultEnum farmResult = RandomUtils.randomFarmEnum();
						farmResultBean.setType(farmResult.getType());					
						farmResultBean.setFlavourText(RandomUtils.randomStringFromArray(farmResult.getFlavourTexts()));
						Long farmedAmount = RandomUtils.randomAmountFromRange(farmResult.getAmountRange());
						farmResultBean.setFarmedAmount(farmedAmount);
						totalFarmed = totalFarmed + farmedAmount;
						switch(farmResult.getType()) {
						case CASH:
							newFunds = newFunds + farmedAmount;
							break;
						case ITEM_UNIQUE:
							itemType+=1;
						case ITEM_GREAT:
							itemType+=1;
						case ITEM_MEH:
							List<String> itemNames = DatabaseDegens.getItemsFromType(itemType);
							String itemName = RandomUtils.randomStringFromList(itemNames);
							DatabaseDegens.createItem(itemName, farmedAmount,DatabaseDegens.getDegenId(id),false);
							farmResultBean.setItemName(itemName);
							break;
						case SERVICE:
							int serviceLevel = RandomUtils.randomInt(degenLevel + 2);
							String serviceName = RandomUtils.randomStringFromArray(farmResult.getType().getPossibleItems());
							int maxFarms = serviceLevel > 2 ? 5 : serviceLevel > 4 ? 6 : serviceLevel > 8 ? 7 : serviceLevel > 12 ? 8 : serviceLevel > 18 ? 9 : 4;
							int farmsFarmed = RandomUtils.randomInt(maxFarms);
							double rateHour = Double.parseDouble(String.format("%.1f",RandomUtils.randomDouble(1.0, maxFarms * .5)));
							int interval = RandomUtils.randomInt(6);
							farmResultBean.setName(serviceName);
							farmResultBean.setFarms(farmsFarmed);
							farmResultBean.setRateHours(rateHour);
							farmResultBean.setInterval(interval);
							farmResultBean.setLevel(serviceLevel);
							DatabaseDegens.createService(serviceName, farmsFarmed, rateHour,interval, DatabaseDegens.getDegenId(id),false, serviceLevel);
							break;
						}
						farmList.add(farmResultBean);
					}
					result.setFarms(farmList);
					result.setNewAttempts(newAttempts);
					result.setFarmedAmount(totalFarmed);
					result.setNewFunds(newFunds);
					DatabaseDegens.updateFarmAtt(result.getNewAttempts(), (int) degenData.get("DEGENID"), farms);
					DatabaseDegens.updateFunds(result.getNewFunds(),(int) degenData.get("DEGENID"));
				}else {
					result.setAttempts(false);
					result.setNewFunds(oldFunds);
					result.setFarmedAmount(0L);
				}
		}else {
			result.setExists(false);
		}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void addSetFarmsToUser(long idLong, int farmsToAdd) {
		try {
			
			DatabaseDegens.addFarmAtt(farmsToAdd, DatabaseDegens.getDegenId(idLong));
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int addFarmsToUser(long idLong) {
		try {
			int freeFarms = RandomUtils.randomFreeFarm();
			
			DatabaseDegens.addFarmAtt(freeFarms, DatabaseDegens.getDegenId(idLong));
			return freeFarms;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getFarms(long idLong) {
		try {
			if(DatabaseDegens.existsById(idLong)) {	
				return DatabaseDegens.getFarmAtts(idLong);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
}
