package mx.fortson.rehab.utils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.BankBean;
import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.bean.Degen;
import mx.fortson.rehab.bean.DuelResultBean;
import mx.fortson.rehab.bean.FarmCollectionBean;
import mx.fortson.rehab.bean.FarmResultBean;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.MessageUtilsResultBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.bean.PredeterminedServiceSaleBean;
import mx.fortson.rehab.bean.TransactionResultBean;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.FarmTypeEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.enums.ShopCommandsEnum;

public class MessageUtils {

	public static String getAvailableRehabCommands(ChannelsEnum channel) {
		StringBuilder sb = new StringBuilder();
		sb.append("This is the list of commands available in this channel:\n");
		for(RehabCommandsEnum command : RehabCommandsEnum.values()) {
			if(command.isActive() && 
					(command.getChannel().equals(channel) || command.getChannel().equals(ChannelsEnum.ALL))) {
				sb.append("`")
				.append(command.getCommand())
				.append("`")
				.append("   -   ")
				.append(command.getDescription())
				.append("\n");
			}
		}
		
		return sb.toString();
	}
	
	public static String getAvailableShopCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("This is the list of currently available shop commands:\n");
		for(ShopCommandsEnum command : ShopCommandsEnum.values()) {
			if(command.isActive()) {
				sb.append("`")
				.append(command.getCommand())
				.append("`")
				.append("   -   ")
				.append(command.getDescription())
				.append("\n");
			}
		}
		
