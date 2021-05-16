package mx.fortson.rehab.enums;

public enum SlotsEnum {

	BONE("\uD83E\uDDB4",20,.601), 
	CRAB("\uD83E\uDD80",500,.2),
	CLOVER("\uD83C\uDF40",1500,.15),
	MEDAL("\uD83C\uDFC5",5000,.1),
	GEM("\uD83D\uDC8E",25000,.08),
	SPOON("\uD83E\uDD44",50000,.05),
	;

	String unicodeEmoji;
	int payout;
	double weight;
	
	private SlotsEnum(String unicodeEmoji, int payout, double weight) {
		this.unicodeEmoji = unicodeEmoji;
		this.payout = payout;
		this.weight = weight;
	}
	
	public String getUnicodeEmoji() {
		return unicodeEmoji;
	}
	public int getPayout() {
		return payout;
	}
	public double getWeight() {
		return weight;
	}
	
	
}
