package mx.fortson.rehab.constants;

public class DBQueriesConstants {

	public static final String COUNT_BY_DISC_ID = "select count(1) from degens where discordid = ? and active = true";
	
	public static final String COUNT_INACTIVE_DISC_ID = "select count(1) from degens where discordid = ? and active = false";
	
	public static final String SELECT_PROPERTY = "select prop_value from properties where prop_key = ?";
	
	public static final String INSERT_NEW_DEGEN = "insert into degens (discordid,name,ironman) values (?,?,?) as new on duplicate key update active = true, ironman = new.ironman";
	
	public static final String SELECT_FUNDS_BY_ID = "select funds.funds from degens, funds where degens.degenid = funds.degenid and degens.discordid = ?";
	
	public static final String SELECT_DEGEN_DATA = "select funds.funds, farms.farmattempts, degens.degenid from degens, funds, farms where "
			+ "degens.degenid = farms.degenid and degens.degenid = funds.degenid and degens.discordid = ?";
	
	public static final String UPDATE_DEGEN_FUNDS = "update funds set funds = ? where degenid = ?";
	
	public static final String UPDATE_SUM_DEGEN_FUNDS = "update funds set funds = funds + ? where degenid = ?"; 
	
	public static final String UPDATE_DEGEN_FARMATT = "update farms set farmattempts = ?, timesfarmed = timesfarmed + ? where degenid = ?";
	
	public static final String ADD_FARMATT = "update farms set farmattempts = farmattempts + ? where degenid = ?";
	
	public static final String SELECT_ALL_DEGEN_DATA = "select degens.wins, degens.losses, farms.timesfarmed, funds.funds, farms.farmattempts, degens.name, funds.peak, degens.ironman, degens.level from degens, funds, farms where "
			+ "degens.degenid = farms.degenid and degens.degenid = funds.degenid and degens.active = true";
	
	public static final String SELECT_DEGEN_ID = "select degenid from degens where discordid = ?";
	
	public static final String UPDATE_DEGEN_RATE = "update degens set wins = wins + ?, losses = losses + ? where discordid = ?";
	
	public static final String SELECT_FOR_SALE_SHOP = "select s.itemid, s.itemname, s.price, d.name, s.value from shop s, degens d where s.forsale = true and s.degenid = d.degenid";
	
	public static final String SELECT_FOR_SALE_SERVICES = "select s.serviceid, concat(s.farms ,'f/' ,s.rateminutes ,'mins for ',s.lengthhours, 'hrs.') , s.price, d.name, 73 from services s, degens d where s.forsale = true and s.degenid = d.degenid and s.type <> 2";
	
	public static final String SELECT_ITEM_BY_ID = "select s.itemid, s.itemname, s.degenid, s.price, s.forsale, d.discordid, s.value from shop s, degens d where itemid = ? and d.degenid = s.degenid";
	
	public static final String UPDATE_SHOP_NEW_OWNER = "update shop set degenid = ?, forsale = false where itemid = ?";
	
	public static final String UPDATE_SERVICE_NEW_OWNER = "update services set degenid = ?, forsale = false where serviceid = ?";
	
	public static final String SELECT_ITEMS_BY_DISCID = "select s.itemid, s.itemname, s.price, d.name, s.forsale, s.value, i.imagename from shop s, degens d, items i where i.name = s.itemname and s.degenid = d.degenid and d.discordid = ? order by i.type desc";
	
	public static final String SELECT_SERVICES_BY_DISCID = "select s.serviceid, concat(s.farms ,'f/' ,s.rateminutes ,'mins for ',s.lengthhours, 'hrs.'), s.price, d.name, s.forsale, 73, case when s.active then 'y' else 'n' end, concat(s.farms ,'f/' ,s.rateminutes ,'m\n ',s.lengthhours, 'h'), s.level from services s, degens d where s.degenid = d.degenid and d.discordid = ? order by s.serviceid asc";
	
	public static final String UPDATE_ITEM_FOR_SALE = "update shop set forsale = not forsale, price = value where itemid = ?";
	
	public static final String UPDATE_ITEM_FOR_SALE_SET_PRICE = "update shop set forsale = not forsale, price = ? where itemid = ?";
	
	public static final String UPDATE_SERVICE_FOR_SALE_SET_PRICE = "update services set forsale = not forsale, price = ? where serviceid = ?";
	
	public static final String CREATE_ITEM_FOR_SALE = "insert into shop(itemname, price,value, degenid, forsale) values (?,?,?,?,?)";
	
	public static final String SELECT_INV_SUM = "select sum(s.value) from shop s, degens d where s.degenid = d.degenid and d.discordid = ?";
	
	public static final String SELECT_SERVICE_STATUS = "select running, expirydate from servicestate";
	
	public static final String UPDATE_SERVICESTATUS = "update servicestate set running = ?, expirydate = ?";
	
	public static final String CREATE_SERVICE = "insert into services(farms, lengthhours, name, rateminutes, degenid, type, level) values (?,?,?,?,?,?,?)";
	
	public static final String SELECT_SERVICE_BY_ID = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name, s.level from services s, degens d where s.serviceid = ? and d.degenid = s.degenid";
	
	public static final String SELECT_ACTIVE_SERVICES = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name, s.type from services s, degens d where s.active = true and d.degenid = s.degenid and s.type not in (1,4)";
	
