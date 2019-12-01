package data;

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

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	// position 对象之间的减法
	public static Position subtract(Position a, Position b) {
		return new Position(a.getX() - b.getX(), a.getY() - b.getY());
	}

	// position 对象之间的减相等
	public static boolean isEqual(Position a, Position b) {
		return a.getX() == b.getX() && a.getY() == b.getY();
	}
}
