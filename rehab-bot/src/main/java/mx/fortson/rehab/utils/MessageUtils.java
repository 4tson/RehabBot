package mx.fortson.rehab.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.BankBean;
import mx.fortson.rehab.bean.BiddableServiceBean;
import mx.fortson.rehab.bean.Degen;
import mx.fortson.rehab.bean.DuelResultBean;
import mx.fortson.rehab.bean.FarmCollectionBean;
import mx.fortson.rehab.bean.FarmResultBean;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.LevelBean;
import mx.fortson.rehab.bean.LevelUpResultBean;
import mx.fortson.rehab.bean.PagedImageMessageBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.bean.PredeterminedServiceSaleBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.bean.TransactionResultBean;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.FarmTypeEnum;
import mx.fortson.rehab.enums.RegisterResultEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.enums.RolesEnum;
import mx.fortson.rehab.image.InventoryImage;
import mx.fortson.rehab.image.LeaderboardImage;
import net.dv8tion.jda.api.entities.Message.MentionType;

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

	public static CharSequence getRegistrationMessage(RegisterResultEnum registerResult, String name) {
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			switch(registerResult) {
				case ALREADY_ACTIVE:
					sb.append(" is already registered.");
					break;
				case FAIL:
					return "There has been an error during registration";
				case REACTIVATE:
					sb.append(" has been reactivated. Welcome back :)");
					break;
				case SUCCESS:
					sb.append(" has been successfully registered. Enjoy ")
					.append(FormattingUtils.format(DatabaseDegens.getInitialFunds()))
					.append(" starting funds.");
					break;
				case FAIL_NORMIE_TO_IRON:
					sb.append(" you used to be a normie. If you wish to become an ironman, you will need to `!wipe` and start over.");
					break;
			}
			return sb.toString();
		}catch(SQLException e) {
			e.printStackTrace();
			return "Registered";
		}
	}

	public static PagedMessageBean getFarmResult(FarmCollectionBean farmCollectionBean, Long id) {
		PagedMessageBean result = new PagedMessageBean();
		List<FarmResultBean> leftovers = new ArrayList<>(farmCollectionBean.getFarms());
		
		if(!farmCollectionBean.isExists()) {
			result.setMessage("This user is not registered! Protip: `!degen`");
		}else if(!farmCollectionBean.isAttempts()) {
			result.setMessage("You have run out of farm tries today. You have a chance to get more by winning duels.");
		}else {
		
			StringBuilder sb = new StringBuilder();
			
			for(FarmResultBean farmResult : farmCollectionBean.getFarms()) {
				if(sb.length()<RehabBotConstants.DISCORD_MAX_MESSAGE_LENGTH-260){
					if(farmResult.getType().equals(FarmTypeEnum.ITEM_UNIQUE) || farmResult.getType().equals(FarmTypeEnum.SERVICE)){
						sb.append("<@&")
						.append(RehabBot.getOrCreateRole(RolesEnum.UNIQUES).getIdLong())
						.append("> - ");
						result.setPingList(Collections.singletonList(MentionType.ROLE));
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
						sb.append("<@")
						.append(id)
						.append("> - You found `")
						.append(farmResult.getName())
						.append("` A level `")
						.append(farmResult.getLevel())
						.append("` service, it will generate `")
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
					leftovers.remove(farmResult);
				}else {
					result.setMoreRecords(true);
					result.setMessage(sb.toString());
					result.setLeftOverRecords(leftovers);
					break;
				}
			}
			
			if(!result.isMoreRecords()) {
				result.setMoreRecords(false);
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
		}
		return result;
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
		
		sb.append("`+----------------------------------------------------------------------------------------------------+\r\n"
				+ "|                                            Shop                                                     |\r\n"
				+ "+----------------+------------------------+---------------+---------------+-----------------+---------+\r\n"
				+ "|       ID       |          Name          |     Owner     |     Price     |      Value      |  Level  |\r\n"
				+ "+----------------+------------------------+---------------+---------------+-----------------+---------+\r\n");
		
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
				.append("|")
				.append(FormattingUtils.tableFormat(shopItem.isService() ? String.valueOf(shopItem.getRequiredLevel()) : "-",9))
				.append("|\r\n")
				.append("+----------------+------------------------+---------------+---------------+-----------------+---------+\r\n");
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

	public static PagedImageMessageBean getInventoryImage(List<ItemBean> inventory, String name) {
		
		PagedImageMessageBean result = new PagedImageMessageBean();
		try {
			result = InventoryImage.generateInventoryImage(inventory,name);
			result.setMoreRecords(!result.getLeftOverRecords().isEmpty());
			result.setImageName(name + "_inv_" + System.currentTimeMillis() + ".png");
		}catch(IOException e) {
			e.printStackTrace();
			result.setMessage("Could not generate inventory");
		}
		return result;
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

	public static CharSequence confirmServiceCancel(ServiceBean serviceBean) {
		StringBuilder sb = new StringBuilder();
		sb.append("Are you sure you want to cancel ")
		.append(serviceBean.info())
		.append("? You will not be refunded for this service. **This action cannot be undone.** Y/N");
		return sb.toString();
	}

	public static CharSequence getuserFarms(int farms, Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(id)
		.append(">");
		if(farms<0) {
			sb.append(" is not registered. Please run `!degen` or `!ironman` to register.");
		}else {
			sb.append(" has `")
			.append(farms)
			.append("` farm(s) available. Is that a lot?");
		}
		return sb.toString();
	}

	public static CharSequence getDeactivationMessage(boolean deactivated, long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(id)
		.append(">");
		if(deactivated) {
			sb.append("You were successfully deactivated. Come back any time by registering again.");
		}else {
			sb.append("You are not currently active, so I cannot deactivate you");
		}
		return sb.toString();
	}

	public static CharSequence confirmWipe(long idLong, String confirmString) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append("> Are you ABSOLUTELY sure you want to wipe your data? You have 10 seconds to send the following message to confirm: `")
		.append(confirmString)
		.append("`. **THIS ACTION CANNOT BE UNDONE**");
		return sb.toString();
	}

	public static CharSequence confirmLevelUp(long idLong, LevelBean nextLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append("> leveling up will cost you ")
		.append(FormattingUtils.format(nextLevel.getCost()))
		.append(" are you sure you want to continue? Y/N");
		return sb.toString();
	}

	public static CharSequence announceLevelUp(LevelUpResultBean result, Long userId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(userId)
		.append("> ");
		if(result.isLevelUp()) {
			sb.append("You have leveled up. You are now level `")
			.append(result.getNewLevel())
			.append("`.");
			if(result.isFreeService()) {
				sb.append("Additionally, you found `")
				.append(result.getService().getName())
				.append("` it will generate `")
				.append(result.getService().getFarms())
				.append("` farm(s) every `")
				.append(result.getService().getInterval())
				.append("` minute(s) for `")
				.append(result.getService().getLength())
				.append("` hour(s).");
			}
		}else {
			sb.append("There was an issue leveling you up.");
		}
		return sb.toString();
	}

	public static CharSequence announceLevel(LevelBean level, long idLong) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append("> You are currently level `")
		.append(level.getLevel())
		.append("`");
		return sb.toString();
	}

	public static PagedImageMessageBean getLeaderBoardImage(List<Degen> leaderBoard) {
		PagedImageMessageBean result = new PagedImageMessageBean();
		try {
			result = LeaderboardImage.generateLeaderboardImage(leaderBoard);
			result.setMoreRecords(!result.getLeftOverRecords().isEmpty());
			result.setImageName("ldr_" + System.currentTimeMillis() + ".png");
		}catch(IOException e) {
			e.printStackTrace();
			result.setMessage("Could not generate Leaderboard");
		}
		return result;
	}
}
