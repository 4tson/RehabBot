package mx.fortson.rehab.constants;

public class DBQueriesConstants {

	public static final String COUNT_BY_DISC_ID = "SELECT COUNT(1) FROM DEGENS WHERE DISCORDID = ? AND ACTIVE = TRUE";
	
	public static final String COUNT_INACTIVE_DISC_ID = "SELECT COUNT(1) FROM DEGENS WHERE DISCORDID = ? AND ACTIVE = FALSE";
	
	public static final String SELECT_PROPERTY = "SELECT PROP_VALUE FROM PROPERTIES WHERE PROP_KEY = ?";
	
	public static final String INSERT_NEW_DEGEN = "INSERT INTO DEGENS (DISCORDID,NAME,IRONMAN) VALUES (?,?,?) as new on duplicate key update active = true, ironman = new.ironman";
	
	public static final String SELECT_FUNDS_BY_ID = "SELECT FUNDS.FUNDS FROM DEGENS, FUNDS WHERE DEGENS.DEGENID = FUNDS.DEGENID AND DEGENS.DISCORDID = ?";
	
	public static final String SELECT_DEGEN_DATA = "SELECT FUNDS.FUNDS, FARMS.FARMATTEMPTS, DEGENS.DEGENID FROM DEGENS, FUNDS, FARMS WHERE "
			+ "DEGENS.DEGENID = FARMS.DEGENID AND DEGENS.DEGENID = FUNDS.DEGENID AND DEGENS.DISCORDID = ?";
	
	public static final String UPDATE_DEGEN_FUNDS = "UPDATE FUNDS SET FUNDS = ? WHERE DEGENID = ?";
	
	public static final String UPDATE_SUM_DEGEN_FUNDS = "UPDATE FUNDS SET FUNDS = FUNDS + ? WHERE DEGENID = ?"; 
	
	public static final String UPDATE_DEGEN_FARMATT = "UPDATE FARMS SET FARMATTEMPTS = ?, TIMESFARMED = TIMESFARMED + ? WHERE DEGENID = ?";
	
	public static final String ADD_FARMATT = "UPDATE FARMS SET FARMATTEMPTS = FARMATTEMPTS + ? WHERE DEGENID = ?";
	
	public static final String SELECT_ALL_DEGEN_DATA = "SELECT DEGENS.WINS, DEGENS.LOSSES, FARMS.TIMESFARMED, FUNDS.FUNDS, FARMS.FARMATTEMPTS, DEGENS.NAME, FUNDS.PEAK, DEGENS.IRONMAN, DEGENS.LEVEL FROM DEGENS, FUNDS, FARMS WHERE "
			+ "DEGENS.DEGENID = FARMS.DEGENID AND DEGENS.DEGENID = FUNDS.DEGENID AND DEGENS.ACTIVE = TRUE";
	
	public static final String SELECT_DEGEN_ID = "SELECT DEGENID FROM DEGENS WHERE DISCORDID = ?";
	
	public static final String UPDATE_DEGEN_RATE = "UPDATE DEGENS SET WINS = WINS + ?, LOSSES = LOSSES + ? WHERE DISCORDID = ?";
	
	public static final String SELECT_FOR_SALE_SHOP = "SELECT S.ITEMID, S.ITEMNAME, S.PRICE, D.NAME, S.VALUE FROM SHOP S, DEGENS D WHERE S.FORSALE = TRUE AND S.DEGENID = D.DEGENID";
	
	public static final String SELECT_FOR_SALE_SERVICES = "SELECT S.SERVICEID, concat(S.FARMS ,'F/' ,S.RATEMINUTES ,'Mins For ',S.LENGTHHOURS, 'Hrs.') , S.PRICE, D.NAME, 73 FROM SERVICES S, DEGENS D WHERE S.FORSALE = TRUE AND S.DEGENID = D.DEGENID AND S.PREDETERMINED IS FALSE";
	
	public static final String SELECT_ITEM_BY_ID = "SELECT S.ITEMID, S.ITEMNAME, S.DEGENID, S.PRICE, S.FORSALE, D.DISCORDID, S.VALUE FROM SHOP S, DEGENS D WHERE ITEMID = ? AND D.DEGENID = S.DEGENID";
	
	public static final String UPDATE_SHOP_NEW_OWNER = "UPDATE SHOP SET DEGENID = ?, FORSALE = FALSE WHERE ITEMID = ?";
	
	public static final String UPDATE_SERVICE_NEW_OWNER = "UPDATE SERVICES SET DEGENID = ?, FORSALE = FALSE WHERE SERVICEID = ?";
	
