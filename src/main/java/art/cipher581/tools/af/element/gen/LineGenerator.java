package art.cipher581.tools.af.element.gen;


import java.awt.Color;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.Line;
import art.cipher581.tools.af.util.RandomUtilities;


public class LineGenerator extends AbstractRandomElementGenerator<Line> {

	public LineGenerator() {
		super();
	}

	public LineGenerator(IConfiguration configuration) {
		super(configuration);
	}


	@Override
	public Line generate(int imageWidth, int imageHeight) {
		int maxThickness = getConfigValueAsInt("lineMaxThickness", 20);
		int minThickness = getConfigValueAsInt("lineMinThickness", 4);

		int x = RandomUtilities.nextInt(-maxThickness, imageWidth + maxThickness);
		int y = RandomUtilities.nextInt(-maxThickness, imageHeight + maxThickness);
		int x2 = RandomUtilities.nextInt(-maxThickness, imageWidth + maxThickness);
		int y2 = RandomUtilities.nextInt(-maxThickness, imageWidth + maxThickness);
		
		Color color = getRandomColor();
		
		int thickness = RandomUtilities.nextInt(minThickness, maxThickness);
		
		return new Line(x, y, color, x2, y2, thickness);
	}
	
	
	@Override
	public String getName() {
		return "line";
	}

}
