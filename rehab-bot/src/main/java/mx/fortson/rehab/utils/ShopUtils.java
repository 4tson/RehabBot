package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.bean.TransactionResultBean;

public class ShopUtils {

	public static List<ItemBean> getShopContents() {
		List<ItemBean> items = new ArrayList<>();
		try {
			
			items = DatabaseDegens.getShopContents();
			items.addAll(DatabaseDegens.getServicesContents());
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return items;
	}

	public static TransactionResultBean transaction(String itemIdS, long discordId, boolean service) {
		TransactionResultBean result = new TransactionResultBean();
		try {
			
			if(StringUtils.isNumeric(itemIdS)) {
				ItemBean item = null;
				if(service) {
					item = DatabaseDegens.getServiceById(Long.parseLong(itemIdS));
					item.setItemName("A service");
				}else {
					item = DatabaseDegens.getShopItemById(Long.parseLong(itemIdS));
				}
				
				if(item==null) {
					result.setFlavourText("No such item. Are you sure you have the right command?");
					return result;
				}
				
				if(item.getOwnerDiscordId().equals(discordId)) {
					result.setFlavourText("How do you expect to buy from yourself?");
				}else if(item.isForSale()) {
					Long buyerFunds = DatabaseDegens.getFundsById(discordId);
					if(buyerFunds>=item.getPrice()) {
						int buyerDegenId = DatabaseDegens.getDegenId(discordId);
						if(service) {
							DatabaseDegens.updateSeviceNewOwner(Math.toIntExact(item.getItemID()), buyerDegenId);
						}else{
							DatabaseDegens.updateShopNewOwner(item.getItemID(), buyerDegenId);
						}
						//We remove the funds from the buyer
						DatabaseDegens.updateFundsSum(-item.getPrice(), buyerDegenId);
						//We add the funds to the seller
						DatabaseDegens.updateFundsSum(item.getPrice(), item.getDegenID());
						result.setBuyerDiscordId(discordId);
						result.setSellerDiscordId(item.getOwnerDiscordId());
						result.setItemSold(item);
						result.setSold(true);
					}else {
						result.setFlavourText("You don't have the funds to buy this item. Try `!farm` or `!duel`");
					}
				}else {
					result.setFlavourText("This item is not for sale, maybe ask the owner to put it up for sale?");
				}
			}else {
				result.setFlavourText("You need to buy by id, not name.");
			}
			
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean putForSale(String itemIdS, long discId) {
		try {
			
			ItemBean itemDB = DatabaseDegens.getShopItemById(Long.parseLong(itemIdS));
			if(itemDB!=null) {
				if(itemDB.getOwnerDiscordId().equals(discId) && !itemDB.isForSale()) {
					DatabaseDegens.updateItemForSale(itemDB.getItemID());
					return true;
				}
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return false;
	}

	public static boolean cancelSale(String itemIdS, long discId) {
		try {
			
			ItemBean itemDB = DatabaseDegens.getShopItemById(Long.parseLong(itemIdS));
			if(itemDB!=null) {
				if(itemDB.getOwnerDiscordId().equals(discId) && itemDB.isForSale()) {
					DatabaseDegens.updateItemForSale(itemDB.getItemID());
					return true;
				}
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return false;
	}


	public static boolean putForSale(String itemIdS, String priceS, long discId, boolean service) {
		try {
			
			if(service) {
				ServiceBean serviceDB = DatabaseDegens.getServiceById(Long.parseLong(itemIdS));
				if(serviceDB.getOwnerDiscordId().equals(discId) && !serviceDB.isForSale() && !serviceDB.isActive()) {
					DatabaseDegens.putServiceForSaleSetPrice(serviceDB.getServiceId(),FormattingUtils.parseAmount(priceS));
					return true;
				}
			}else {
				ItemBean itemDB = DatabaseDegens.getShopItemById(Long.parseLong(itemIdS));
				if(itemDB!=null) {
					if(itemDB.getOwnerDiscordId().equals(discId) && !itemDB.isForSale()) {
						DatabaseDegens.putItemForSaleSetPrice(itemDB.getItemID(),FormattingUtils.parseAmount(priceS));
						return true;
					}
				}
			}
		}catch(SQLException e) {	
			e.printStackTrace();
		}
		return false;
	}
}
