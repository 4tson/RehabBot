package mx.fortson.rehab.listeners;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.Shop;
import mx.fortson.rehab.bean.Degen;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.MessageUtilsResultBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.DuelUtils;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.FormattingUtils;
import mx.fortson.rehab.utils.FundUtils;
import mx.fortson.rehab.utils.InventoryUtils;
import mx.fortson.rehab.utils.LeaderBoardUtils;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{

	@SuppressWarnings("unchecked")
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {		
		if(!event.getAuthor().isBot()) {
			if(event.getChannel().getName().equals("bot-commands")) {
				String messageContent = event.getMessage().getContentRaw();
				if(messageContent.startsWith("!")) {
					MessageChannel channel = event.getChannel();
					User author = event.getAuthor();
					switch(RehabCommandsEnum.fromCommand(messageContent)) {
					case COMMANDS:
						channel.sendMessage(MessageUtils.getAvailableRehabCommands()).queue();
						break;
					case DUEL:
						Long challengedId = event.getMessage().getMentionedUsers().get(0).getIdLong();
						Long amount = FormattingUtils.parseAmount(messageContent.split(" ")[2]);
						channel.sendMessage(MessageUtils.announceChallenge(author.getIdLong(), challengedId, amount)).queue();
					    event.getJDA().addEventListener(new DuelStateMachine(challengedId, author.getIdLong(),amount));
						break;
					case GIFTCHUCK:
						Long giftedId = event.getMessage().getMentionedUsers().get(0).getIdLong();
						Long giftChuckAmount = FormattingUtils.parseAmount((messageContent.split(" ")[2]));
						Long gifterId = author.getIdLong();
						channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.giftChuck(gifterId,giftedId,giftChuckAmount))).allowedMentions(new ArrayList<>()).queue();
						break;
					case DUELSETAMOUNT:
						Long stakeAmount = 0L;
						stakeAmount = FormattingUtils.parseAmount(messageContent.split(" ")[1]);
						channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuelSetAmount(author.getIdLong(),stakeAmount))).allowedMentions(new ArrayList<>()).queue();
						break;
					case DUELRANDO:
						channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuel(author.getIdLong()))).allowedMentions(new ArrayList<>()).queue();
						break;
					case FARM:
						int farms = 1;
						String[] splitContent = messageContent.split(" ");
						if(splitContent.length>1) {
							String secondArg = splitContent[1];
							if(StringUtils.isNumeric(secondArg)) {
								farms = Integer.parseInt(secondArg);
							}
						}
						MessageUtilsResultBean messageUtilsResultBean = MessageUtils.getFarmResult(FarmUtils.farmForUser(author.getIdLong(),farms), author.getIdLong());
						channel.sendMessage(messageUtilsResultBean.getMessage()).allowedMentions(messageUtilsResultBean.getPingList()).queue();
						break;
					case FUNDS:
						channel.sendMessage(MessageUtils.getUserFunds(FundUtils.getBankValue(author.getIdLong()), author.getName())).allowedMentions(new ArrayList<>()).queue();
						break;
					case REGISTER:
						boolean registerSuccess = RehabBot.register(author.getIdLong(),author.getName());
						if(registerSuccess) {
							Role degenRole = event.getGuild().getRolesByName("degen", true).get(0);
							event.getGuild().addRoleToMember(event.getMember(), degenRole).queue();;
						}
						channel.sendMessage(MessageUtils.getRegistrationMessage(registerSuccess,author.getName())).allowedMentions(new ArrayList<>()).queue();
						break;
					case LDRBOARD:
						PagedMessageBean paging = MessageUtils.getLeaderBoardMessage(LeaderBoardUtils.getLeaderBoard());
						while(paging.isMoreRecords()) {
							channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
							paging = MessageUtils.getLeaderBoardMessage((List<Degen>)(Object)paging.getLeftOverRecords());
						}
						channel.sendMessage(paging.getMessage()).allowedMentions(new ArrayList<>()).queue();
						break;
					case INVENTORY:
						PagedMessageBean pagingInv = MessageUtils.getInventory(InventoryUtils.getInventory(author.getIdLong()),author.getName());
						PrivateChannel pm = event.getAuthor().openPrivateChannel().complete();
						while(pagingInv.isMoreRecords()) {
							pm.sendMessage(pagingInv.getMessage()).allowedMentions(new ArrayList<>()).queue();
							pagingInv = MessageUtils.getInventory((List<ItemBean>)(Object)pagingInv.getLeftOverRecords(),author.getName());
						}
						pm.sendMessage(pagingInv.getMessage()).allowedMentions(new ArrayList<>()).queue();
						break;
					case CHUCK:
						channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuelSetAmount(author.getIdLong(),-1L))).allowedMentions(new ArrayList<>()).queue();
					case ACCEPT:
						break;
					case ACTIVATESERVICE:
						String id = messageContent.split(" ")[1];
						if(StringUtils.isNumeric(id)) {
							channel.sendMessage(ServicesUtils.activateService(author.getIdLong(),Long.parseLong(id))).queue();
						}else {
							channel.sendMessage("Your command is invalid").queue();
						}
						break;
					default:
						event.getMessage().delete().queue();
						channel.sendMessage("Command not valid, try `!commands` for a list of commands and their description").allowedMentions(new ArrayList<>()).queue();
						break;	
					}
				}else {
					event.getMessage().delete().queue();
				}
			}
			if(event.getChannel().getName().equals("general")) {
				if(event.getMessage().getContentRaw().equalsIgnoreCase("!degen")) {
					boolean registerSuccess = RehabBot.register(event.getAuthor().getIdLong(),event.getAuthor().getName());
					if(registerSuccess) {
						Role degenRole = event.getGuild().getRolesByName("degen", true).get(0);
						event.getGuild().addRoleToMember(event.getMember(), degenRole).queue();;
					}
					event.getChannel().sendMessage(MessageUtils.getRegistrationMessage(registerSuccess,event.getAuthor().getName())).allowedMentions(new ArrayList<>()).queue();
				}
			}
			if(event.getChannel().getName().equals("free-farms")) {
				int farms = FarmUtils.addFarmsToUser(event.getAuthor().getIdLong());
				event.getChannel().sendMessage("Added " + farms + " farm(s) to user <@" + event.getAuthor().getIdLong() + ">").allowedMentions(new ArrayList<>()).queue();
				event.getMessage().delete().queue();
			}
			if(event.getChannel().getName().equals("shop")) {
				Shop.processCommand(event);
			}
			if(event.getChannel().getName().equals("services")) {
				Role servicesRole = event.getJDA().getRolesByName("services", true).get(0);
				if(event.getMessage().getContentRaw().equalsIgnoreCase("!addrole")) {
					event.getGuild().addRoleToMember(event.getMember(), servicesRole).queue();;
				}
				if(event.getMessage().getContentRaw().equalsIgnoreCase("!remrole")) {
					event.getGuild().removeRoleFromMember(event.getMember(), servicesRole).queue();;
				}
			}
			if(event.getChannel().getName().equals("services-shop")) {
				String[] splitContent = event.getMessage().getContentRaw().split(" ");
				if(splitContent.length==2 && splitContent[0].equals("!buy") && StringUtils.isNumeric(splitContent[1])) {
					event.getChannel().sendMessage(MessageUtils.announcePredSale(ServicesUtils.buyPredeterminedService(Integer.parseInt(splitContent[1]),event.getAuthor().getIdLong()),event.getAuthor().getIdLong())).allowedMentions(new ArrayList<>()).queue();
				}else if(!event.getMessage().getContentRaw().startsWith("!")) {
					event.getMessage().delete().queue();
				}else {
					event.getChannel().sendMessage("That is not a valid command in this channel.");
				}
			}
		}
	}
	
}
