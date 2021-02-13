package art.cipher581.tools.af.element;

import java.awt.Color;
import java.awt.Graphics;


public class Rectangle extends AbstractElement {
	
	private int width;
	
	private int height;
	

	public Rectangle() {
		super();
	}
	
	
	public Rectangle(int x, int y, Color color, int width, int height) {
		super(x, y, color);

		this.width = width;
		this.height = height;
	}


	@Override
	public void paint(Graphics g) {
		Color originalColor = g.getColor();
		
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		g.setColor(originalColor);
	}

}
