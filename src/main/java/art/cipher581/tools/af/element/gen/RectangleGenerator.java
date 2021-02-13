package art.cipher581.tools.af.element.gen;

import java.awt.Color;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.Rectangle;
import art.cipher581.tools.af.util.RandomUtilities;

public class RectangleGenerator extends AbstractRandomElementGenerator<Rectangle> {

	public RectangleGenerator() {
		super();
	}

	public RectangleGenerator(IConfiguration configuration) {
		super(configuration);
	}

	@Override
	public Rectangle generate(int imageWidth, int imageHeight) {
		int x = RandomUtilities.nextInt(-200, imageWidth + 200);
		int y = RandomUtilities.nextInt(-200, imageHeight + 200);

		Color color = getRandomColor();

		int width = RandomUtilities.nextInt(imageWidth / 2);
		int height = RandomUtilities.nextInt(imageHeight / 2);

		return new Rectangle(x, y, color, width, height);
	}

	@Override
	public String getName() {
		return "rectangle";
	}

}
