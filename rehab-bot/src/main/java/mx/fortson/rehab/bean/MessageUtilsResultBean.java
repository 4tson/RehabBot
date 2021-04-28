package mx.fortson.rehab.bean;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Message.MentionType;

public class MessageUtilsResultBean {
	
	private String message;

	private List<MentionType> pingList = new ArrayList<>();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<MentionType> getPingList() {
		return pingList;
	}

	public void setPingList(List<MentionType> pingList) {
		this.pingList = pingList;
	}
}
