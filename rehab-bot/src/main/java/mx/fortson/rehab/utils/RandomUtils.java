package mx.fortson.rehab.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import mx.fortson.rehab.enums.FarmResultEnum;

public class RandomUtils {
	
	private static final SecureRandom RANDOM = new SecureRandom();
	
	public static int randomInt(int size) {
		return RANDOM.nextInt(size) + 1;
	}
	
	public static double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
	
	public static Long randomAmountFromRange(Long[] amountRange) {
		if(amountRange[0] == amountRange[1]) {
			return amountRange[1];
		}
		Long win = ThreadLocalRandom.current().nextLong(amountRange[0], amountRange[1]);
		return win;
	}
	
	public static String randomStringFromArray(String[] possibleStrings) {
		int x = RANDOM.nextInt(possibleStrings.length);
        return possibleStrings[x];
	}

	public static int randomFreeFarm() {
		return RANDOM.nextInt(3) + 1;
	}
	
	public static FarmResultEnum randomFarmEnum()  {
		final List<Pair<FarmResultEnum, Double>> itemWeights = new ArrayList<>();
		for (FarmResultEnum i: FarmResultEnum.values()) {
		    itemWeights.add(new Pair<FarmResultEnum, Double>(i, i.getWeight()));
		}
		return new EnumeratedDistribution<>(itemWeights).sample();
    }
}