	public static final String SELECT_ITEMS_BY_DISCID = "SELECT S.ITEMID, S.ITEMNAME, S.PRICE, D.NAME, S.FORSALE, S.VALUE, I.IMAGENAME FROM SHOP S, DEGENS D, ITEMS I WHERE I.NAME = S.ITEMNAME AND S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String SELECT_SERVICES_BY_DISCID = "SELECT S.SERVICEID, concat(S.FARMS ,'F/' ,S.RATEMINUTES ,'Mins For ',S.LENGTHHOURS, 'Hrs.'), S.PRICE, D.NAME, S.FORSALE, 73, CASE WHEN S.ACTIVE THEN 'Y' ELSE 'N' END, concat(S.FARMS ,'F/' ,S.RATEMINUTES ,'M\n ',S.LENGTHHOURS, 'H'), S.LEVEL FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String UPDATE_ITEM_FOR_SALE = "UPDATE SHOP SET FORSALE = NOT FORSALE, PRICE = VALUE WHERE ITEMID = ?";
	
	public static final String UPDATE_ITEM_FOR_SALE_SET_PRICE = "UPDATE SHOP SET FORSALE = NOT FORSALE, PRICE = ? WHERE ITEMID = ?";
	
	public static final String UPDATE_SERVICE_FOR_SALE_SET_PRICE = "UPDATE SERVICES SET FORSALE = NOT FORSALE, PRICE = ? WHERE SERVICEID = ?";
	
	public static final String CREATE_ITEM_FOR_SALE = "INSERT INTO SHOP(ITEMNAME, PRICE,VALUE, DEGENID, FORSALE) VALUES (?,?,?,?,?)";
	
	public static final String SELECT_INV_SUM = "SELECT SUM(S.VALUE) FROM SHOP S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String SELECT_SERVICE_STATUS = "SELECT RUNNING, EXPIRYDATE FROM SERVICESTATE";
	
	public static final String UPDATE_SERVICESTATUS = "UPDATE SERVICESTATE SET RUNNING = ?, EXPIRYDATE = ?";
	
	public static final String CREATE_SERVICE = "INSERT INTO SERVICES(FARMS, LENGTHHOURS, NAME, RATEMINUTES, DEGENID, PREDETERMINED, LEVEL) VALUES (?,?,?,?,?,?,?)";
	
	public static final String SELECT_SERVICE_BY_ID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME, S.LEVEL FROM SERVICES S, DEGENS D WHERE S.SERVICEID = ? AND D.DEGENID = S.DEGENID";
	
	public static final String SELECT_ACTIVE_SERVICES = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.ACTIVE = TRUE AND D.DEGENID = S.DEGENID AND S.BIDDABLE = FALSE";
	
	public static final String DELETE_SERVICE_BY_ID = "DELETE FROM SERVICES WHERE SERVICEID = ?";
	
	public static final String UPDATE_SERVICE_ACTIVE_BY_ID = "UPDATE SERVICES SET ACTIVE = NOT ACTIVE WHERE SERVICEID = ?";
	
	public static final String SELECT_PRED_SERVICE_BY_ID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME, S.LEVEL FROM SERVICES S, DEGENS D WHERE S.SERVICEID = ? AND D.DEGENID = S.DEGENID AND S.PREDETERMINED IS TRUE";
	
	public static final String COUNT_DEGEN_PRED_SERVICE = "SELECT COUNT(1) FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ? AND S.PREDETERMINED IS TRUE";
	
	public static final String SELECT_PRED_SERVICE_BY_DEGENID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.DEGENID = ? AND D.DEGENID = S.DEGENID AND S.PREDETERMINED IS TRUE";
	
	public static final String SELECT_PRED_SERVICES = "SELECT S.SERVICEID, concat(S.FARMS ,'F/' ,S.RATEMINUTES ,'Mins For ',S.LENGTHHOURS, 'Hrs.'), S.PRICE, D.NAME, S.FORSALE, S.PRICE, CASE WHEN S.ACTIVE THEN 'Y' ELSE 'N' END, S.LEVEL FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND S.PREDETERMINED IS TRUE AND S.ACTIVE IS FALSE AND D.DISCORDID = ?";
	
	public static final String DELETE_DEGEN = "DELETE FROM DEGENS WHERE DISCORDID = ?";
	
	public static final String SELECT_ITEM_NAMES_BY_TYPE = "SELECT NAME FROM ITEMS WHERE TYPE = ?";
	
