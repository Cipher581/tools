package art.cipher581.tools.af.element;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import art.cipher581.tools.af.element.Point.Dimension;


public class Polygon implements IElement {

	private List<Point> points = new LinkedList<Point>();

	private Color color;
	
	private boolean fill;
	
	private int thickness;


	public Polygon() {
		super();
	}


	public Polygon(Color color, boolean fill, int thickness) {
		super();

		this.color = color;
		this.fill = fill;
		this.thickness = thickness;
	}


	public Polygon(List<Point> points, boolean fill, int thickness, Color color) {
		super();

		this.points = points;
		this.fill = fill;
		this.color = color;
	}


	public void addPoint(Point point) {
		this.points.add(point);
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public List<Point> getPoints() {
		return points;
	}


	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	
	public boolean isFill() {
		return fill;
	}
	
	
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	
	public int getThickness() {
		return thickness;
	}
	
	
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}


	@Override
	public void paint(Graphics g) {
		Color originalColor = g.getColor();

		g.setColor(color);

		java.awt.Polygon polygon = new java.awt.Polygon(getArray(Dimension.X), getArray(Dimension.Y), points.size());

		if (fill) {
			g.fillPolygon(polygon);
		} else {
			Graphics2D g2 = (Graphics2D) g;

			BasicStroke s = new BasicStroke(thickness);
			
			g2.setStroke(s);
			g2.drawPolygon(polygon);
		}

		g.setColor(originalColor);
	}


	private int[] getArray(Dimension d) {
		int[] arr = new int[points.size()];

		int i = 0;
		for (Point point : points) {
			arr[i] = point.get(d);
			i++;
		}

		return arr;
	}


	public int getMinX() {
		return Point.getMin(points, Dimension.X);
	}
	
	
	public int getMaxX() {
		return Point.getMax(points, Dimension.X);
	}
	
	
	public int getMinY() {
		return Point.getMin(points, Dimension.Y);
	}
	
	
	public int getMaxY() {
		return Point.getMax(points, Dimension.Y);
	}


	public int getHeight() {
		if (points.isEmpty()) {
			return 0;
		}

		return getMaxY() - getMinY();
	}
	
	
	public int getWidth() {
		if (points.isEmpty()) {
			return 0;
		}

		return getMaxX() - getMinX();
	}

}
