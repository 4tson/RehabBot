package mx.fortson.rehab.channels;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.listeners.DuelStateMachine;
import mx.fortson.rehab.utils.DuelUtils;
import mx.fortson.rehab.utils.FormattingUtils;
import mx.fortson.rehab.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DuelArena implements IChannel{
	
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.DUELARENA);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case DUEL:
					resolveDuel(event);
					break;
				case GIFTCHUCK:
					resolveGiftChuck(event);
					break;
				case CHUCK:
					channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuelSetAmount(author.getIdLong(),-1L))).allowedMentions(new ArrayList<>()).queue();
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.DUELARENA,commandEnum, event);
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

	private void resolveGiftChuck(GuildMessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		User author = event.getAuthor();
		String messageContent = event.getMessage().getContentRaw();
		String[] splitContent = messageContent.split(" ");
		
		if(splitContent.length==3) {
			if(splitContent[1].startsWith("<@") && FormattingUtils.isValidAmount(splitContent[2])) {
				Long giftedId = event.getMessage().getMentionedUsers().get(0).getIdLong();
				if(giftedId.equals(RehabBot.getApi().getSelfUser().getIdLong())) {
					channel.sendMessage("I am flattered, alas I am but a simple bot, what would I even do with money?").queue();
					return;
				}
				Long giftChuckAmount = FormattingUtils.parseAmount((splitContent[2]));
				Long gifterId = author.getIdLong();
				channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.giftChuck(gifterId,giftedId,giftChuckAmount))).allowedMentions(new ArrayList<>()).queue();
				return;
			}
		}
		channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
	}

	private void resolveDuel(GuildMessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		User author = event.getAuthor();
		String messageContent = event.getMessage().getContentRaw();
		
		String[] splitContent = messageContent.split(" ");
		
		if(splitContent.length == 1) {
			channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuel(author.getIdLong()))).allowedMentions(new ArrayList<>()).queue();
			return;
		}
		
		if(splitContent.length == 2 && StringUtils.isNumeric(splitContent[1])) {
			Long stakeAmount = 0L;
			stakeAmount = FormattingUtils.parseAmount(splitContent[1]);
			channel.sendMessage(MessageUtils.getDuelResult(DuelUtils.randomDuelSetAmount(author.getIdLong(),stakeAmount))).allowedMentions(new ArrayList<>()).queue();
			return;
		}
		
		if(splitContent.length==3) {		
			if(splitContent[1].startsWith("<@") && FormattingUtils.isValidAmount(splitContent[2])) {
				Member challengedMember = event.getMessage().getMentionedMembers().get(0);
				Long challengedId = challengedMember.getIdLong();
				if(challengedId.equals(RehabBot.getApi().getSelfUser().getIdLong())) {
					channel.sendMessage("I have been programmed to never lose. I don't think you want to test that.").queue();
				}else if(challengedId.equals(author.getIdLong())) {
					channel.sendMessage("You try to play with yourself... It's not very effective.").queue();
				}else{
					Long amount = FormattingUtils.parseAmount(splitContent[2]);
					channel.sendMessage(MessageUtils.announceChallenge(author.getIdLong(), challengedId, amount)).queue();
				    event.getJDA().addEventListener(new DuelStateMachine(challengedId, author.getIdLong(),amount));
				}
				return;
			}
		}
		
		channel.sendMessage(MessageUtils.announceWrongCommand(messageContent)).allowedMentions(new ArrayList<>()).queue();
	}

}
