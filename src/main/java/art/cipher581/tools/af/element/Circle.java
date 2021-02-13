package art.cipher581.tools.af.element;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Circle extends AbstractElement {

	private int radius;

	private boolean fill = true;

	private int thickness;


	public Circle() {
		super();
	}


	public Circle(int x, int y, Color color, int radius) {
		super(x, y, color);

		this.radius = radius;
	}


	public Circle(int x, int y, Color color, int radius, boolean fill, int thickness) {
		super(x, y, color);
		this.radius = radius;
		this.fill = fill;
		this.thickness = thickness;
	}


	@Override
	public void paint(Graphics g) {
		int d = radius * 2;

		Color originalColor = g.getColor();

		g.setColor(color);

		if (fill) {
			g.fillOval(x, y, d, d);
		} else {
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setStroke(new BasicStroke(thickness));
			
			int curX = x;
			int curY = y;

			g2.drawOval(curX, curY, d, d);
		}

		g.setColor(originalColor);
	}

}