	public static final String DELETE_SERVICE_BY_ID = "delete from services where serviceid = ?";
	
	public static final String UPDATE_SERVICE_ACTIVE_BY_ID = "update services set active = not active where serviceid = ?";
	
	public static final String SELECT_PRED_SERVICE_BY_ID = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name, s.level from services s, degens d where s.serviceid = ? and d.degenid = s.degenid and s.type = 2";
	
	public static final String COUNT_DEGEN_PRED_SERVICE = "select count(1) from services s, degens d where s.degenid = d.degenid and d.discordid = ? and s.type = 2";
	
	public static final String SELECT_PRED_SERVICE_BY_DEGENID = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name from services s, degens d where s.degenid = ? and d.degenid = s.degenid and s.type = 2";
	
	public static final String SELECT_PRED_SERVICES = "select s.serviceid, concat(s.farms ,'f/' ,s.rateminutes ,'mins for ',s.lengthhours, 'hrs.'), s.price, d.name, s.forsale, s.price, case when s.active then 'y' else 'n' end, s.level from services s, degens d where s.degenid = d.degenid and s.type = 2 and s.active is false and d.discordid = ?";
	
	public static final String DELETE_DEGEN = "delete from degens where discordid = ?";
	
	public static final String SELECT_ITEM_NAMES_BY_TYPE = "select name from items where type = ?";
	
	public static final String INSERT_HIGHLOWSTATE = "insert into highlowstate values (?,?,?)";
	
	public static final String SELECT_HIGHLOWSTATE = "select d.discordid, h.farms, h.rate from degens d, highlowstate h where d.degenid = h.degenid";
	
	public static final String DELETE_HIGHLOWSTATE = "delete from highlowstate";
	
	public static final String UPDATE_SERVICE_LENGTH = "update services set lengthhours = ? where serviceid = ?";
	
	public static final String SELECT_BIDDABLE_SERVICE = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name from services s, degens d where d.degenid = s.degenid and s.type = ?";
	
	public static final String GET_BIDDABLE_SERVICE_ID = "select serviceid from services where type = ?";
	
	public static final String UPDATE_BIDDABLE_SERVICE = "update services set farms = ?, lengthhours = ?, name = ?, rateminutes = ?, degenid = ?, price = ? ,active = true where type = ?";
	
	public static final String UPDATE_BIDDABLE_SERVICE_ACTIVE = "update services set active = false where type = 1";
	
	public static final String SELECT_FARMS = "select farms.farmattempts from degens, farms where degens.degenid = farms.degenid and degens.discordid = ?";
	
	public static final String INSERT_PREDETERMINED_SERVICE = "insert into services(farms, lengthhours, name, rateminutes, degenid,type, price, level) values (?,?,?,?,?,?,?,?)";
	
	public static final String COUNT_ACTIVE_SERVICES = "select count(1) from services where active = true and type not in (1,4)";
	
	public static final String COUNT_ACTIVE_SERVICES_BY_DEGEN = "select count(1) from services where active = true and type not in (1,4) and degenid = ?";
	
	public static final String DEACTIVATE_DEGEN = "update degens set active = false where degenid = ?";
	
	public static final String REACTIVATE_DEGEN = "update degens set active = true where degenid = ?";
	
	public static final String SELECT_DEGEN_ACTIVE_SERVICEIDS = "select serviceid from services where degenid = ?";
	
	public static final String INSERT_BIDDABLE_SERVICE = "insert into services(farms, lengthhours, name, rateminutes,degenid, active, type, price) values (?,?,?,?,?,false,?,?)";
	
	public static final String SELECT_DEGEN_PRED_SERVICE = "select s.serviceid, s.farms, s.lengthhours, s.name, s.rateminutes, s.forsale, s.active, s.degenid, d.discordid, s.price, d.name from services s, degens d where d.degenid = s.degenid and s.type = 2 and d.degenid = ?";
	
	public static final String SELECT_DEGEN = "select degenid, ironman, active from degens where discordid=?";
	
	public static final String RAISE_LEVEL = "update degens set level = level + 1 where discordid = ?";
	
	public static final String SELECT_LEVEL = "select cost, servicereward, degens.level from levels, degens where levels.level = degens.level and degens.discordid = ?";
	
	public static final String SELECT_MULTIPLIER = "select ifnull(sum(d.multiplier),0) + \r\n"
			+ "(select farmmultiplier from levels, degens where levels.level = (select level from degens where degenid = ?) and degens.degenid = ?) \r\n"
			+ "from (select t.multiplier from types t, items i, shop s where s.itemname = i.name and i.type = t.typeid and s.degenid = ? and s.forsale = false group by i.name) d";
	
	public static final String SELECT_DUPE_COUNT_VAL = "select a.count, s.value, i.type from(\r\n"
			+ "select count(1) count from shop, degens where itemname = (select itemname from shop, degens where itemid = ? and discordid = ? and degens.degenid = shop.degenid and forsale = false) \r\n"
			+ "and shop.degenid = degens.degenid and discordid = ? and forsale = false) a, shop s, items i where s.itemid = ? and i.name = s.itemname";
	
	public static final String DELETE_ITEM = "delete from shop where itemid = ?";
}
