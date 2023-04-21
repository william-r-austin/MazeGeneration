package maze;

public class Wall {
	private int x;
	private int y;
	private int direction; // 0 = vertical, 1 = horizontal
	private boolean permanent;
	private boolean removed;
		
	public Wall(int x, int y, int direction, boolean isPermanent) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.permanent = isPermanent;
		this.removed = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setRemoved() {
		removed = true; 
	}
	
	public boolean isRemoved() {
		return removed;
	}
}
