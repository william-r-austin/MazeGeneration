package maze;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {
	private List<GridSquare> gridSquares;
	
	public Room(GridSquare gridSquare) {
		gridSquares = new ArrayList<>();
		gridSquares.add(gridSquare);
	}
	
	public Room(Set<Room> oldRooms) {
		gridSquares = new ArrayList<>();
		
		for(Room oldRoom : oldRooms) {
			gridSquares.addAll(oldRoom.getGridSquares());
		}
	}
	
	public List<GridSquare> getGridSquares() {
		return gridSquares;
	}
	
	

}
