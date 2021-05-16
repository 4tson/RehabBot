package mx.fortson.rehab.channels;

import java.util.ArrayList;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.MessageUtilsResultBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.enums.RolesEnum;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.SlotsUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Slots implements IChannel{

	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		Role jackpotRole = RehabBot.getOrCreateRole(RolesEnum.JACKPOT);
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.SLOTS);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case ROLL:
					MessageUtilsResultBean rollResult = MessageUtils.announceSlotsRoll(SlotsUtils.roll(author.getIdLong()));
					channel.sendMessage(rollResult.getMessage()).allowedMentions(rollResult.getPingList()).queue();
					break;
				case JACKPOT:
					channel.sendMessage(MessageUtils.announceJackpot(SlotsUtils.getCurrentJackpot())).queue();
					break;
				case PAYOUTS:
					channel.sendMessage(MessageUtils.announcePayouts(SlotsUtils.getCurrentPayouts())).queue();
					break;
				case ADDROLESSLOTS:
					event.getGuild().addRoleToMember(event.getMember(), jackpotRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),jackpotRole.getName(),"added")).allowedMentions(new ArrayList<>()).queue();
					break;
				case REMROLESLOTS:
					event.getGuild().removeRoleFromMember(event.getMember(), jackpotRole).queue();
					channel.sendMessage(MessageUtils.announceRoleChange(author.getIdLong(),jackpotRole.getName(),"removed")).allowedMentions(new ArrayList<>()).queue();
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.SLOTS,commandEnum, event);
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

}
