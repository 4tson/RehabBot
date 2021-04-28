package mx.fortson.rehab.bean;

public class TransactionResultBean {

	private boolean sold;
	private String flavourText;
	private ItemBean itemSold;	
	private Long sellerDiscordId;
	private Long buyerDiscordId;
	
	public Long getSellerDiscordId() {
		return sellerDiscordId;
	}

	public void setSellerDiscordId(Long sellerDiscordId) {
		this.sellerDiscordId = sellerDiscordId;
	}

	public Long getBuyerDiscordId() {
		return buyerDiscordId;
	}

	public void setBuyerDiscordId(Long buyerDiscordId) {
		this.buyerDiscordId = buyerDiscordId;
	}

	public ItemBean getItemSold() {
		return itemSold;
	}

	public void setItemSold(ItemBean itemSold) {
		this.itemSold = itemSold;
	}

	public String getFlavourText() {
		return flavourText;
	}

	public void setFlavourText(String flavourText) {
		this.flavourText = flavourText;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}
	
}
