package maze;

import java.util.Objects;

public class Wall {
	private int x;
	private int y;
	private Direction direction;
	private boolean permanent;
	private boolean removed;
	
	// A wall can border 2 grid squares. The 'permanent' walls on the outside of the maze will
	// only border one grid square.
	// 
	// For VERTICAL WALLS:
	// - The primary grid square is the one to the RIGHT of the wall.
	// - The secondary grid square is the one to the LEFT of the wall.
	//
	// FOR HORIZONTAL WALLS:
	// - The primary grid square is the one BELOW the wall.
	// - The secondary grid square is the one ABOVE the wall.
	//
	// Note that the x, y coordinates for the wall will always match the x, y coordinates for the
	// primary grid square.
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
