package mx.fortson.rehab.tasks;

import java.util.TimerTask;

import mx.fortson.rehab.RehabBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoveListenerTask extends TimerTask{

	private final ListenerAdapter listener;	
	
	public RemoveListenerTask(ListenerAdapter listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		RehabBot.getApi().removeEventListener(this.listener);
	}

}
