package mx.fortson.rehab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;

import mx.fortson.rehab.bean.DegenBean;
import mx.fortson.rehab.bean.ItemBean;
import mx.fortson.rehab.bean.LevelBean;
import mx.fortson.rehab.bean.LevelUpResultBean;
import mx.fortson.rehab.bean.PagedMessageBean;
import mx.fortson.rehab.bean.ServiceBean;
import mx.fortson.rehab.bean.ServiceTimerTaskPair;
import mx.fortson.rehab.constants.RehabBotConstants;
import mx.fortson.rehab.enums.CategoriesEnum;
import mx.fortson.rehab.enums.ChannelsEnum;
import mx.fortson.rehab.enums.PredefinedServicesEnum;
import mx.fortson.rehab.enums.RegisterResultEnum;
import mx.fortson.rehab.enums.RolesEnum;
import mx.fortson.rehab.listeners.LeaveListener;
import mx.fortson.rehab.listeners.MessageListener;
import mx.fortson.rehab.listeners.ServiceListener;
import mx.fortson.rehab.tasks.KillServiceTask;
import mx.fortson.rehab.utils.FormattingUtils;
import mx.fortson.rehab.utils.MessageUtils;
import mx.fortson.rehab.utils.RandomUtils;
import mx.fortson.rehab.utils.ServicesUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class RehabBot {
	
	private static JDA api;
	private static Properties props;
	private static Guild guild;
	
	public static Guild getGuild() {
		return guild;
	}
	
	public static JDA getApi() {
		return api;
	}
	
	public static void main(String[] args) {
		try {
			if(args.length<1) {
				System.err.println("Expected properties file location as parameter.");
				System.exit(1);
			}
			initProperties(args[0]);
			
			Runtime.getRuntime().addShutdownHook(new Thread(RehabBot::announceDown));
			
			JDABuilder builder = JDABuilder.createDefault(getBotToken())
					.setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
			          .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
			          .enableIntents(GatewayIntent.GUILD_MEMBERS)
			          .setEnableShutdownHook(false);
			System.out.println("Creating api");
			api = builder.build()
			.awaitReady();
			
			Long botDiscId = getBotId();
			
			if(register(botDiscId,"Shop Owner",false).equals(RegisterResultEnum.SUCCESS)) {
				registerPredefinedServices(botDiscId);
			}
			guild = api.getGuildById(getGuildId());
			System.out.println("Initializing channels");
			initChannels();
			System.out.println("Clearing and restarting services");
			clearAndRestartServices();
			
			if(isAnnounceUp()) {
				announceUp();
			}
			System.out.println("Registering the listeners");
			getApi().addEventListener(new MessageListener());
			getApi().addEventListener(new LeaveListener());
			System.out.println("We up");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected static void announceDown() {
		getOrCreateChannel(ChannelsEnum.ANNOUNCEMENTS).sendMessage("<@&" + getOrCreateRole(RolesEnum.ANNOUNCEMENTS).getIdLong() + "> the bot is now down.").complete();
		getApi().shutdownNow();
	}

	private static void registerPredefinedServices(Long botDiscId) {
		int degenId;
		try {
			degenId = DatabaseDegens.getDegenId(botDiscId);
			for(PredefinedServicesEnum service : PredefinedServicesEnum.values()) {
				DatabaseDegens.createPredService(service.getName(), service.getFarms(), service.getDurationHours(), service.getRate(), degenId,FormattingUtils.parseAmount(service.getPrice()),service.getLevel());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void initChannels() {
		for(ChannelsEnum channel : ChannelsEnum.values()) {
			if(!channel.equals(ChannelsEnum.ALL)) {
				getOrCreateChannel(channel);
			}
		}
	}

	public static void initProperties(String file) throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new FileInputStream(new File(file)));
	}
	
	public static TextChannel getOrCreateChannel(String name, Category category, int slowmode, RolesEnum[] permittedRoles, RolesEnum[] readOnlyRoles){
		for(TextChannel channel : getGuild().getTextChannels()) {
			if(channel.getName().equalsIgnoreCase(name)) {
				return channel;
			}
		}
		TextChannel createdChannel = getGuild().createTextChannel(name, category).setSlowmode(slowmode).complete();
		createdChannel.putPermissionOverride(getOrCreateRole(RolesEnum.EVERYONE)).deny(Permission.VIEW_CHANNEL).complete();
		for(RolesEnum role : permittedRoles) {
			createdChannel.putPermissionOverride(getOrCreateRole(role)).setAllow(Permission.VIEW_CHANNEL).complete();
		}
		for(RolesEnum role : readOnlyRoles) {
			createdChannel.putPermissionOverride(getOrCreateRole(role)).deny(Permission.MESSAGE_WRITE).setAllow(Permission.VIEW_CHANNEL).complete();
		}
		return createdChannel;
	}
	
	public static TextChannel getOrCreateChannel(ChannelsEnum channel){
		return getOrCreateChannel(channel.getName(),getOrCreateCategory(channel.getCategory()),channel.getSlowmode(),channel.getPermitedRoles(),channel.getReadOnlyRoles());
	}


	public static Category getOrCreateCategory(CategoriesEnum category) {
		return getOrCreateCategory(category.getName(), category.getPermitedRoles());
	}

	public static Category getOrCreateCategory(String name, RolesEnum[] permittedRoles) {
		for(Category category : getGuild().getCategories()) {
			if(category.getName().equalsIgnoreCase(name)) {
				return category;
			}
		}
		Category createdCategory = getGuild().createCategory(name).complete();
		createdCategory.putPermissionOverride(getOrCreateRole(RolesEnum.EVERYONE)).deny(Permission.VIEW_CHANNEL).complete();
		for(RolesEnum role : permittedRoles) {
			createdCategory.putPermissionOverride(getOrCreateRole(role)).setAllow(Permission.VIEW_CHANNEL).complete();
		}
		return createdCategory;
	}
	
	public static Role getOrCreateRole(RolesEnum degen) {
		for(Role role : getGuild().getRoles()) {
			if(role.getName().equalsIgnoreCase(degen.getName())) {
				return role;
			}
		}
		return getGuild().createRole().setName(degen.getName()).complete();
	}
	
	private static String getGuildId() {
		return props.getProperty("guild-id");
	}

	@SuppressWarnings("unchecked")
	private static void clearAndRestartServices() throws InterruptedException, SQLException {
		TextChannel servicesShopChannel = getOrCreateChannel(ChannelsEnum.SERVICESSHOP);
		{
			TextChannel bidServicesChannel = getOrCreateChannel(ChannelsEnum.BIDSERVICE);
			TextChannel blindBidServicesChannel = getOrCreateChannel(ChannelsEnum.BLINDBIDSERVICE);
			TextChannel highLowChannel = getOrCreateChannel(ChannelsEnum.HIGHLOW);
			purgeChannel(bidServicesChannel);
			purgeChannel(blindBidServicesChannel);
			purgeChannel(highLowChannel);
			purgeChannel(servicesShopChannel);
		}
		
		Category myServicesCat = getOrCreateCategory(CategoriesEnum.MYSERVICES);
		//Delete all channels in "my-services"
		myServicesCat.getTextChannels().forEach(a -> a.delete().complete());
		
		//Get all "running" services
		PagedMessageBean pagedServiceShop = MessageUtils.getShopDetails(DatabaseDegens.getPredServices());
		while(pagedServiceShop.isMoreRecords()) {
			servicesShopChannel.sendMessage(pagedServiceShop.getMessage()).allowedMentions(new ArrayList<>()).complete();
			pagedServiceShop = MessageUtils.getShopDetails((List<ItemBean>)(Object)pagedServiceShop.getLeftOverRecords());
		}
		servicesShopChannel.sendMessage(pagedServiceShop.getMessage()).allowedMentions(new ArrayList<>()).queue();
		
		
		List<ServiceBean> runningServices = DatabaseDegens.getRunningServices();
		for(ServiceBean runningService : runningServices) {
			Member member = getGuild().getMemberById(runningService.getOwnerDiscordId());
			if(null!=member) {
				Long timeToRun = (long) (1000 * 60 * 60 * runningService.getLength());
				Long expireTime = timeToRun + System.currentTimeMillis();
				TextChannel createdChannel = myServicesCat.createTextChannel(runningService.getServiceId() + "-" + runningService.getName() + "-" + runningService.getOwnerName()).complete();
				 
				createdChannel.putPermissionOverride(getOrCreateRole(RolesEnum.EVERYONE)).deny(Permission.VIEW_CHANNEL).complete();
				createdChannel.putPermissionOverride(getOrCreateRole(RolesEnum.DEGEN)).deny(Permission.VIEW_CHANNEL).complete();
				createdChannel.putPermissionOverride(getOrCreateRole(RolesEnum.IRONMAN)).deny(Permission.VIEW_CHANNEL).complete();
				createdChannel.putPermissionOverride(member).setAllow(Permission.VIEW_CHANNEL).complete();
				
				StringBuilder greeting = new StringBuilder();
				greeting.append(runningService.info())
				.append(" You can check the status at any time using !status");
				createdChannel.sendMessage(greeting.toString()).allowedMentions(new ArrayList<>()).complete();
				
				ServiceListener sl = new ServiceListener(createdChannel.getIdLong(), expireTime, runningService.getServiceId());
				Service serviceTask = new Service(runningService.getOwnerDiscordId(),
						runningService.getName(),
						runningService.getFarms(),
						createdChannel.getIdLong(),
						true,
						runningService.getServiceId(),
						sl,
						expireTime,
						runningService.getType());
				
				Timer serviceTimer = new Timer("Service-" + runningService.getServiceId() + "Timer");
				serviceTimer.schedule(serviceTask, 0L, (1000 * 60 * runningService.getInterval()));
				
				Timer kstTimer = new Timer("KillService-" + runningService.getServiceId() + "Timer");
				KillServiceTask kst = new KillServiceTask(serviceTask);
				kstTimer.schedule(kst, new Date(expireTime));
				
				ServicesUtils.addCancellableService(runningService.getServiceId(), new ServiceTimerTaskPair(kstTimer,kst));
			}else {
				deactivateDegen(runningService.getOwnerDiscordId());
			}
		}
		//We create the new services service
		ServicesUtils.restoreBiddableServices();
	}

	public static void deleteDegen(Long discordId) {
		if(deactivateDegen(discordId)) {
			try {
				DatabaseDegens.deleteDegen(discordId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static boolean deactivateDegen(Long discordId) {
		try {
			if(DatabaseDegens.existsById(discordId)) {
				int degenId = DatabaseDegens.getDegenId(discordId);
				boolean deactivated = deactivateDegen(degenId);
				if(deactivated) {
					if(getGuild().getMemberById(discordId)!=null) {
						for(Role role : getGuild().getMemberById(discordId).getRoles()) {
							if(RolesEnum.isRemovable(role.getName())) {
								getGuild().removeRoleFromMember(discordId, role).queue();
							}
						}
					}
					for(int serviceId : DatabaseDegens.getDegenActiveServicesId(degenId)) {
						ServicesUtils.stopService(serviceId);
					}
				}
				
				return deactivated; 
			}else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deactivateDegen(int degenId) {
		try {
			return DatabaseDegens.deactivateDegen(degenId);
		}catch(SQLException e) {
			e.printStackTrace();
			return false;
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
		getOrCreateChannel(ChannelsEnum.ANNOUNCEMENTS).sendMessage("<@&" + getOrCreateRole(RolesEnum.ANNOUNCEMENTS).getIdLong() + "> the bot is now up. !degen if you can't see channels.").queue();
	}
	
	public static RegisterResultEnum register(long id, String name, boolean iron) {
		RegisterResultEnum result = RegisterResultEnum.FAIL;
		try {
			if(DatabaseDegens.existsById(id)) {
				result = RegisterResultEnum.ALREADY_ACTIVE;
			}else {
				DegenBean degen = DatabaseDegens.getDegen(id);
				if(degen!=null) {
					int degenId = degen.getDegenId();
					if(iron && !degen.isIronman()) {
						result = RegisterResultEnum.FAIL_NORMIE_TO_IRON;
					}else {
						int insertResult = DatabaseDegens.insertNewDegen(id,name,iron);
						if(insertResult>1) {
							ServiceBean predService = DatabaseDegens.selectDegenPredService(degenId);
							if(predService!=null) {
								if(ServicesUtils.canActivateService(id)) {
									ServicesUtils.activateService(predService);
								}
							}
							result = RegisterResultEnum.REACTIVATE;
						}
					}
				}else {
					if(DatabaseDegens.insertNewDegen(id,name,iron)>=1) {
						result =  RegisterResultEnum.SUCCESS;
					}
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static boolean isRole(Member member, String roleName) {
		for(Role memberRole: member.getRoles()) {
			if(memberRole.getName().equalsIgnoreCase(roleName)){
				return true;
			}
		}
		return false;
	}

	private static boolean isAnnounceUp() {
		return Boolean.valueOf(props.getProperty("announce-up"));
	}

	private static String getBotToken() {
		return props.getProperty("token");
	}

	public static String getDBUrl() {
		return props.getProperty("db.url");
	}

	public static String getDbUsername() {
		return props.getProperty("db.username");
	}

	public static String getDbPassword() {
		return props.getProperty("db.password");
	}

	public static Long getBotId() {
		return getApi().getSelfUser().getIdLong();
	}

	public static LevelUpResultBean levelUp(long idLong, LevelBean nextLevel) {
		LevelUpResultBean result = new LevelUpResultBean();
		try {
			Long funds = DatabaseDegens.getFundsById(idLong);
			if(funds>=nextLevel.getCost()) {
				DatabaseDegens.updateFundsSum(-nextLevel.getCost(), DatabaseDegens.getDegenId(idLong));
				DatabaseDegens.updateLevel(idLong);
				if(nextLevel.isFreeService()) {
					int serviceLevel = nextLevel.getLevel() + 1;
					String serviceName = RandomUtils.randomStringFromArray(RehabBotConstants.SERVICE_NAMES);
					
					int maxFarms = serviceLevel > 18 ? 8 : serviceLevel > 12 ? 7 : serviceLevel > 8 ? 6 : serviceLevel > 4 ? 5 : 4;
					
					int farmsFarmed = RandomUtils.randomInt(Math.toIntExact(Math.round(maxFarms*1.5)));
					
					double rateHour = Double.parseDouble(String.format("%.1f",RandomUtils.randomDouble(3.0, maxFarms)));
					
					int intervalLowerBound = serviceLevel > 15 ? 1 : 2;  
					
					int interval = RandomUtils.randomInt(6 - intervalLowerBound) + intervalLowerBound;
					if(interval>farmsFarmed) {
						farmsFarmed = interval;
					}
					ServiceBean service = new ServiceBean(0, farmsFarmed, rateHour, serviceName, interval, false);
					
					DatabaseDegens.createService(serviceName, farmsFarmed, rateHour,interval, DatabaseDegens.getDegenId(idLong),3, serviceLevel);
					result.setService(service);
					result.setFreeService(true);
				}
				result.setNewLevel(nextLevel.getLevel() + 1);
				result.setLevelUp(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
