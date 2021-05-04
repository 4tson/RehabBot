package mx.fortson.rehab.bean;

import java.util.List;

public class PagedMessageBean extends MessageUtilsResultBean{

	private boolean moreRecords;
	private List<?> leftOverRecords;
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
