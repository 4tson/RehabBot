package mx.fortson.rehab.constants;

public class DBQueriesConstants {

	public static final String COUNT_BY_DISC_ID = "SELECT COUNT (1) FROM DEGENS WHERE DISCORDID = ?";
	
	public static final String SELECT_PROPERTY = "SELECT VALUE FROM PROPERTIES WHERE KEY = ?";
	
	public static final String INSERT_NEW_DEGEN = "INSERT INTO DEGENS (DISCORDID,NAME,IRONMAN) VALUES (?,?,?)";
	
	public static final String SELECT_FUNDS_BY_ID = "SELECT FUNDS.FUNDS FROM DEGENS, FUNDS WHERE DEGENS.DEGENID = FUNDS.DEGENID AND DEGENS.DISCORDID = ?";
	
	public static final String SELECT_DEGEN_DATA = "SELECT FUNDS.FUNDS, FARMATTEMPTS.FARMATTEMPTS, DEGENS.DEGENID FROM DEGENS, FUNDS, FARMATTEMPTS WHERE "
			+ "DEGENS.DEGENID = FARMATTEMPTS.DEGENID AND DEGENS.DEGENID = FUNDS.DEGENID AND DEGENS.DISCORDID = ?";
	
	public static final String UPDATE_DEGEN_FUNDS = "UPDATE FUNDS SET FUNDS = ? WHERE DEGENID = ?";
	
	public static final String UPDATE_SUM_DEGEN_FUNDS = "UPDATE FUNDS SET FUNDS = FUNDS + ? WHERE DEGENID = ?"; 
	
	public static final String UPDATE_DEGEN_FARMATT = "UPDATE FARMATTEMPTS SET FARMATTEMPTS = ?, TIMESFARMED = TIMESFARMED + ? WHERE DEGENID = ?";
	
	public static final String ADD_FARMATT = "UPDATE FARMATTEMPTS SET FARMATTEMPTS = FARMATTEMPTS + ? WHERE DEGENID = ?";
	
	public static final String SELECT_ALL_DEGEN_DATA = "SELECT DEGENS.WINS, DEGENS.LOSSES, FARMATTEMPTS.TIMESFARMED, FUNDS.FUNDS, FARMATTEMPTS.FARMATTEMPTS, DEGENS.NAME, FUNDS.PEAK FROM DEGENS, FUNDS, FARMATTEMPTS WHERE "
			+ "DEGENS.DEGENID = FARMATTEMPTS.DEGENID AND DEGENS.DEGENID = FUNDS.DEGENID";
	
	public static final String SELECT_DEGEN_ID = "SELECT DEGENID FROM DEGENS WHERE DISCORDID = ?";
	
	public static final String UPDATE_DEGEN_RATE = "UPDATE DEGENS SET WINS = WINS + ?, LOSSES = LOSSES + ? WHERE DISCORDID = ?";
	
	public static final String SELECT_FOR_SALE_SHOP = "SELECT S.ITEMID, S.ITEMNAME, S.PRICE, D.NAME, S.VALUE FROM SHOP S, DEGENS D WHERE S.FORSALE = TRUE AND S.DEGENID = D.DEGENID";
	
	public static final String SELECT_FOR_SALE_SERVICES = "SELECT S.SERVICEID, S.FARMS + 'F/' + S.RATEMINUTES + 'Mins For ' + S.LENGTHHOURS + 'Hrs.' , S.PRICE, D.NAME, 73 FROM SERVICES S, DEGENS D WHERE S.FORSALE = TRUE AND S.DEGENID = D.DEGENID AND S.PREDETERMINED IS FALSE";
	
	public static final String SELECT_ITEM_BY_ID = "SELECT S.ITEMID, S.ITEMNAME, S.DEGENID, S.PRICE, S.FORSALE, D.DISCORDID, S.VALUE FROM SHOP S, DEGENS D WHERE ITEMID = ? AND D.DEGENID = S.DEGENID";
	
	public static final String UPDATE_SHOP_NEW_OWNER = "UPDATE SHOP SET DEGENID = ?, FORSALE = FALSE WHERE ITEMID = ?";
	
	public static final String UPDATE_SERVICE_NEW_OWNER = "UPDATE SERVICES SET DEGENID = ?, FORSALE = FALSE WHERE SERVICEID = ?";
	
