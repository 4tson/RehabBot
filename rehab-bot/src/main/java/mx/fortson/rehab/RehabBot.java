package mx.fortson.rehab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.security.auth.login.LoginException;

import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.listeners.MessageListener;
import mx.fortson.rehab.listeners.ServiceListener;
import mx.fortson.rehab.tasks.KillServiceTask;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class RehabBot {
	
	private static JDA api;
	
	public static JDA getApi() {
		return api;
	}
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		JDABuilder builder = JDABuilder.createDefault("")
				.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
		          .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
		          .enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.addEventListeners(new MessageListener());
		try {
			api = builder.build()
			.awaitReady();
			register(getApi().getSelfUser().getIdLong(),"Shop Owner");
			clearAndRestartServices();
			announceUp();
		}catch(LoginException e) {
			System.err.println("login error");
			e.printStackTrace();
		}catch(InterruptedException e) {
			System.err.println("interrupted thread");
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private static void clearAndRestartServices() throws InterruptedException {
		
		TextChannel servicesChannel = getApi().getTextChannelsByName("services", true).get(0);
		TextChannel servicesShopChannel = getApi().getTextChannelsByName("services-shop", true).get(0);
		purgeChannel(servicesChannel);
		purgeChannel(servicesShopChannel);

		
		//Delete all channels in "my-services"
		getApi().getCategoriesByName("my-services", true).get(0).getTextChannels().forEach(a -> a.delete().complete());
		//Get all "running" services
		try {
			
			
			
			PagedMessageBean pagedServiceShop = MessageUtils.getShopDetails(DatabaseDegens.getPredServices());
			while(pagedServiceShop.isMoreRecords()) {
				servicesShopChannel.sendMessage(pagedServiceShop.getMessage()).allowedMentions(new ArrayList<>()).complete();
				pagedServiceShop = MessageUtils.getShopDetails((List<ItemBean>)(Object)pagedServiceShop.getLeftOverRecords());
			}
			servicesShopChannel.sendMessage(pagedServiceShop.getMessage()).allowedMentions(new ArrayList<>()).queue();
			
			
			List<ServiceBean> runningServices = DatabaseDegens.getRunningServices();
			for(ServiceBean runningService : runningServices) {
				
				Member member = getApi().getGuilds().get(0).getMemberById(runningService.getOwnerDiscordId());
				if(null==member) {
					
					DatabaseDegens.deleteService(runningService.getServiceId());
					continue;
				}
				Long timeToRun = (long) (1000 * 60 * 60 * runningService.getLength());
				Long expireTime = timeToRun + System.currentTimeMillis();
				TextChannel createdChannel = RehabBot.getApi().getCategoriesByName("my-services", true).get(0).createTextChannel(runningService.getServiceId() + "-" + runningService.getName() + "-" + runningService.getOwnerName()).complete();
				 
				createdChannel.putPermissionOverride(RehabBot.getApi().getRolesByName("@everyone", true).get(0)).deny(Permission.VIEW_CHANNEL).complete();
				createdChannel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).complete();
				StringBuilder greeting = new StringBuilder();
				greeting.append(runningService.info())
				.append(" You can check the status at any time using !status");
				createdChannel.sendMessage(greeting.toString()).allowedMentions(new ArrayList<>()).complete();
				
				ServiceListener sl = new ServiceListener(createdChannel.getIdLong(), expireTime);
				Service serviceTask = new Service(runningService.getOwnerDiscordId(),
						runningService.getName(),
						runningService.getFarms(),
						createdChannel.getIdLong(),
						true,
						runningService.getServiceId(),
						sl);
				
				Timer serviceTimer = new Timer("Service-" + runningService.getServiceId() + "Timer");
				serviceTimer.schedule(serviceTask, 0L, (1000 * 60 * runningService.getInterval()));
				
				Timer kstTimer = new Timer("KillService-" + runningService.getServiceId() + "Timer");
				KillServiceTask kst = new KillServiceTask(serviceTask);
				kstTimer.schedule(kst, new Date(expireTime));
			}
			//We create the new services service
			ServicesUtils.createNewService();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private static void purgeChannel(TextChannel channel) {
		for(int i = 0; i==2;i++) {
			channel.sendMessage("deleting..").complete();
		}

		List<Message> history = channel.getHistoryFromBeginning(100).complete().getRetrievedHistory();
		while(!history.isEmpty()) {
			channel.purgeMessages(history);
			history = channel.getHistoryFromBeginning(100).complete().getRetrievedHistory();
		}
	}

	public static void announceUp() {
		getApi().getTextChannelsByName("general",true).get(0).sendMessage("@here the bot is now up. !degen if you can't see channels.").queue();
	}
	
	public static boolean register(long id, String name) {
		try {
			
			if(DatabaseDegens.existsById(id)) {
				return false;
			}else {
				return DatabaseDegens.insertNewDegen(id,name);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
