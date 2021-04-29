package mx.fortson.rehab.channels;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ShopUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Shop implements IChannel{

	@SuppressWarnings("unchecked")
	public void processMessage(GuildMessageReceivedEvent event) {
		Role shopperRole = RehabBot.getOrCreateRole("shopper");
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.SHOP);
			String[] contentSplit = messageContent.split(" ");
			if(null!=commandEnum) {
				switch(commandEnum) {
				case BUYSHOP:
					if(contentSplit.length==2 && StringUtils.isNumeric(contentSplit[1])) {
						channel.sendMessage(MessageUtils.getTransactionResult(ShopUtils.transaction(messageContent.split(" ")[1],author.getIdLong(),false))).allowedMentions(new ArrayList<>()).queue();
					}else {
						channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					}
					break;
				case SELLSHOP:
					if(contentSplit.length>1) {
						if(contentSplit.length==2 && StringUtils.isNumeric(contentSplit[1])) {
							channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(contentSplit[1],author.getIdLong()),"",shopperRole.getIdLong())).queue();
						}else if(contentSplit.length==3 && StringUtils.isNumeric(contentSplit[1]) && StringUtils.isNumeric(contentSplit[2])) {
							channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(contentSplit[1],contentSplit[2],author.getIdLong(),false),"",shopperRole.getIdLong())).queue();
						}else {
							channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
						}
					}else{
						channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					}
					break;
				case BUY_SERVICE:
					if(contentSplit.length==2 && StringUtils.isNumeric(contentSplit[1])) {
						channel.sendMessage(MessageUtils.getTransactionResult(ShopUtils.transaction(messageContent.split(" ")[1],author.getIdLong(),true))).allowedMentions(new ArrayList<>()).queue();
					}else {
						channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					}
					break;
				case SELLSERVICE:
					if(contentSplit.length==3 && StringUtils.isNumeric(contentSplit[1]) && StringUtils.isNumeric(contentSplit[2])) {
						channel.sendMessage(MessageUtils.announceSale(ShopUtils.putForSale(contentSplit[1],contentSplit[2],author.getIdLong(),true),"",shopperRole.getIdLong())).queue();
					}else {
						channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					}
					break;
				case SHOPAVAIL:
					PagedMessageBean paging = MessageUtils.getShopDetails(ShopUtils.getShopContents());
					while(paging.isMoreRecords()) {
						channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
						paging = MessageUtils.getShopDetails((List<ItemBean>)(Object)paging.getLeftOverRecords());
					}
					channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
					break;
				case CANCEL_SALE:
					if(contentSplit.length==2 && StringUtils.isNumeric(contentSplit[1])) {
						channel.sendMessage(MessageUtils.announceSaleCancel(ShopUtils.cancelSale(contentSplit[1],author.getIdLong()),author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					}else {
						channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
					}
					break;
				case ADDROLESHOP:
					event.getGuild().addRoleToMember(event.getMember(), shopperRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),shopperRole.getName(),"added")).allowedMentions(new ArrayList<>()).queue();
					break;
				case REMROLESHOP:
					event.getGuild().removeRoleFromMember(event.getMember(), shopperRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),shopperRole.getName(),"removed")).allowedMentions(new ArrayList<>()).queue();
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.SHOP,commandEnum, event);
					break;	
				}
			}else {
				event.getMessage().delete().queue();
				channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
			}
		}else {
			event.getMessage().delete().queue();
		}
			
	}
}