		return sb.toString();
	}

	public static String getUserFunds(BankBean bankBean, String userName) {
		if(null==bankBean) {
			return "You are not registered to duel, try `!degen`";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(userName)
		.append(" has ")
		.append(FormattingUtils.format(bankBean.getCash()))
		.append(" available and ")
		.append(FormattingUtils.format(bankBean.getInventoryWorth()))
		.append(" in items.");
		Long total = bankBean.getCash() + bankBean.getInventoryWorth();
		if(total.compareTo(1000000L)>0) {
			if(total.compareTo(100000000L)>0) {
				if(total.compareTo(1000000000L)>0) {
					if(total.compareTo(6000000000L)>0) {
						if(total.compareTo(10000000000L)>0){
							if(total.compareTo(300000000000L)>0){
								sb.append(" You are so rich you introduced a bug in the code... This shouldn't be in here smh.");
							}else {
								//>10B <300B
								sb.append(" Okay I cba adding more flavour text, you beat the bot gz.");
							}
						}else {
						//>6B <10B
						sb.append(" There's probably no reason to keep staking...");
						}
					}else {
						//1B> <6B
						sb.append(" Close to rich!");
					}
				}else {
					//100M> <1B
					sb.append(" A few chucks away from glory.");
				}
			}else {
				//1M> <100M
				sb.append(" You could try going to Zulrah? Protip: `!farm`");
			}
			
		}else {
			sb.append(" Who are you, some poor guy?");
		}
		return sb.toString();
	}

	public static CharSequence getRegistrationMessage(boolean registerSuccess, String name) {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			if(registerSuccess) {
				sb.append(" has been successfully registered. Enjoy ")
				.append(FormattingUtils.format(DatabaseDegens.getInitialFunds()))
				.append(" starting funds.");
			}else {
				sb.append(" is already registered.");
			}
			sb.append("\nYou can look at your funds at any time with the command `!bank`");
			return sb.toString();
		}catch(SQLException e) {
			e.printStackTrace();
			return "Could not register";
		}
	}

	public static MessageUtilsResultBean getFarmResult(FarmCollectionBean farmCollectionBean, Long id) {
		MessageUtilsResultBean result = new MessageUtilsResultBean();
		if(!farmCollectionBean.isExists()) {
			result.setMessage("This user is not registered! Protip: `!degen`");
		}else if(!farmCollectionBean.isAttempts()) {
			result.setMessage("You have run out of farm tries today. You have a chance to get more by winning duels.");
		}else {
		
			StringBuilder sb = new StringBuilder();
			
			for(FarmResultBean farmResult : farmCollectionBean.getFarms()) {
				if(farmResult.getType().equals(FarmTypeEnum.ITEM_UNIQUE) || farmResult.getType().equals(FarmTypeEnum.SERVICE)){
					sb.append("@here - ");
					result.setPingList(null);
				}
				switch(farmResult.getType()) {
				case CASH:
					sb.append("<@")
					.append(id)
					.append("> - ")
					.append(FormattingUtils.formatFarm(farmResult.getFlavourText(),farmResult.getFarmedAmount()))
					.append("\n");
				break;
				case ITEM_MEH:
				case ITEM_GREAT:
				case ITEM_UNIQUE:
					sb.append("<@")
					.append(id)
					.append("> - You found a `")
					.append(farmResult.getItemName())
					.append("`! ")
					.append(FormattingUtils.formatFarm(farmResult.getFlavourText(),farmResult.getFarmedAmount()))
					.append(" congratulations.\n");
					break;
				case SERVICE:
					System.out.println("Service found " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
					sb.append("<@")
					.append(id)
					.append("> - You found `")
					.append(farmResult.getName())
					.append("` it will generate `")
					.append(farmResult.getFarms())
					.append("` farm(s) every `")
					.append(farmResult.getInterval())
					.append("` minute(s) for `")
					.append(farmResult.getRateHours())
					.append("` hour(s). ")
					.append(farmResult.getFlavourText())
					.append(" You can `!activateservice {id}` or `!sellservice {id}` it in the appropiate channels. Enjoy.\n");
					break;
				}
			}
			sb.append(" You now have ")
			.append(FormattingUtils.format(farmCollectionBean.getNewFunds()));
			
			if(farmCollectionBean.getNewAttempts()>0) {
				sb.append(". You have ")
				.append(farmCollectionBean.getNewAttempts())
				.append(" attempt(s).");
			}else {
				sb.append(". You have run out of attempts.");
			}
			
			result.setMessage(sb.toString());
		}
		return result;
	}

	public static PagedMessageBean getLeaderBoardMessage(List<Degen> leaderBoard) {
		PagedMessageBean paging = new PagedMessageBean();
		
		StringBuilder sb = new StringBuilder();
		List<Degen> leftovers = new ArrayList<>(leaderBoard);
		
		sb.append("`+-------------------------------------------------------------------+\r\n"
				+ "|                           Leaderboard                             |\r\n"
				+ "+-------------------+----------+----------+--------+--------+-------+\r\n"
				+ "|       Degen       |   Bank   |   Peak   | Avail. | Times  |  W/L  |\r\n"
				+ "|                   |          |          | Farms  | Farmed |       |\r\n"
				+ "+-------------------+----------+----------+--------+--------+-------+\r\n");
		for(Degen degen : leaderBoard) {
			if(sb.length()<RehabBotConstants.DISCORD_MAX_MESSAGE_LENGTH-138){
				sb.append("|")
				.append(FormattingUtils.tableFormat(degen.getName(),19))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(degen.getBank()),10))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(degen.getPeak()),10))
				.append("|")
				.append(FormattingUtils.tableFormat(String.valueOf(degen.getFarmAttempts()),8))
				.append("|")
				.append(FormattingUtils.tableFormat(String.valueOf(degen.getTimesFarmed()),8))
				.append("|")
				.append(FormattingUtils.tableFormat(degen.getWins() + "/" + degen.getLosses(),7))
				.append("|\r\n")
				.append("+-------------------+----------+----------+--------+--------+-------+\r\n");
				leftovers.remove(degen);
			}else {
				sb.deleteCharAt(sb.length()-1);
				sb.append("`");
				paging.setMoreRecords(true);
				paging.setMessage(sb.toString());
				paging.setLeftOverRecords(leftovers);
				break;
			}
		}
		if(!paging.isMoreRecords()) {
			sb.deleteCharAt(sb.length()-1);
			sb.append("`");
			paging.setMoreRecords(false);
			paging.setMessage(sb.toString());
		}
		return paging;
	}

	public static CharSequence getDuelResult(DuelResultBean duelResult) {
		if(duelResult.getFlavourText()!=null) {
			return duelResult.getFlavourText();
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(duelResult.getWinner())
		.append(" won against ")
		.append(duelResult.getLoser())
		.append(" for ")
		.append(FormattingUtils.format(duelResult.getAmount()));
		if(duelResult.isFreeFarm()) {
			sb.append(" This resulted in ")
			.append(duelResult.getFreeFarms())
			.append(" free farm(s), congratulations!");
		}
		return sb.toString();
	}

	public static PagedMessageBean getShopDetails(List<ItemBean> shopContents) {
		PagedMessageBean paging = new PagedMessageBean();
		if(shopContents.isEmpty()) {
			paging.setMessage("There are currently no items for sale, keep an eye out for new sales!");
			paging.setMoreRecords(false);
			return paging;
		}
		
		List<ItemBean> leftovers = new ArrayList<>(shopContents);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("`+------------------------------------------------------------------------------------------+\r\n"
				+ "|                                            Shop                                           |\r\n"
				+ "+----------------+------------------------+---------------+---------------+-----------------+\r\n"
				+ "|       ID       |          Name          |     Owner     |     Price     |      Value      |\r\n"
				+ "+----------------+------------------------+---------------+---------------+-----------------+\r\n");
		
		for(ItemBean shopItem : shopContents) {
			if(sb.length()<RehabBotConstants.DISCORD_MAX_MESSAGE_LENGTH-184){
				sb.append("|")
				.append(FormattingUtils.tableFormat(String.valueOf(shopItem.getItemID()),16))
				.append("|")
				.append(FormattingUtils.tableFormat(shopItem.getItemName(),24))
				.append("|")
				.append(FormattingUtils.tableFormat(shopItem.getOwnerName(),15))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(shopItem.getPrice()),15))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(shopItem.getValue()),17))
				.append("|\r\n")
				.append("+----------------+------------------------+---------------+---------------+-----------------+\r\n");
				leftovers.remove(shopItem);
			}else {
				sb.deleteCharAt(sb.length()-1);
				sb.append("`");
				paging.setMoreRecords(true);
				paging.setMessage(sb.toString());
				paging.setLeftOverRecords(leftovers);
				break;
			}
		}
		if(!paging.isMoreRecords()) {
			sb.deleteCharAt(sb.length()-1);
			sb.append("`");
			paging.setMoreRecords(false);
			paging.setMessage(sb.toString());
		}
		return paging;
	}

	public static CharSequence getTransactionResult(TransactionResultBean transaction) {
		if(!transaction.isSold()) {
			return transaction.getFlavourText();
		}
		StringBuilder sb = new StringBuilder();
		ItemBean item = transaction.getItemSold();
		
		sb.append("<@")
		.append(transaction.getBuyerDiscordId())
		.append("> has bought [")
		.append(item.getItemName())
		.append("] from <@")
		.append(transaction.getSellerDiscordId())
		.append("> for ")
		.append(FormattingUtils.format(item.getPrice()))
		.append(". Get merched kid.");
		
		return sb.toString();
	}

	public static PagedMessageBean getInventory(List<ItemBean> inventory, String name) {
		PagedMessageBean paging = new PagedMessageBean();
		if(inventory.isEmpty()) {
			paging.setMessage("You don't have any items, try farming, or dueling for money and then snatch items from the shop!");
			return paging;
		}
		
		List<ItemBean> leftovers = new ArrayList<>(inventory);
		
		StringBuilder sb = new StringBuilder();
		sb.append("`+----------------------------------------------------------------------------------------------+\r\n")
		.append("|")
		.append(FormattingUtils.tableFormat(name + "'s Inventory",95))
		.append("|\r\n"
				+ "+----------------+------------------------+----------------+-------+-----------------+---------+\r\n"
				+ "|       ID       |          Name          |     Price      | Sale? |      Value      | Active? |\r\n"
				+ "+----------------+------------------------+----------------+-------+-----------------+---------+\r\n");
		for(ItemBean item : inventory){
			if(sb.length()<RehabBotConstants.DISCORD_MAX_MESSAGE_LENGTH-196){
				sb.append("|")
				.append(FormattingUtils.tableFormat(String.valueOf(item.getItemID()),16))
				.append("|")
				.append(FormattingUtils.tableFormat(item.getItemName(),24))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(item.getPrice()),16))
				.append("|")	
				.append(FormattingUtils.tableFormat(item.isForSale() ? "Y": "N",7))
				.append("|")
				.append(FormattingUtils.tableFormat(FormattingUtils.format(item.getValue()),17))
				.append("|")
				.append(FormattingUtils.tableFormat(item.getActiveStr(),9))
				.append("|\r\n")
				.append("+----------------------------------------------------------------------------------------------+\r\n");
				leftovers.remove(item);
			}else {
				sb.deleteCharAt(sb.length()-1);
				sb.append("`");
				paging.setMoreRecords(true);
				paging.setMessage(sb.toString());
				paging.setLeftOverRecords(leftovers);
				break;
			}
		}
		if(!paging.isMoreRecords()) {
			sb.deleteCharAt(sb.length()-1);
			sb.append("`");
			paging.setMoreRecords(false);
			paging.setMessage(sb.toString());
		}
		return paging;
	}

	public static CharSequence announceSale(boolean putForSale, String flavourText, long id) {
		if(putForSale) {
			return "<@&" + id + "> There is a new item up for sale!" + flavourText; 
		}else {
			return "Item was not put for sale.";
		}
	}

	public static CharSequence announceSaleCancel(boolean cancelled, long idLong) {
		if(cancelled) {
			StringBuilder sb = new StringBuilder();
			sb.append("<@")
			.append(idLong)
			.append(">: your sale was cancelled successfully");
			return sb.toString();
		}else{
			return "Sale was not cancelled.";
		}
	}

	public static CharSequence announceChallenge(long idLong, Long challengedId, Long amount) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append("> has challenged <@")
		.append(challengedId)
		.append(">. <@")
		.append(challengedId)
		.append("> has 20 seconds to `!accept @mention` the duel before it gets cancelled.");
		return sb.toString();
	}

	public static CharSequence getServiceResult(Long ownerID, String serviceName, int farms) {
		StringBuilder sb = new StringBuilder();
		sb.append("`")
		.append(serviceName)
		.append("` has added `")
		.append(farms)
		.append("` farm(s) to <@")
		.append(ownerID)
		.append(">'s inventory");
		return sb.toString();
	}

	public static CharSequence announceServiceEnd(Long ownerID, String serviceName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(ownerID)
		.append(">'s service `")
		.append(serviceName)
		.append("` has ended. Be on the lookout for more services!");
		
		return sb.toString();
	}

	public static CharSequence announceNewService(BiddableServiceBean biddableService, Long serviceRoleId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@&")
		.append(serviceRoleId)
		.append(">")
		.append(" `")
		.append(biddableService.getServiceName())
		.append("` has appeared. Offering `")
		.append(biddableService.getFarms())
		.append("` farm(s) every `")
		.append(biddableService.getIntervalMinutes())
		.append("` minute(s) for `")
		.append(biddableService.getLengthHours())
		.append("` hour(s). Please type !bid {amount} in order to bid for this once in a lifetime service. Only one service is active at a time, so don't miss out!")
		.append(" The bid starts at `")
		.append(FormattingUtils.format(biddableService.getBid()))
		.append("`");
		return sb.toString();
	}

	public static CharSequence getServiceBidStatus(BiddableServiceBean biddableService) {
		StringBuilder sb = new StringBuilder();
		sb.append("Current bid for `")
		.append(biddableService.getServiceName())
		.append("` from <@")
		.append(biddableService.getWinnerID())
		.append(">, for ")
		.append(FormattingUtils.format(biddableService.getBid()));
		return sb;
	}

	public static CharSequence getBidResult(boolean bid, BiddableServiceBean biddableService) {
		if(!bid) {
			return "No bid was  made";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("New highest bid for `")
		.append(biddableService.getServiceName())
		.append("` is `")
		.append(FormattingUtils.format(biddableService.getBid()))
		.append("` by <@")
		.append(biddableService.getWinnerID())
		.append(">. You have one minute to outbid before the bidding is over.");
		return sb.toString();
	}

	public static CharSequence announceBidEnd(BiddableServiceBean biddableService) {
		StringBuilder sb = new StringBuilder();
		sb.append("Bidding over for `")
		.append(biddableService.getServiceName())
		.append("`! Winning bid is `")
		.append(FormattingUtils.format(biddableService.getBid()))
		.append("` by <@")
		.append(biddableService.getWinnerID())
		.append(">. The service will now run for `")
		.append(biddableService.getLengthHours())
		.append("` hour(s) and generate `")
		.append(biddableService.getFarms())
		.append("` farm(s) every `")
		.append(biddableService.getIntervalMinutes())
		.append("` minute(s). Keep an eye out for new services!");
		return sb.toString();
	}

	public static CharSequence announcePredSale(PredeterminedServiceSaleBean saleResult, long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(id)
		.append(">: ");
		if(saleResult.isSale()) {
			sb.append("Sale complete! ")
			.append(saleResult.getService().info());
		}else {
			sb.append("Sale of a predetermined service was not completed. Reason: ")
			.append(saleResult.getFlavourText());
		}
		return sb.toString();
	}

	public static CharSequence announceWrongCommand(String messageContent) {
		StringBuilder sb = new StringBuilder();
		sb.append("Command `")
		.append(messageContent)
		.append("` not valid, try `!commands` for a list of commands and their description. You might be in the wrong channel, or have the wrong command format.");
		return sb.toString();
	}

	public static CharSequence announceRoleChange(long idLong, String roleName, String action) {
		StringBuilder sb = new StringBuilder();
		sb.append("The role `")
		.append(roleName)
		.append("` was ")
		.append(action)
		.append(" for user <@")
		.append(idLong)
		.append(">");
		return sb.toString();
	}
}
