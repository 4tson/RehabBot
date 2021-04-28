package mx.fortson.rehab;

import java.util.ArrayList;
import java.util.List;

import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.enums.ShopCommandsEnum;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ShopUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Shop {

	@SuppressWarnings("unchecked")
	public static void processCommand(GuildMessageReceivedEvent event) {
		Role shopperRole = event.getGuild().getRolesByName("shopper", true).get(0);
		String messageContent = event.getMessage().getContentRaw();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			switch(ShopCommandsEnum.fromCommand(messageContent)) {
			case BUY:
				channel.sendMessage(MessageUtils.getTransactionResult(ShopUtils.transaction(messageContent.split(" ")[1],author.getIdLong(),false))).allowedMentions(new ArrayList<>()).queue();
				break;
			case BUY_SERVICE:
				channel.sendMessage(MessageUtils.getTransactionResult(ShopUtils.transaction(messageContent.split(" ")[1],author.getIdLong(),true))).allowedMentions(new ArrayList<>()).queue();
				break;
			case COMMANDS:
				channel.sendMessage(MessageUtils.getAvailableShopCommands()).queue();
				break;
			case NON_EXISTANT:
				break;
			case SELL:
				if(messageContent.split(" ").length==2) {
				channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(messageContent.split(" ")[1],author.getIdLong()),"",shopperRole.getIdLong())).queue();
				}
				if(messageContent.split(" ").length==3) {
					channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(messageContent.split(" ")[1],messageContent.split(" ")[2],author.getIdLong(),false),"",shopperRole.getIdLong())).queue();
				}
				break;
			case SELL_SERVICE:
				if(messageContent.split(" ").length==3) {
					channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(messageContent.split(" ")[1],messageContent.split(" ")[2],author.getIdLong(),true),"",shopperRole.getIdLong())).queue();
				}
				break;
			case SHOP:
				PagedMessageBean paging = MessageUtils.getShopDetails(ShopUtils.getShopContents());
				while(paging.isMoreRecords()) {
					channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
					paging = MessageUtils.getShopDetails((List<ItemBean>)(Object)paging.getLeftOverRecords());
				}
				channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
				break;
			case CANCEL_SALE:
				if(messageContent.split(" ").length>1) {
					channel.sendMessage(MessageUtils.announceSaleCancel(ShopUtils.cancelSale(messageContent.split(" ")[1],author.getIdLong()),author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
				}
			case ADD_ROLE:
				event.getGuild().addRoleToMember(event.getMember(), shopperRole).queue();;
				break;
			case REM_ROLE:
				event.getGuild().removeRoleFromMember(event.getMember(), shopperRole).queue();
				break;
			default:
				event.getMessage().delete().queue();
				break;
			}
		}else {
			event.getMessage().delete().queue();
		}
	}

}
