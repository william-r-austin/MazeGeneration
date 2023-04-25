package maze;

import java.util.EnumMap;
import java.util.Objects;

public class GridSquare {
	private int x;
	private int y;
	private Integer roomId;

	// Every GridSquare is surrounded by 4 walls, in each of the 4 directions
	//
	// Note that the walls in the 'Left' and 'Up' directions share (x, y) 
	// coordinates with this grid square.
	private EnumMap<Direction, Wall> adjacentWallsMap;
	
	public GridSquare(int x, int y, Integer roomId) {
		this.x = x;
		this.y = y;
		this.roomId = roomId;
		this.adjacentWallsMap = new EnumMap<>(Direction.class);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Integer getRoomId() {
		return roomId;
	}
	
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	
	public void setWall(Direction direction, Wall wall) {
		adjacentWallsMap.put(direction, wall);
	}
	
	public Wall getWall(Direction direction) {
		return adjacentWallsMap.get(direction);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GridSquare other = (GridSquare) obj;
		return x == other.x && y == other.y;
	}
}
