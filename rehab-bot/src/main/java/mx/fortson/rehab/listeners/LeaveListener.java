package mx.fortson.rehab.listeners;

import java.sql.SQLException;

import mx.fortson.rehab.DatabaseDegens;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveListener extends ListenerAdapter{

	@Override
	public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
		try {
			int degenId = DatabaseDegens.getDegenId(event.getUser().getIdLong());
			if(degenId!=0) {
				DatabaseDegens.deleteDegen(degenId);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}
