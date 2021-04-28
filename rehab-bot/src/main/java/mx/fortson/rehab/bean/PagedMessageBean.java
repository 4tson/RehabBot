package mx.fortson.rehab.bean;

import java.util.List;

public class PagedMessageBean {

	private boolean moreRecords;
	private List<?> leftOverRecords;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isMoreRecords() {
		return moreRecords;
	}
	public void setMoreRecords(boolean moreRecords) {
		this.moreRecords = moreRecords;
	}
	public List<?> getLeftOverRecords() {
		return leftOverRecords;
	}
	public void setLeftOverRecords(List<?> leftOverRecords) {
		this.leftOverRecords = leftOverRecords;
	}	
}
