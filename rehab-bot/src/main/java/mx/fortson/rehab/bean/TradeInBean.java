package mx.fortson.rehab.bean;

public class TradeInBean {

	private int itemCount;
	private long value;
	private int type;
	public TradeInBean(int itemCount, long value, int type) {
		super();
		this.itemCount = itemCount;
		this.value = value;
		this.type = type;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public long getValue() {
		return value;
	}
	public void setValue(long value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}		
}
