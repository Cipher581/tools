package art.cipher581.tools.af.util;


import java.awt.Color;
import java.util.Random;


public class ColorGenerator {

	private static final Random RANDOM = new Random();


	public Color generateRandomColor() {
		int r = RANDOM.nextInt(256);
		int g = RANDOM.nextInt(256);
		int b = RANDOM.nextInt(256);
		int a = RANDOM.nextInt(256);

		return new Color(r, g, b, a);
	}
	
}
