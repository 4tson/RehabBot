
package mx.fortson.rehab.channels;

import java.util.ArrayList;
import java.util.List;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.Degen;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.PagedImageMessageBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.enums.RolesEnum;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.FundUtils;
import mx.fortson.rehab.utils.InventoryUtils;
import mx.fortson.rehab.utils.LeaderBoardUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class BotCommands implements IChannel{
	
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
			if(messageContent.startsWith("!")) {
				MessageChannel channel = event.getChannel();
				User author = event.getAuthor();
				RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.BOTCOMMANDS);
				if(null!=commandEnum) {
					switch(commandEnum) {
					case REGISTERDEGEN:
						boolean registerSuccess = RehabBot.register(author.getIdLong(),author.getName(),false);
						if(registerSuccess) {
							event.getGuild().addRoleToMember(event.getMember(),  RehabBot.getOrCreateRole(RolesEnum.DEGEN)).queue();
						}
						channel.sendMessage(MessageUtils.getRegistrationMessage(registerSuccess,author.getName())).allowedMentions(new ArrayList<>()).queue();
						break;
					case REGISTERIRON:
						boolean registerSuccessIron = RehabBot.register(author.getIdLong(),author.getName(),true);
						if(registerSuccessIron) {
							event.getGuild().addRoleToMember(event.getMember(), RehabBot.getOrCreateRole(RolesEnum.IRONMAN)).queue();
						}
						channel.sendMessage(MessageUtils.getRegistrationMessage(registerSuccessIron,author.getName())).allowedMentions(new ArrayList<>()).queue();
						break;
					case DEACTIVATE:
						channel.sendMessage(MessageUtils.getDeactivationMessage(RehabBot.deactivateDegen(author.getIdLong()),author.getIdLong()));
						break;
					case ADDANNOUNCEMENTROLE:
						String action = "added";
						if(event.getMember().getRoles().contains(RehabBot.getOrCreateRole(RolesEnum.ANNOUNCEMENTS))) {
							event.getGuild().removeRoleFromMember(event.getMember(), RehabBot.getOrCreateRole(RolesEnum.ANNOUNCEMENTS)).queue();
							action = "removed";
						}else {
							event.getGuild().addRoleToMember(event.getMember(), RehabBot.getOrCreateRole(RolesEnum.ANNOUNCEMENTS)).queue();
						}
						channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(), RolesEnum.ANNOUNCEMENTS.getName(), action)).allowedMentions(new ArrayList<>()).queue();
						break;
					default:
						commonCommands(ChannelsEnum.BOTCOMMANDS,commandEnum, event);
						break;	
					}
				}else {
					event.getMessage().delete().queue();
					channel.sendMessage(MessageUtils.announceWrongCommand(event.getMessage().getContentDisplay())).allowedMentions(new ArrayList<>()).queue();
				}
			}else {
				event.getMessage().delete().queue();
			}
	}

	@SuppressWarnings("unchecked")
	public static void commonCommands(ChannelsEnum channelEnum,RehabCommandsEnum commandEnum, GuildMessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		User author = event.getAuthor();
		switch(commandEnum) {
		case COMMANDS:
			channel.sendMessage(MessageUtils.getAvailableRehabCommands(channelEnum)).queue();
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
			int imageCount = 0;
			PagedImageMessageBean result = MessageUtils.getInventoryImage(InventoryUtils.getInventory(author.getIdLong()),author.getName(),imageCount);
			PrivateChannel pm = event.getAuthor().openPrivateChannel().complete();
			MessageAction messageAction = pm.sendMessage("Here is your inventory.");
			while(result.isMoreRecords()) {
				imageCount++;
				messageAction = messageAction.addFile(result.getImageBytes(),result.getImageName());
				result = MessageUtils.getInventoryImage((List<ItemBean>)(Object)result.getLeftOverRecords(),author.getName(),imageCount);
				if(imageCount==10) {
					messageAction.complete();
					messageAction = pm.sendMessage("And anotha one.");
					imageCount = 0;
				}
			}
			messageAction.addFile(result.getImageBytes(), result.getImageName()).queue();
			channel.sendMessage("Inventory sent via PM.").queue();
			break;
		case FUNDS:
			channel.sendMessage(MessageUtils.getUserFunds(FundUtils.getBankValue(author.getIdLong()), author.getName())).allowedMentions(new ArrayList<>()).queue();
			break;
		case FARMS:
			channel.sendMessage(MessageUtils.getuserFarms(FarmUtils.getFarms(author.getIdLong()), author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
			break;
		default:
			event.getMessage().delete().queue();
			channel.sendMessage(MessageUtils.announceWrongCommand(event.getMessage().getContentDisplay())).allowedMentions(new ArrayList<>()).queue();
			break;	
		}
	}
}
