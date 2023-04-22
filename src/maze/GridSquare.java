package maze;

import java.util.Objects;

public class GridSquare {
	private int x;
	private int y;
	private Integer roomId;
	private Wall[] adjacentWalls;
	
	public GridSquare(int x, int y, Integer roomId) {
		this.x = x;
		this.y = y;
		this.roomId = roomId;
		this.adjacentWalls = new Wall[4];
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
	
	public void setWall(int wallId, Wall wall) {
		adjacentWalls[wallId] = wall;
	}
	
	public Wall getWall(int wallId) {
		return adjacentWalls[wallId];
	}
	
	public Wall[] getAdjacentWalls() {
		return adjacentWalls;
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
