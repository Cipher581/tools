package art.cipher581.tools.af.element.gen;

import java.awt.Color;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.Circle;
import art.cipher581.tools.af.util.RandomUtilities;

public class CircleGenerator extends AbstractRandomElementGenerator<Circle> {

	public CircleGenerator(IConfiguration configuration) {
		super(configuration);
	}

	private int getMaxRadius(int imageWidth, int imageHeight) {
		int defaultMaxRadius = (int) (Math.max(imageWidth, imageHeight) * 0.33);

		return getConfigValueAsInt("circleMaxRadius", defaultMaxRadius);
	}
	
	
	private int getMinRadius(int maxRadius) {
		int defaultMinRadius = (int) (maxRadius * 0.33);

		return getConfigValueAsInt("circleMinRadius", defaultMinRadius);
	}
	

	@Override
	public Circle generate(int imageWidth, int imageHeight) {
		int maxRadius = getMaxRadius(imageWidth, imageHeight);
		int minRadius = getMinRadius(maxRadius);
		int fillProbability = getConfigValueAsInt("circlefillProbability", 50);
		int minThickness = getConfigValueAsInt("circleMinThickness", 10);
		int maxThickness = getConfigValueAsInt("circleMaxThickness", (int) (maxRadius * 0.9));

		int x = RandomUtilities.nextInt(-maxRadius * 2, imageWidth);
		int y = RandomUtilities.nextInt(-maxRadius * 2, imageHeight);
		boolean fill = RandomUtilities.nextBoolean(fillProbability);

		Color color = getRandomColor();

		int radius = RandomUtilities.nextInt(minRadius, maxRadius);
		int thickness = RandomUtilities.nextInt(minThickness, maxThickness);

		return new Circle(x, y, color, radius, fill, thickness);
	}
	
	@Override
	public String getName() {
		return "circle";
	}

}
