package mx.fortson.rehab.utils;

import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import mx.fortson.rehab.enums.SuffixEnum;

public class FormattingUtils {

	private static final NavigableMap<Long, String> suffixes = new TreeMap<> ();
	static {
	  suffixes.put(1_000L, "k gp");
	  suffixes.put(1_000_000L, "M gp");
	  suffixes.put(1_000_000_000L, "B gp");
	  suffixes.put(1_000_000_000_000L, "T gp");
	  suffixes.put(1_000_000_000_000_000L, "P gp");
	  suffixes.put(1_000_000_000_000_000_000L, "E gp");
	}

	public static String format(long value) {
	  //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
	  if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
	  if (value < 0) return "-" + format(-value);
	  if (value < 1000) return Long.toString(value) + " gp"; //deal with easy case

	  Entry<Long, String> e = suffixes.floorEntry(value);
	  Long divideBy = e.getKey();
	  String suffix = e.getValue();

	  long truncated = value / (divideBy / 10); //the number part of the output times 10
	  boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
	  return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
	}

	public static String formatFarm(String flavourText, Long farmedAmount) {
		return flavourText.replace("{amount}", format(farmedAmount));
	}

	public static boolean isValidAmount(String amount) {
		if(StringUtils.isNumeric(amount) 
				|| amount.matches("(^((\\d*\\.?\\d{1,3})(k|K))|^((\\d*\\.?\\d{1,6})(m|M))|^((\\d*\\.?\\d{1,9})(b|B)))")) {
			return true;
		}
		return false;
	}

	public static Long parseAmount(String amount) {
		Long result = 0L;
		if(StringUtils.isNumeric(amount)){
			result = Long.parseLong(amount);
		}else if(amount.equals("allin")){
			result = -1L;
		}else {
			String suffix = amount.substring(amount.length()-1, amount.length());
			SuffixEnum suffixEnum = SuffixEnum.fromSuffix(suffix);
			Double amountDouble = Double.parseDouble(amount.substring(0, amount.length()-1));
			result = (long) (amountDouble * suffixEnum.getBase());
		}
		return result;
	}

	public static String tableFormat(String content, int size) {
		return StringUtils.center(content, size, " ");
	}
}
