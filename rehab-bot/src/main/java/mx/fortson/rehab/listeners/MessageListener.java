package mx.fortson.rehab.listeners;

import mx.fortson.rehab.enums.ChannelsEnum;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {	
		if(!event.getAuthor().isBot()) {
			ChannelsEnum channel = ChannelsEnum.fromName(event.getChannel().getName());
			if(null!=channel && null!=channel.getChannel()) {
				channel.getChannel().processMessage(event);
			}
		}
	}
			
		
		
//			if(event.getChannel().getName().equals("shop")) {
//				Shop.processCommand(event);
//			}
//			if(event.getChannel().getName().equals("services")) {
//				Role servicesRole = event.getJDA().getRolesByName("services", true).get(0);
//				if(event.getMessage().getContentRaw().equalsIgnoreCase("!addrole")) {
//					event.getGuild().addRoleToMember(event.getMember(), servicesRole).queue();;
//				}
//				if(event.getMessage().getContentRaw().equalsIgnoreCase("!remrole")) {
//					event.getGuild().removeRoleFromMember(event.getMember(), servicesRole).queue();;
//				}
//			}
//			if(event.getChannel().getName().equals("services-shop")) {
//				String[] splitContent = event.getMessage().getContentRaw().split(" ");
//				if(splitContent.length==2 && splitContent[0].equalsIgnoreCase("!buy") && StringUtils.isNumeric(splitContent[1])) {
//					event.getChannel().sendMessage(MessageUtils.announcePredSale(ServicesUtils.buyPredeterminedService(Integer.parseInt(splitContent[1]),event.getAuthor().getIdLong()),event.getAuthor().getIdLong())).allowedMentions(new ArrayList<>()).queue();
//				}else if(!event.getMessage().getContentRaw().startsWith("!")) {
//					event.getMessage().delete().queue();
//				}else {
//					event.getChannel().sendMessage("That is not a valid command in this channel.");
//				}
//			}
//	}
	
}
