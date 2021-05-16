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
							int itemId = DatabaseDegens.getItemIdByName(itemName);
							DatabaseDegens.createItem(itemId, farmedAmount,DatabaseDegens.getDegenId(id),false);
							farmResultBean.setItemName(itemName);
							break;
						case SERVICE:
							int serviceLevel = RandomUtils.randomInt(degenLevel + 2);
							String serviceName = RandomUtils.randomStringFromArray(farmResult.getType().getPossibleItems());
							
							int maxFarms = serviceLevel > 40 ? 12 : serviceLevel > 30 ? 10 : serviceLevel > 18 ? 9 : serviceLevel > 12 ? 8 : serviceLevel > 8 ? 7 : serviceLevel > 4 ? 6 : serviceLevel > 2 ? 5 : 4;
							int farmsLowerBound = 2;
							int farmsFarmed = RandomUtils.randomInt(maxFarms-farmsLowerBound) + farmsLowerBound;
							double rateHour = Double.parseDouble(String.format("%.1f",RandomUtils.randomDouble(1.0, maxFarms * .5)));
							int intervalLowerBound = serviceLevel > 30 ? 0 : serviceLevel > 20 ? 1 : serviceLevel > 10 ? 2 : 3;  
							int interval = RandomUtils.randomInt(6 - intervalLowerBound) + intervalLowerBound;
							
							farmResultBean.setName(serviceName);
							farmResultBean.setFarms(farmsFarmed);
							farmResultBean.setRateHours(rateHour);
							farmResultBean.setInterval(interval);
							farmResultBean.setLevel(serviceLevel);
							DatabaseDegens.createService(serviceName, farmsFarmed, rateHour,interval, DatabaseDegens.getDegenId(id),3, serviceLevel);
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
	
	public static double getMultiplier(int degenId) {
		try {
			return DatabaseDegens.getFarmMultiplier(degenId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static int addFarmsToUser(long idLong) {
		try {
			int degenId = DatabaseDegens.getDegenId(idLong);
			int freeFarms = RandomUtils.randomFreeFarm();
			double multiplier = getMultiplier(degenId);
			DatabaseDegens.addFarmAtt(Math.toIntExact(Math.round(freeFarms * multiplier)), degenId);
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

	public static double getMultiplier(Long ownerID) {
		try {
			return getMultiplier(DatabaseDegens.getDegenId(ownerID));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
}
