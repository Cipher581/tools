package art.cipher581.common.color;

public class Position {

	private int x;
	
	private int y;

	public Position(int x, int y) {
		super();

		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getPrintString() {
		return "(" + (x + 1) + ", " + (y + 1) + ")";
	}

	public static int compareByX(Position a, Position b) {
		return Integer.compare(a.x, b.x);
	}
	
}
