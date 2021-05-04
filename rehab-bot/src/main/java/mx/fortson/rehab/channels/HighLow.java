package mx.fortson.rehab.channels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import mx.fortson.rehab.RehabBot;
import mx.fortson.rehab.bean.PlayerWinsBean;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.RehabCommandsEnum;
import mx.fortson.rehab.utils.FarmUtils;
import mx.fortson.rehab.utils.HighLowUtils;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.RandomUtils;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public final class HighLow implements IChannel{
	
	private static Map<Long,Boolean> playersBets;
	private static Map<Long,PlayerWinsBean> playersWins;
	private static int number;
	private static TimerTask highLowTask = null;
	private static Timer timer = new Timer("HighLowTimer");
	private static final Map<Integer,String> NUMBERS_EMOJIS = createMap();

	static{
		
		
		playersBets = new HashMap<>();
		playersWins = HighLowUtils.loadState();;
		number = RandomUtils.randomInt(13);
		highLowTask = new TimerTask() {
			@Override
			public void run() {
				RehabBot.getOrCreateChannel(ChannelsEnum.HIGHLOW).sendMessage(generateNewNumberMessage()).allowedMentions(new ArrayList<>()).queue();
				highLowTask.cancel();
				timer.purge();
				highLowTask = null;
			}
		};
		timer.schedule(highLowTask, 1000L * 10);
	}
	
	private void initTask() {
		if(null==highLowTask) {
			highLowTask = new TimerTask() {
				@Override
				public void run() {
					int oldNumber = number;
					boolean equal = true;
					while(equal) {
						number = RandomUtils.randomInt(13);
						equal = number==oldNumber;
					}
					List<Long> losers = new ArrayList<>();
					for(Entry<Long,PlayerWinsBean> entry : playersWins.entrySet()) {
						if(playersBets.containsKey(entry.getKey())) {
							if(playersBets.get(entry.getKey()).equals(number>oldNumber)){
								entry.getValue().raiseRate();
								entry.getValue().updateFarms();
							}else {
								losers.add(entry.getKey());
							}
						}else {
							entry.getValue().lowerRate();
						}
					}
					playersWins.keySet().removeAll(losers);
					
					playersBets.clear();
					highLowTask.cancel();
					timer.purge();
					highLowTask = null;
					HighLowUtils.saveState(playersWins);
					RehabBot.getOrCreateChannel(ChannelsEnum.HIGHLOW).sendMessage(generateGameEndMessage(oldNumber)).allowedMentions(new ArrayList<>()).queue();
					RehabBot.getOrCreateChannel(ChannelsEnum.HIGHLOW).sendMessage(generateNewNumberMessage()).allowedMentions(new ArrayList<>()).queue();
				}
			};
			timer.schedule(highLowTask, 1000L * 30);
			RehabBot.getOrCreateChannel(ChannelsEnum.HIGHLOW).sendMessage("Get your bets in, the game will resolve in 30 seconds.").allowedMentions(new ArrayList<>()).queue();
		}
	}
	
	private static Map<Integer, String> createMap() {
		Map<Integer,String> result = new HashMap<>();
		for(int i = 1; i<=9; i++) {
			result.put(i, i + "\u20E3");
		}
		result.put(10, "1\u20E30\u20E3");
		result.put(11, "1\u20E31\u20E3");
		result.put(12, "1\u20E32\u20E3");
		result.put(13, "1\u20E33\u20E3");
		return Collections.unmodifiableMap(result);
	}

	protected CharSequence generateGameEndMessage(int oldNumber) {
		StringBuilder sb = new StringBuilder();
		sb.append("The previous number was `")
		.append(oldNumber)
		.append("`, the new number is... `")
		.append(number)
		.append("`! `")
		.append(number>oldNumber ? "High" : "Low")
		.append("` wins.");
		for(Entry<Long, PlayerWinsBean> playerWins : playersWins.entrySet()) {
			sb.append("\n<@")
			.append(playerWins.getKey())
			.append("> You have `")
			.append(playerWins.getValue().cashOut())
			.append("` farm(s) you can cash out and your rate is `")
			.append(playerWins.getValue().getRate())
			.append("`.");
		}
		return sb.toString();
	}

	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		String messageContent = event.getMessage().getContentDisplay();
		if(messageContent.startsWith("!")) {
			MessageChannel channel = event.getChannel();
			User author = event.getAuthor();
			RehabCommandsEnum commandEnum = RehabCommandsEnum.fromCommand(messageContent, ChannelsEnum.HIGHLOW);
			if(null!=commandEnum) {
				switch(commandEnum) {
				case HIGH:
					if(!playersWins.containsKey(author.getIdLong())) {
						if(FarmUtils.getFarms(author.getIdLong())>=10) {
							FarmUtils.addSetFarmsToUser(author.getIdLong(), -10);
							playersWins.put(author.getIdLong(), new PlayerWinsBean());
						}else {
							channel.sendMessage("<@" + author.getIdLong() + "> You don't have enough farms to play. It costs 10 farms to start.").allowedMentions(new ArrayList<>()).queue();;
							return;
						}
					}
					if(playersBets.containsKey(author.getIdLong())) {
						channel.sendMessage(generateExistingBetMessage(author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					}else {
						playersBets.put(author.getIdLong(), true);
						channel.sendMessage(generateRegisteredBetMessage(author.getIdLong(),true)).allowedMentions(new ArrayList<>()).queue();
						initTask();
					}
					break;
				case LOW:
					if(!playersWins.containsKey(author.getIdLong())) {
						if(FarmUtils.getFarms(author.getIdLong())>=10) {
							FarmUtils.addSetFarmsToUser(author.getIdLong(), -10);
							playersWins.put(author.getIdLong(), new PlayerWinsBean());
						}else {
							channel.sendMessage("<@" + author.getIdLong() + "> You don't have enough farms to play. It costs 10 farms to start.").allowedMentions(new ArrayList<>()).queue();;
							return;
						}
					}
					if(playersBets.containsKey(author.getIdLong())) {
						channel.sendMessage(generateExistingBetMessage(author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					}else {
						playersBets.put(author.getIdLong(), false);
						channel.sendMessage(generateRegisteredBetMessage(author.getIdLong(),false)).allowedMentions(new ArrayList<>()).queue();
						initTask();
					}
					break;
				case STATUSHIGHLOW:
					channel.sendMessage(generateStatusMessage(author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					break;
				case CASHOUT:
					channel.sendMessage(cashout(author.getIdLong())).allowedMentions(new ArrayList<>()).queue();
					break;
				default:
					BotCommands.commonCommands(ChannelsEnum.HIGHLOW,commandEnum, event);
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

	private CharSequence cashout(long idLong) {
		if(playersBets.containsKey(idLong)) {
			return "You need to wait for the current game to end before you can cash out.";
		}
		if(playersWins.containsKey(idLong)) {
			int farmAmount = playersWins.get(idLong).cashOut();
			double multiplier = FarmUtils.getMultiplier(idLong);
			FarmUtils.addSetFarmsToUser(idLong, Math.toIntExact(Math.round((farmAmount - 10) * multiplier)) + 10);
			playersWins.remove(idLong);
			HighLowUtils.saveState(playersWins);
			return "You have successfully cashed out `" + farmAmount + "` farms. See you soon.";
		}
		return "You don't have any farms to cash out, remember to type `!high` or `!low` to gamble on the numbers.";
	}

	private CharSequence generateStatusMessage(long idLong) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append(">: ");
		if(playersWins.containsKey(idLong)) {
			PlayerWinsBean playerWins = playersWins.get(idLong);
			sb.append("You could cash out `")
			.append(playerWins.cashOut())
			.append("` farm(s). Your current rate is `")
			.append(String.format("%.2f", playerWins.getRate()))
			.append("`");
		}else {
			sb.append("You don't have any farms to cash out.");
		}
		sb.append(" The current number to bet on is `")
		.append(number)
		.append("`");
		return sb.toString();
	}

	private CharSequence generateRegisteredBetMessage(Long id, boolean b) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(id)
		.append(">: Your bet for `")
		.append(b ? "High" : "Low")
		.append("` has been registered.");
		return sb.toString();
	}

	private static CharSequence generateNewNumberMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("New number is ")
		.append(NUMBERS_EMOJIS.get(number))
		.append(" type `!high` or `!low` to gamble on the next number");
		return sb.toString();
	}

	private CharSequence generateExistingBetMessage(long idLong) {
		StringBuilder sb = new StringBuilder();
		sb.append("<@")
		.append(idLong)
		.append("> already has a bet for `")
		.append(playersBets.get(idLong) ? "High" : "Low")
		.append("`. Wait for the current game to end to keep playing.");
		return sb.toString();
	}

}
