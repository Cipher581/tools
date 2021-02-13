package art.cipher581.tools.af.element;


import java.awt.Color;


public abstract class AbstractElement implements IElement {

	protected int x;

	protected int y;
	
	protected Color color;


	public AbstractElement() {
		super();
	}

	public AbstractElement(int x, int y, Color color) {
		super();
		
		this.x = x;
		this.y = y;
		this.color = color;
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
	
	
	public Color getColor() {
		return color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}

}
