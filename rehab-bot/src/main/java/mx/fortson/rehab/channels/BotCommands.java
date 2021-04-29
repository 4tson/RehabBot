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
import mx.fortson.rehab.utils.FundUtils;
import mx.fortson.rehab.utils.InventoryUtils;
import mx.fortson.rehab.utils.LeaderBoardUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
							event.getGuild().addRoleToMember(event.getMember(),  RehabBot.getOrCreateRole("degen")).queue();
						}
						channel.sendMessage(MessageUtils.getRegistrationMessage(registerSuccess,author.getName())).allowedMentions(new ArrayList<>()).queue();
						break;
					case REGISTERIRON:
						boolean registerSuccessIron = RehabBot.register(author.getIdLong(),author.getName(),true);
						if(registerSuccessIron) {
							event.getGuild().addRoleToMember(event.getMember(), RehabBot.getOrCreateRole("ironman")).queue();
						}
						channel.sendMessage(MessageUtils.getRegistrationMessage(registerSuccessIron,author.getName())).allowedMentions(new ArrayList<>()).queue();
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
			PagedImageMessageBean result = MessageUtils.getInventoryImage(InventoryUtils.getInventory(author.getIdLong()),author.getName());
			PrivateChannel pm = event.getAuthor().openPrivateChannel().complete();
			while(result.isMoreRecords()) {
				pm.sendFile(result.getImageBytes(),result.getImageName()).queue();
				result = MessageUtils.getInventoryImage((List<ItemBean>)(Object)result.getLeftOverRecords(),author.getName());
			}
			pm.sendFile(result.getImageBytes(), result.getImageName()).queue();
			channel.sendMessage("Inventory sent via PM").queue();
			break;
		case FUNDS:
			channel.sendMessage(MessageUtils.getUserFunds(FundUtils.getBankValue(author.getIdLong()), author.getName())).allowedMentions(new ArrayList<>()).queue();
			break;
		default:
			event.getMessage().delete().queue();
			channel.sendMessage(MessageUtils.announceWrongCommand(event.getMessage().getContentDisplay())).allowedMentions(new ArrayList<>()).queue();
			break;	
		}
	}
}
