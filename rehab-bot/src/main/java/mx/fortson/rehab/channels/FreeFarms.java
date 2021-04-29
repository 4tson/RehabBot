package mx.fortson.rehab.channels;

import java.util.ArrayList;

import mx.fortson.rehab.utils.FarmUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class FreeFarms implements IChannel{
	@Override
	public void processMessage(GuildMessageReceivedEvent event) {
		User author = event.getAuthor();
		if(!author.isBot()) {
			int farms = FarmUtils.addFarmsToUser(author.getIdLong());
			event.getMessage().delete().queue();
			event.getChannel().sendMessage("Added " + farms + " farm(s) to user <@" + author.getIdLong() + ">").allowedMentions(new ArrayList<>()).queue();
		}
	}

}
