package art.cipher581.tools.af.element;

import java.util.List;


public class Point {

	public enum Dimension {
		X, Y
	}

	private int x;

	private int y;

	public Point() {
		super();
	}

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int get(Dimension d) {
		if (d == null) {
			throw new IllegalArgumentException("d is null"); //$NON-NLS-1$
		}

		if (d.equals(Dimension.X)) {
			return x;
		} else {
			return y;
		}
	}

	public static int getMin(List<Point> points, Dimension dimension) {
		if (points == null) {
			throw new IllegalArgumentException("points is null");
		}
		if (points.isEmpty()) {
			throw new IllegalArgumentException("points is empty");
		}

		int min = Integer.MAX_VALUE;

		for (Point point : points) {
			int v = point.get(dimension);

			if (v < min) {
				min = v;
			}
		}

		return min;
	}

	public static int getMax(List<Point> points, Dimension dimension) {
		if (points == null) {
			throw new IllegalArgumentException("points is null");
		}
		if (points.isEmpty()) {
			throw new IllegalArgumentException("points is empty");
		}

		int max = Integer.MIN_VALUE;

		for (Point point : points) {
			int v = point.get(dimension);

			if (v > max) {
				max = v;
			}
		}

		return max;
	}

}