	public static final String INSERT_HIGHLOWSTATE = "INSERT INTO HIGHLOWSTATE VALUES (?,?,?)";
	
	public static final String SELECT_HIGHLOWSTATE = "SELECT D.DISCORDID, H.FARMS, H.RATE FROM DEGENS D, HIGHLOWSTATE H WHERE D.DEGENID = H.DEGENID";
	
	public static final String DELETE_HIGHLOWSTATE = "DELETE FROM HIGHLOWSTATE";
	
	public static final String UPDATE_SERVICE_LENGTH = "UPDATE SERVICES SET LENGTHHOURS = ? WHERE SERVICEID = ?";
	
	public static final String SELECT_BIDDABLE_SERVICE = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE D.DEGENID = S.DEGENID AND S.BIDDABLE = TRUE";
	
	public static final String GET_BIDDABLE_SERVICE_ID = "SELECT SERVICEID FROM SERVICES WHERE BIDDABLE = TRUE";
	
	public static final String UPDATE_BIDDABLE_SERVICE = "UPDATE SERVICES SET FARMS = ?, LENGTHHOURS = ?, NAME = ?, RATEMINUTES = ?, DEGENID = ?, PRICE = ? ,ACTIVE = TRUE WHERE BIDDABLE = TRUE";
	
	public static final String UPDATE_BIDDABLE_SERVICE_ACTIVE = "UPDATE SERVICES SET ACTIVE = FALSE WHERE BIDDABLE = TRUE";
	
	public static final String SELECT_FARMS = "SELECT FARMS.FARMATTEMPTS FROM DEGENS, FARMS WHERE DEGENS.DEGENID = FARMS.DEGENID AND DEGENS.DISCORDID = ?";
	
	public static final String INSERT_PREDETERMINED_SERVICE = "INSERT INTO SERVICES(FARMS, LENGTHHOURS, NAME, RATEMINUTES, DEGENID,PREDETERMINED, PRICE, LEVEL) VALUES (?,?,?,?,?,?,?,?)";
	
	public static final String COUNT_ACTIVE_SERVICES = "SELECT COUNT(1) FROM SERVICES WHERE ACTIVE = TRUE AND BIDDABLE = FALSE";
	
	public static final String COUNT_ACTIVE_SERVICES_BY_DEGEN = "SELECT COUNT(1) FROM SERVICES WHERE ACTIVE = TRUE AND BIDDABLE = FALSE AND DEGENID = ?";
	
	public static final String DEACTIVATE_DEGEN = "UPDATE DEGENS SET ACTIVE = FALSE WHERE DEGENID = ?";
	
	public static final String REACTIVATE_DEGEN = "UPDATE DEGENS SET ACTIVE = TRUE WHERE DEGENID = ?";
	
	public static final String SELECT_DEGEN_ACTIVE_SERVICEIDS = "SELECT SERVICEID FROM SERVICES WHERE DEGENID = ?";
	
	public static final String INSERT_BIDDABLE_SERVICE = "INSERT INTO SERVICES(FARMS, LENGTHHOURS, NAME, RATEMINUTES,DEGENID, ACTIVE, BIDDABLE, PRICE) VALUES (?,?,?,?,?,false,true,?)";
	
	public static final String SELECT_DEGEN_PRED_SERVICE = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE D.DEGENID = S.DEGENID AND S.PREDETERMINED = TRUE AND D.DEGENID = ?";
	
	public static final String SELECT_DEGEN = "SELECT DEGENID, IRONMAN, ACTIVE FROM DEGENS WHERE DISCORDID=?";
	
	public static final String RAISE_LEVEL = "UPDATE DEGENS SET LEVEL = LEVEL + 1 WHERE DISCORDID = ?";
	
	public static final String SELECT_LEVEL = "SELECT COST, SERVICEREWARD, DEGENS.LEVEL FROM LEVELS, DEGENS WHERE LEVELS.LEVEL = DEGENS.LEVEL AND DEGENS.DISCORDID = ?";
	
	public static final String SELECT_MULTIPLIER = "select ifnull(sum(d.multiplier),0) + \r\n"
			+ "(select farmmultiplier from levels, degens where levels.level = (select level from degens where degenid = ?) and degens.degenid = ?) \r\n"
			+ "from (select t.multiplier from types t, items i, shop s where s.ITEMNAME = i.NAME and i.type = t.TYPEID and s.DEGENID = ? and S.forsale = false group by i.name) d";
}
