package art.cipher581.tools.af.element;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class Line extends AbstractElement {

	private int x2;
	
	private int y2;
	
	private int thickness;
	

	public Line(int x, int y, Color color, int x2, int y2, int thickness) {
		super(x, y, color);

		this.x2 = x2;
		this.y2 = y2;
		this.thickness = thickness;
	}


	@Override
	public void paint(Graphics g) {
		Color originalColor = g.getColor();
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(color);
		g2.setStroke(new BasicStroke(thickness));
        g2.draw(new Line2D.Float(x, y, x2, y2));
		
		g.setColor(originalColor);
	}
	
}
