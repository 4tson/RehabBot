package mx.fortson.rehab.bean;

public class ItemBean {

	private Long itemID;
	private String itemName;
	private Long price;
	private String ownerName;
	private Long ownerDiscordId;
	private Long value;
	private String activeStr = "N/A";
	
	private int degenID;
	private boolean forSale;
	
	public String getActiveStr() {
		return activeStr;
	}
	public void setActiveStr(String activeStr) {
		this.activeStr = activeStr;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public Long getOwnerDiscordId() {
		return ownerDiscordId;
	}
	public void setOwnerDiscordId(Long ownerDiscordId) {
		this.ownerDiscordId = ownerDiscordId;
	}
	public int getDegenID() {
		return degenID;
	}
	public void setDegenID(int degenID) {
		this.degenID = degenID;
	}
	public boolean isForSale() {
		return forSale;
	}
	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}
	public Long getItemID() {
		return itemID;
	}
	public void setItemID(Long itemID) {
		this.itemID = itemID;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
}