	public static final String SELECT_ITEMS_BY_DISCID = "SELECT S.ITEMID, S.ITEMNAME, S.PRICE, D.NAME, S.FORSALE, S.VALUE FROM SHOP S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String SELECT_SERVICES_BY_DISCID = "SELECT S.SERVICEID, S.FARMS + 'F/' + S.RATEMINUTES + 'Mins For ' + S.LENGTHHOURS + 'Hrs.', S.PRICE, D.NAME, S.FORSALE, 73, CASE WHEN S.ACTIVE THEN 'Y' ELSE 'N' END FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String UPDATE_ITEM_FOR_SALE = "UPDATE SHOP SET FORSALE = NOT FORSALE WHERE ITEMID = ?";
	
	public static final String UPDATE_ITEM_FOR_SALE_SET_PRICE = "UPDATE SHOP SET FORSALE = NOT FORSALE, PRICE = ? WHERE ITEMID = ?";
	
	public static final String UPDATE_SERVICE_FOR_SALE_SET_PRICE = "UPDATE SERVICES SET FORSALE = NOT FORSALE, PRICE = ? WHERE SERVICEID = ?";
	
	public static final String CREATE_ITEM_FOR_SALE = "INSERT INTO SHOP(ITEMNAME, PRICE,VALUE, DEGENID, FORSALE) VALUES (?,?,?,?,?)";
	
	public static final String SELECT_INV_SUM = "SELECT SUM(S.VALUE) FROM SHOP S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ?";
	
	public static final String SELECT_SERVICE_STATUS = "SELECT RUNNING, EXPIRYDATE FROM SERVICESTATE";
	
	public static final String UPDATE_SERVICESTATUS = "UPDATE SERVICESTATE SET RUNNING = ?, EXPIRYDATE = ?";
	
	public static final String CREATE_SERVICE = "INSERT INTO SERVICES(FARMS, LENGTHHOURS, NAME, RATEMINUTES, DEGENID, PREDETERMINED) VALUES (?,?,?,?,?,?)";
	
	public static final String SELECT_SERVICE_BY_ID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.SERVICEID = ? AND D.DEGENID = S.DEGENID";
	
	public static final String SELECT_ACTIVE_SERVICES = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.ACTIVE = TRUE AND D.DEGENID = S.DEGENID";
	
	public static final String DELETE_SERVICE_BY_ID = "DELETE FROM SERVICES WHERE SERVICEID = ?";
	
	public static final String UPDATE_SERVICE_ACTIVE_BY_ID = "UPDATE SERVICES SET ACTIVE = NOT ACTIVE WHERE SERVICEID = ?";
	
	public static final String SELECT_PRED_SERVICE_BY_ID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.SERVICEID = ? AND D.DEGENID = S.DEGENID AND S.PREDETERMINED IS TRUE";
	
	public static final String COUNT_DEGEN_PRED_SERVICE = "SELECT COUNT(1) FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND D.DISCORDID = ? AND S.PREDETERMINED IS TRUE";
	
	public static final String SELECT_PRED_SERVICE_BY_DEGENID = "SELECT S.SERVICEID, S.FARMS, S.LENGTHHOURS, S.NAME, S.RATEMINUTES, S.FORSALE, S.ACTIVE, S.DEGENID, D.DISCORDID, S.PRICE, D.NAME FROM SERVICES S, DEGENS D WHERE S.DEGENID = ? AND D.DEGENID = S.DEGENID AND S.PREDETERMINED IS TRUE";
	
	public static final String SELECT_PRED_SERVICES = "SELECT S.SERVICEID, S.FARMS + 'F/' + S.RATEMINUTES + 'Mins For ' + S.LENGTHHOURS + 'Hrs.', S.PRICE, D.NAME, S.FORSALE, S.PRICE, CASE WHEN S.ACTIVE THEN 'Y' ELSE 'N' END FROM SERVICES S, DEGENS D WHERE S.DEGENID = D.DEGENID AND S.PREDETERMINED IS TRUE AND ACTIVE IS FALSE";
	
	public static final String DELETE_DEGEN = "DELETE FROM DEGENS WHERE DEGENID = ?";
}
