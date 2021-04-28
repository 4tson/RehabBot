package mx.fortson.rehab.enums;

public enum SuffixEnum {

	THOUSANDS("K",1000L),
	MILLIONS("M",1000000L),
	BILLIONS("B",1000000000L),
	;
	
	String suffix;
	Long base;
	
	private SuffixEnum(String suffix, Long base) {
		this.suffix = suffix;
		this.base = base;
	}
	

	public String getSuffix() {
		return suffix;
	}


	public Long getBase() {
		return base;
	}


	public static SuffixEnum fromSuffix(String suffix) {
		for(SuffixEnum value : SuffixEnum.values()) {
			if(value.getSuffix().equalsIgnoreCase(suffix)) {
				return value;
			}
		}
		return null;
	}
	
	
}
