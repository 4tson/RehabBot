package mx.fortson.rehab.enums;

public enum FarmResultEnum {

	RANDO_PITY(new String[]{
			"A random player took pity in you, and gave you `{amount}`.",
			"You found `{amount}` on the ground, so lucky!",
			"Jagex bug resulted in you getting `{amount}` in your bank."
			}, 
			new Long[]{1000000L,25000000L},
			0.5,
			FarmTypeEnum.CASH),
	CLUE_GOOD(new String[]{
			"You completed a Hard clue for `{amount}`",
			"You completed a Medium clue for `{amount}`",
			"You completed a Master clue for `{amount}`"
			}, 
			new Long[]{1413576L,47131015L},
			0.31,
			FarmTypeEnum.CASH),
	CLUE_GREAT(new String[]{
			"You hit the super rare table on a master for `{amount}`"
			}, 
			new Long[]{71642172L,2127668291L},
			0.001,
			FarmTypeEnum.CASH),
	GIFT_CHUCK(new String[]{
			"A new sugar daddy is in town. You get a `{amount}` gift chuck.",
			"Rehabbers pool a cheeky `{amount}` gift chuck for you.",
			"We feel bad for you, the bot gives you a `{amount}` gift chuck."
			}, 
			new Long[]{15000000L,500000000L},
			0.1,
			FarmTypeEnum.CASH),
	COX_DROP(new String[]{
			"You decide to go back to CoX, as if thats going to help. Wait... is that a purple? Your loot is worth `{amount}`"
			}, 
			new Long[]{7699443L,1020160391L},
			0.022,
			FarmTypeEnum.CASH),
	TOB_DROP(new String[]{
			"Maybe ToB is going to be good today. 416, LFR, sigh... What's the difference? Your loot is worth `{amount}`"
			}, 
			new Long[]{7156790L,564115278L},
			0.022,
			FarmTypeEnum.CASH),
	ITEM_MEH(new String[]{
			"You go do some PVM as a good reformed kid. Neat, this is worth `{amount}`",
			"You go PKing. Why did someone risk something worth `{amount}`?",
			"Cool, an item on the ground. Almost not worth bending over for `{amount}`",
			"Falador party room never dissapoints. It's worth `{amount}`!"
			}, 
			new Long[]{1000000L,20000000L},
			0.0077,
			FarmTypeEnum.ITEM_MEH),
	ITEM_GREAT(new String[]{
			"See, these are the kind of drops I'm talking about! `{amount}`!!!",
			"ashdpqiudqpweifupoqifpqeoifpqepk no way! `{amount}`!!!!!!!",
			"Okay this is the 4th time I've seen this flavour text, surely this is very common.... `{amount}` no complaints from me though.",
			"BEHEMOOOOOOOOOOOOOOOOOOOOOOOOOOOOOTH. `{amount}`"
			}, 
			new Long[]{100000000L,2000000000L},
			0.001,
			FarmTypeEnum.ITEM_GREAT),
	ITEM_UNIQUE(new String[]{
			"What even is this? There's no way right? Seems too expensive..."
			}, 
			new Long[]{10000000000L,20000000000L},
			0.0001,
			FarmTypeEnum.ITEM_UNIQUE),
	SERVICE(new String[]{
			"This might be useful.",
			"Maybe you can get some farms from this.",
			"Now this is what I call farming.",
			"The possibilities are endless."
			}, 
			new Long[]{73L,73L},
			0.002,
			FarmTypeEnum.SERVICE),
	;
	
	String[] flavourTexts;
	Long[] amountRange;
	double weight;
	FarmTypeEnum type;
	
	private FarmResultEnum(String[] flavourTexts, Long[] amountRange, double weight, FarmTypeEnum type) {
		this.flavourTexts = flavourTexts;
		this.amountRange = amountRange;
		this.weight = weight;
		this.type = type;
	}

	public FarmTypeEnum getType() {
		return type;
	}

	public double getWeight() {
		return weight;
	}

	public String[] getFlavourTexts() {
		return flavourTexts;
	}

	public Long[] getAmountRange() {
		return amountRange;
	}
}
