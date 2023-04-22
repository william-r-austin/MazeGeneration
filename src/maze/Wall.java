package maze;

import java.util.Objects;

public class Wall {
	private int x;
	private int y;
	private Direction direction; // 0 = vertical, 1 = horizontal
	private boolean permanent;
	private boolean removed;
	private GridSquare primary;
	private GridSquare secondary;
			
	public Wall(int x, int y, Direction direction, boolean isPermanent) {
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

	public Direction getDirection() {
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

	public GridSquare getPrimary() {
		return primary;
	}

	public void setPrimary(GridSquare primary) {
		this.primary = primary;
	}

	public GridSquare getSecondary() {
		return secondary;
	}

	public void setSecondary(GridSquare secondary) {
		this.secondary = secondary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(direction, permanent, x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wall other = (Wall) obj;
		return direction == other.direction && permanent == other.permanent && x == other.x && y == other.y;
	}
}
