package art.cipher581.tools.af.element.gen;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import art.cipher581.tools.af.IConfiguration;
import art.cipher581.tools.af.element.Point;
import art.cipher581.tools.af.element.Point.Dimension;
import art.cipher581.tools.af.element.Polygon;
import art.cipher581.tools.af.util.RandomUtilities;

public class PolygonGenerator extends AbstractRandomElementGenerator<Polygon> {

	public PolygonGenerator() {
		super();
	}

	public PolygonGenerator(IConfiguration configuration) {
		super(configuration);
	}

	@Override
	public Polygon generate(int imageWidth, int imageHeight) {
		int minPoints = getConfigValueAsInt("polygonMinPoints", 3);
		int maxPoints = getConfigValueAsInt("polygonMaxPoints", 10);
		int fillProbability = getConfigValueAsInt("polygonfillProbability", 80);
		int maxHeight = getConfigValueAsInt("polygonMaxHeight", -1);
		int maxWidth = getConfigValueAsInt("polygonMaxWidth", -1);
		
		int points = RandomUtilities.nextInt(minPoints, maxPoints);

		Color color = getRandomColor();
		boolean fill = RandomUtilities.nextBoolean(fillProbability);
		int thickness = RandomUtilities.nextInt(8, 60);

		Polygon polygon = new Polygon(color, fill, thickness);

		for (int i = 0; i < points; i++) {
			generatePoint(imageWidth, imageHeight, maxWidth, maxHeight, polygon);
		}

		return polygon;
	}


	private void generatePoint(int imageWidth, int imageHeight, int maxWidth, int maxHeight, Polygon polygon) {
		List<Point> points = new LinkedList<Point>(polygon.getPoints());

		int x;
		if (points.isEmpty() || maxWidth <= 0) {
			x = RandomUtilities.nextInt(-200, imageWidth + 200);
		} else {
			int minX = Point.getMin(points, Dimension.X);
			int maxX = Point.getMax(points, Dimension.X);

			int diffX = maxX - minX;
			
			int tolerance = maxWidth - diffX;

			x = RandomUtilities.nextInt(minX - tolerance, maxX + tolerance);
		}
		
		int y;
		if (points.isEmpty() || maxHeight <= 0) {
			y = RandomUtilities.nextInt(-200, imageHeight + 200);
		} else {
			int minY = Point.getMin(points, Dimension.Y);
			int maxY = Point.getMax(points, Dimension.Y);
			
			int diffY = maxY - minY;
			
			int tolerance = maxHeight - diffY;

			y = RandomUtilities.nextInt(minY - tolerance, maxY + tolerance);
		}

		Point point = new Point(x, y);
		
		polygon.addPoint(point);
	}
	
	@Override
	public String getName() {
		return "polygon";
	}

}
