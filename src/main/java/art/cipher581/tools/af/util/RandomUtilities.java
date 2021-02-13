package art.cipher581.tools.af.util;


import java.util.Random;


public class RandomUtilities {

	private static final Random RANDOM = new Random(System.currentTimeMillis());


	public static int nextInt(int min, int max) {
		return min + RANDOM.nextInt(max - min);
	}


	public static int nextInt(int bound) {
		return RANDOM.nextInt(bound);
	}


	public static int nextInt() {
		return RANDOM.nextInt();
	}


	public static boolean nextBoolean() {
		return RANDOM.nextBoolean();
	}
	
	
	public static boolean nextBoolean(int probability) {
		if (probability > 100) {
			throw new IllegalArgumentException("probability must not be greater than 100");
		}
		if (probability < 0) {
			throw new IllegalArgumentException("probability must not be negative");
		}
		
		if (probability == 0) {
			return false;
		}
		
		int r = nextInt(100);
		
		return r <= probability;
	}

}
