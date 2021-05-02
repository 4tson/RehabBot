package mx.fortson.rehab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.bean.DegenBean;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.LevelBean;
import mx.fortson.rehab.bean.PlayerWinsBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.constants.DBQueriesConstants;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.database.DegensDataSource;

public class DatabaseDegens {
	
	public static boolean existsById(long id) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.COUNT_BY_DISC_ID)){
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1)>=1;
		}
	}

	public static int insertNewDegen(long id, String name, boolean iron) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.INSERT_NEW_DEGEN)){
			stmt.setLong(1,id);
			stmt.setString(2, name);
			stmt.setBoolean(3, iron);
			return stmt.executeUpdate();
		}
	}
	public static Long getInitialFunds() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_PROPERTY)){
			stmt.setString(1, RehabBotConstants.INITIAL_FUNDS);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Long.parseLong(rs.getString(1));
			}
			return null;
		}
	}

	public static Integer getInitialFarmAttempts() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_PROPERTY)){
			stmt.setString(1, RehabBotConstants.INITIAL_FARM_ATTEMPTS);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Integer.parseInt(rs.getString(1));
			}
			return null;
		}
	}

	public static Long getFundsById(long id) throws SQLException{
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_FUNDS_BY_ID)){
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
			return null;
		}
	}

	public static Map<String, Object> getDegenData(Long id) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_DEGEN_DATA)){
			Map<String,Object> result = new HashMap<>();
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result.put("FUNDS", rs.getLong(1));
				result.put("FARMATT", rs.getInt(2));
				result.put("DEGENID", rs.getInt(3));
				
			}
			return result;
		}
	}

	public static void updateFunds(Long newFunds, int id) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_DEGEN_FUNDS)){
			stmt.setLong(1, newFunds);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}
	
	public static int getDegenId(Long discordId) throws SQLException{
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_DEGEN_ID)){
			stmt.setLong(1, discordId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		}
	}

	public static void updateFarmAtt(int newAttempts, int id, int timesFarmed) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_DEGEN_FARMATT)){
			stmt.setLong(1, newAttempts);
			stmt.setInt(2,timesFarmed);
			stmt.setInt(3, id);
			stmt.executeUpdate();
		}
	}
	
	public static void addFarmAtt(int farmToAdd, int... ids) throws SQLException{
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.ADD_FARMATT)){
			for(int id : ids) {
				stmt.setInt(1, farmToAdd);
				stmt.setLong(2, id);
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}

	public static List<Map<String, Object>> getAllDegens() throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_ALL_DEGEN_DATA)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Map<String,Object> record = new HashMap<>();
				record.put("WINS", rs.getInt(1));
				record.put("LOSSES", rs.getInt(2));
				record.put("TIMESFARMED", rs.getInt(3));
				record.put("FUNDS", rs.getLong(4));
				record.put("FARMATT", rs.getInt(5));
				record.put("NAME", rs.getString(6));
				record.put("PEAK", rs.getLong(7));
				
				result.add(record);
			}
			return result;
		}
	}

	public static void updateRate(Long discId, boolean win) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_DEGEN_RATE)){
			stmt.setInt(1, win ? 1 : 0);
			stmt.setInt(2, win ? 0 : 1);
			stmt.setLong(3, discId);
			stmt.executeUpdate();
		}
	}

	public static List<ItemBean> getShopContents() throws SQLException {
		List<ItemBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_FOR_SALE_SHOP)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ItemBean item = new ItemBean();
				item.setItemID(rs.getLong(1));
				item.setItemName(rs.getString(2));
				item.setPrice(rs.getLong(3));
				item.setOwnerName(rs.getString(4));
				item.setValue(rs.getLong(5));
				result.add(item);
			}
		}
		return result;
	}

	public static ItemBean getShopItemById(Long itemID) throws SQLException {
		ItemBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_ITEM_BY_ID)){
			stmt.setLong(1, itemID);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ItemBean();
				result.setItemID(rs.getLong(1));
				result.setItemName(rs.getString(2));
				result.setDegenID(rs.getInt(3));
				result.setPrice(rs.getLong(4));
				result.setForSale(rs.getBoolean(5));
				result.setOwnerDiscordId(rs.getLong(6));
				result.setValue(rs.getLong(7));
			}
		}
		return result;
	}

	public static void updateShopNewOwner(Long itemID,int buyerDegenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SHOP_NEW_OWNER)){
			stmt.setInt(1, buyerDegenId);
			stmt.setLong(2, itemID);
			stmt.executeUpdate();
		}
	}
	
	public static void updateFundsSum(Long fundsToAdd, int id) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SUM_DEGEN_FUNDS)){
			stmt.setLong(1, fundsToAdd);
			stmt.setInt(2, id);
			stmt.executeUpdate();
		}
	}

	public static List<ItemBean> getDegenInventory(long discId) throws SQLException{
		List<ItemBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_ITEMS_BY_DISCID)){
			stmt.setLong(1, discId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ItemBean item = new ItemBean();
				item.setItemID(rs.getLong(1));
				item.setItemName(rs.getString(2));
				item.setPrice(rs.getLong(3));
				item.setOwnerName(rs.getString(4));
				item.setForSale(rs.getBoolean(5));
				item.setValue(rs.getLong(6));
				item.setImageName(rs.getString(7));
				item.setService(false);
				result.add(item);
			}
		}
		return result;
	}
	
	public static void updateItemForSale(Long itemID)throws SQLException{
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_ITEM_FOR_SALE)){
			stmt.setLong(1, itemID);
			stmt.executeUpdate();
		}
	}

	public static void createItem(String itemName, long price, long ownerId, boolean forSale) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.CREATE_ITEM_FOR_SALE)){
			stmt.setString(1, itemName);
			stmt.setLong(2, price);
			stmt.setLong(3, price);
			stmt.setLong(4, ownerId);
			stmt.setBoolean(5, forSale);
			stmt.executeUpdate();
		}
	}
	
	public static void putItemForSaleSetPrice(Long itemID,Long price)throws SQLException{
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_ITEM_FOR_SALE_SET_PRICE)){
			stmt.setLong(1, price);
			stmt.setLong(2, itemID);
			stmt.executeUpdate();
		}
	}

	public static Long getInventoryWorth(long discId)throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_INV_SUM)){
			stmt.setLong(1, discId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
			return 0L;
		}
	}
	public static void createService(String serviceName, int farms, double rateHour, int interval, int degenID,boolean predetermined, int level) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.CREATE_SERVICE)){
			stmt.setInt(1, farms);
			stmt.setDouble(2, rateHour);
			stmt.setString(3, serviceName);
			stmt.setInt(4, interval);
			stmt.setInt(5, degenID);
			stmt.setBoolean(6, predetermined);
			stmt.setInt(7, level);
			stmt.executeUpdate();
		}
	}

	public static ServiceBean getServiceById(long itemID) throws SQLException {
		ServiceBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_SERVICE_BY_ID)){
			stmt.setLong(1, itemID);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ServiceBean();
				result.setServiceId(rs.getInt(1));
				result.setItemID(Long.valueOf(result.getServiceId()));
				result.setFarms(rs.getInt(2));
				result.setLength(rs.getDouble(3));
				result.setName(rs.getString(4));
				result.setInterval(rs.getInt(5));
				result.setForSale(rs.getBoolean(6));
				result.setActive(rs.getBoolean(7));
				result.setDegenID(rs.getInt(8));
				result.setOwnerDiscordId(rs.getLong(9));
				result.setPrice(rs.getLong(10));
				result.setOwnerName(rs.getString(11));
				result.setRequiredLevel(rs.getInt(12));
			}
		}
		return result;
	}

	public static void updateSeviceNewOwner(int serviceId, int buyerDegenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SERVICE_NEW_OWNER)){
			stmt.setInt(1, buyerDegenId);
			stmt.setLong(2, serviceId);
			stmt.executeUpdate();
		}
	}

	public static void putServiceForSaleSetPrice(int serviceId, Long price) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SERVICE_FOR_SALE_SET_PRICE)){
			stmt.setLong(1, price);
			stmt.setLong(2, serviceId);
			stmt.executeUpdate();
		}
	}

	public static List<ItemBean> getServicesContents() throws SQLException {
		List<ItemBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_FOR_SALE_SERVICES)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ItemBean item = new ItemBean();
				item.setItemID(rs.getLong(1));
				item.setItemName(rs.getString(2));
				item.setPrice(rs.getLong(3));
				item.setOwnerName(rs.getString(4));
				item.setValue(rs.getLong(5));
				result.add(item);
			}
		}
		return result;
	}

	public static List<ServiceBean> getDegenServices(long discId) throws SQLException {
		List<ServiceBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_SERVICES_BY_DISCID)){
			stmt.setLong(1, discId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ServiceBean item = new ServiceBean();
				item.setItemID(rs.getLong(1));
				item.setItemName(rs.getString(2));
				item.setPrice(rs.getLong(3));
				item.setOwnerName(rs.getString(4));
				item.setForSale(rs.getBoolean(5));
				item.setValue(rs.getLong(6));
				item.setActiveStr(rs.getString(7));
				item.setShortName(rs.getString(8));
				item.setImageName(item.getActiveStr().equalsIgnoreCase("y") ? "/service.png" : "/inactiveservice.png");
				item.setService(true);
				item.setRequiredLevel(rs.getInt(9));
				result.add(item);
			}
		}
		return result;
	}

	public static List<ItemBean> getPredServices() throws SQLException {
		List<ItemBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_PRED_SERVICES)){
			stmt.setLong(1, RehabBot.getBotId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ItemBean item = new ItemBean();
				item.setItemID(rs.getLong(1));
				item.setItemName(rs.getString(2));
				item.setPrice(rs.getLong(3));
				item.setOwnerName(rs.getString(4));
				item.setForSale(rs.getBoolean(5));
				item.setValue(rs.getLong(6));
				item.setActiveStr(rs.getString(7));
				item.setRequiredLevel(rs.getInt(8));
				item.setService(true);
				result.add(item);
			}
		}
		return result;
	}
	
	public static List<ServiceBean> getRunningServices() throws SQLException {
		List<ServiceBean> result = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_ACTIVE_SERVICES)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ServiceBean record = new ServiceBean();
				record.setServiceId(rs.getInt(1));
				record.setItemID(Long.valueOf(record.getServiceId()));
				record.setFarms(rs.getInt(2));
				record.setLength(rs.getDouble(3));
				record.setName(rs.getString(4));
				record.setInterval(rs.getInt(5));
				record.setForSale(rs.getBoolean(6));
				record.setActive(rs.getBoolean(7));
				record.setDegenID(rs.getInt(8));
				record.setOwnerDiscordId(rs.getLong(9));
				record.setPrice(rs.getLong(10));
				record.setOwnerName(rs.getString(11));
				result.add(record);
			}
		}
		return result;
	}

	public static void deleteService(int serviceID) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.DELETE_SERVICE_BY_ID)){
			stmt.setInt(1, serviceID);
			stmt.executeUpdate();
		}
	}

	public static void updateRunningService(int serviceId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SERVICE_ACTIVE_BY_ID)){
			stmt.setInt(1, serviceId);
			stmt.executeUpdate();
		}
	}

	public static ServiceBean getPredeterminedService(int serviceId) throws SQLException {
		ServiceBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_PRED_SERVICE_BY_ID)){
			stmt.setLong(1, serviceId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ServiceBean();
				result.setServiceId(rs.getInt(1));
				result.setItemID(Long.valueOf(result.getServiceId()));
				result.setFarms(rs.getInt(2));
				result.setLength(rs.getDouble(3));
				result.setName(rs.getString(4));
				result.setInterval(rs.getInt(5));
				result.setForSale(rs.getBoolean(6));
				result.setActive(rs.getBoolean(7));
				result.setDegenID(rs.getInt(8));
				result.setOwnerDiscordId(rs.getLong(9));
				result.setPrice(rs.getLong(10));
				result.setOwnerName(rs.getString(11));
				result.setRequiredLevel(rs.getInt(12));
			}
		}
		return result;
	}

	public static int countDegenPredService(long idLong) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.COUNT_DEGEN_PRED_SERVICE)){
			stmt.setLong(1, idLong);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 1;
		}
	}

	public static ServiceBean getDegenPredeterminedService(int degenId) throws SQLException {
		ServiceBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_PRED_SERVICE_BY_DEGENID)){
			stmt.setLong(1, degenId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ServiceBean();
				result.setServiceId(rs.getInt(1));
				result.setItemID(Long.valueOf(result.getServiceId()));
				result.setFarms(rs.getInt(2));
				result.setLength(rs.getDouble(3));
				result.setName(rs.getString(4));
				result.setInterval(rs.getInt(5));
				result.setForSale(rs.getBoolean(6));
				result.setActive(rs.getBoolean(7));
				result.setDegenID(rs.getInt(8));
				result.setOwnerDiscordId(rs.getLong(9));
				result.setPrice(rs.getLong(10));
				result.setOwnerName(rs.getString(11));
			}
		}
		return result;
	}

	public static void deleteDegen(int degenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.DELETE_DEGEN)){
			stmt.setInt(1, degenId);
			stmt.executeUpdate();
		}
	}

	public static List<String> getItemsFromType(int itemType) throws SQLException {
		List<String> items = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_ITEM_NAMES_BY_TYPE)){
			stmt.setLong(1, itemType);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				items.add(rs.getString(1));
			}
		}
		return items;
	}

	public static void insertHighLowState(Map<Long, PlayerWinsBean> playersWins) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.INSERT_HIGHLOWSTATE)){
			for(Entry<Long,PlayerWinsBean> entry : playersWins.entrySet()) {
				stmt.setInt(1, getDegenId(entry.getKey()));
				stmt.setInt(2, entry.getValue().getCurrentFarms());
				stmt.setDouble(3, entry.getValue().getRate());
				stmt.addBatch();
			}
			stmt.executeBatch();
		}
	}

	public static Map<Long, PlayerWinsBean> selectHighLowState() throws SQLException {
		Map<Long, PlayerWinsBean> state = new HashMap<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_HIGHLOWSTATE)){
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				state.put(rs.getLong(1), new PlayerWinsBean(rs.getInt(2),rs.getFloat(3)));
			}
		}
		return state;
	}

	public static void clearHighLowState() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.DELETE_HIGHLOWSTATE)){
			stmt.executeUpdate();
		}
	}

	public static void updateService(int serviceID, Double timeLeft) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_SERVICE_LENGTH)){
			stmt.setDouble(1, timeLeft);
			stmt.setInt(2, serviceID);
			stmt.executeUpdate();
		}
	}

	public static ServiceBean selectBiddableService() throws SQLException {
		ServiceBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_BIDDABLE_SERVICE)){
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ServiceBean();
				result.setServiceId(rs.getInt(1));
				result.setItemID(Long.valueOf(result.getServiceId()));
				result.setFarms(rs.getInt(2));
				result.setLength(rs.getDouble(3));
				result.setName(rs.getString(4));
				result.setInterval(rs.getInt(5));
				result.setForSale(rs.getBoolean(6));
				result.setActive(rs.getBoolean(7));
				result.setDegenID(rs.getInt(8));
				result.setOwnerDiscordId(rs.getLong(9));
				result.setPrice(rs.getLong(10));
				result.setOwnerName(rs.getString(11));
			}
		}
		return result;
	}

	public static int getBiddableServiceId() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.GET_BIDDABLE_SERVICE_ID)){
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public static void updateBiddable(BiddableServiceBean biddableService) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_BIDDABLE_SERVICE)){
			stmt.setInt(1, biddableService.getFarms());
			stmt.setDouble(2,biddableService.getLengthHours());
			stmt.setString(3, biddableService.getServiceName());
			stmt.setInt(4, biddableService.getIntervalMinutes());
			stmt.setInt(5, getDegenId(biddableService.getWinnerID()));
			stmt.setLong(6, biddableService.getBid());
			stmt.executeUpdate();
		}
	}

	public static void updateBiddableActive() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.UPDATE_BIDDABLE_SERVICE_ACTIVE)){
			stmt.executeUpdate();
		}
	}

	public static int getFarmAtts(long idLong) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_FARMS)){
			stmt.setLong(1, idLong);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public static void createPredService(String name, int farms, double durationHours, int rate, int degenId, Long price, int level) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.INSERT_PREDETERMINED_SERVICE)){
			stmt.setInt(1, farms);
			stmt.setDouble(2,durationHours);
			stmt.setString(3, name);
			stmt.setInt(4, rate);
			stmt.setInt(5, degenId);
			stmt.setBoolean(6, true);
			stmt.setLong(7, price);
			stmt.setInt(8, level);
			stmt.executeUpdate();
		}
	}

	public static int getActiveServicesCount() throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.COUNT_ACTIVE_SERVICES)){
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public static int getUserActiveServicesCount(int degenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.COUNT_ACTIVE_SERVICES_BY_DEGEN)){
			stmt.setInt(1, degenId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public static boolean deactivateDegen(int degenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.DEACTIVATE_DEGEN)){
			stmt.setInt(1, degenId);
			return stmt.executeUpdate()>=1;
		}
	}

	public static List<Integer> getDegenActiveServicesId(int degenId) throws SQLException {
		List<Integer> serviceIds = new ArrayList<>();
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_DEGEN_ACTIVE_SERVICEIDS)){
			stmt.setInt(1, degenId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				serviceIds.add(rs.getInt(1));
			}
		}
		return serviceIds;
	}

	public static void insertBiddableService(BiddableServiceBean biddableService) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.INSERT_BIDDABLE_SERVICE)){
			stmt.setInt(1, biddableService.getFarms());
			stmt.setDouble(2,biddableService.getLengthHours());
			stmt.setString(3, biddableService.getServiceName());
			stmt.setInt(4, biddableService.getIntervalMinutes());
			stmt.setInt(5, DatabaseDegens.getDegenId(biddableService.getWinnerID()));
			stmt.setLong(6, biddableService.getBid());
			stmt.executeUpdate();
		}
	}

	public static ServiceBean selectDegenPredService(int degenId) throws SQLException {
		ServiceBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_DEGEN_PRED_SERVICE)){
			stmt.setInt(1, degenId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new ServiceBean();
				result.setServiceId(rs.getInt(1));
				result.setItemID(Long.valueOf(result.getServiceId()));
				result.setFarms(rs.getInt(2));
				result.setLength(rs.getDouble(3));
				result.setName(rs.getString(4));
				result.setInterval(rs.getInt(5));
				result.setForSale(rs.getBoolean(6));
				result.setActive(rs.getBoolean(7));
				result.setDegenID(rs.getInt(8));
				result.setOwnerDiscordId(rs.getLong(9));
				result.setPrice(rs.getLong(10));
				result.setOwnerName(rs.getString(11));
			}
		}
		return result;
	}

	public static boolean existsInactive(long id) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.COUNT_INACTIVE_DISC_ID)){
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			return rs.getInt(1) == 1;
		}
	}

	public static boolean reactivateDegen(int degenId) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.REACTIVATE_DEGEN)){
			stmt.setInt(1, degenId);
			return stmt.executeUpdate()>=1;
		}
	}

	public static DegenBean getDegen(long id) throws SQLException {
		DegenBean result = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_DEGEN)){
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				result = new DegenBean();
				result.setDegenId(rs.getInt(1));
				result.setIronman(rs.getBoolean(2));
				result.setActive(rs.getBoolean(3));
			}
		}
		return result;
	}

	public static void updateLevel(long idLong) throws SQLException {
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.RAISE_LEVEL)){
			stmt.setLong(1, idLong);
			stmt.executeUpdate();
		}
	}

	public static LevelBean getDegenNextLevel(long discId) throws SQLException {
		LevelBean level = null;
		try(Connection con = DegensDataSource.getConnection();
				PreparedStatement stmt = con.prepareStatement(DBQueriesConstants.SELECT_LEVEL)){
			stmt.setLong(1, discId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				level = new LevelBean();
				level.setCost(rs.getLong(1));
				level.setFreeService(rs.getBoolean(2));
				level.setLevel(rs.getInt(3));
			}
		}
		return level;
	}
}
