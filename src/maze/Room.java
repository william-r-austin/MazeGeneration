package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Room {
	private List<GridSquare> gridSquares;
	private Integer roomId;
	
	public Room(Integer roomId) {
		this.roomId = roomId;
		this.gridSquares = new ArrayList<>();
	}
	
	public Room(Integer roomId, GridSquare gridSquare) {
		this.roomId = roomId;
		this.gridSquares = new ArrayList<>();
		
		addToRoom(gridSquare);
	}
	
	public void addToRoom(GridSquare gridSquare) {
		gridSquare.setRoomId(roomId);
		gridSquares.add(gridSquare);
	}
	
	public void merge(Room otherRoom) {
		for(GridSquare gridSquare : otherRoom.getGridSquares()) {
			addToRoom(gridSquare);
		}

		otherRoom.clearRoom();
	}
	
	public void clearRoom() {
		gridSquares.clear();
	}

	public int size() {
		return gridSquares.size();
	}
	
	public List<GridSquare> getGridSquares() {
		return gridSquares;
	}

	@Override
	public int hashCode() {
		return Objects.hash(roomId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return Objects.equals(roomId, other.roomId);
	}
}
