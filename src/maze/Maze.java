package maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Maze {
	private static final int DEFAULT_PRINT_WIDTH = 4;
	private static final int DEFAULT_PRINT_HEIGHT = 2;
	private static final char DEFAULT_WALL_CHAR = '#';
	
	private final int mazeWidth;
	private final int mazeHeight;
	
	private GridSquare[][] allGridSquares;
	private Wall[][][] allWalls;
	private List<Wall> remainingWalls;
	private Map<Integer, Room> allRoomsMap;
	
	public Maze(int width, int height) {
		this.mazeWidth = width;
		this.mazeHeight = height;
		
		this.allGridSquares = new GridSquare[mazeWidth][mazeHeight];

		int numOrientations = Orientation.size();
		this.allWalls = new Wall[mazeWidth + 1][mazeHeight+1][numOrientations];

		this.remainingWalls = new ArrayList<>();
		this.allRoomsMap = new HashMap<>();
		
		// Initialize the maze to set up all data structures
		initializeMaze();
		
		// Randomly remove walls until the maze is generated
		createRandomMaze();
	}
	
	private void initializeMaze() {
		Integer currentRoomId = 0;
		
		// Create all grid squares.
		for(int i = 0; i < mazeWidth; i++) {
			for(int j = 0; j < mazeHeight; j++) {
				// Create the grid square
				GridSquare gridSquare = new GridSquare(i, j, currentRoomId);
				allGridSquares[i][j] = gridSquare;
				
				// Assign it to a new Room
				Room room = new Room(currentRoomId, gridSquare);
				allRoomsMap.put(currentRoomId, room);
				
				// Increment the room id
				currentRoomId++;
			}
		}
		
		// Create all walls
		for(int i = 0; i < mazeWidth + 1; i++) {
			for(int j = 0; j < mazeHeight + 1; j++) {
				// Don't create vertical walls at the bottom
				if(j < mazeHeight) {
					boolean permanent = (i == 0 || i == mazeWidth);
					
					Wall wall = new Wall(i, j, Orientation.Vertical, permanent);
					int dirIndex = Orientation.Vertical.getIndex();
					
					allWalls[i][j][dirIndex] = wall;
					if(!permanent) {
						remainingWalls.add(wall);
					}
					
					if(i < mazeWidth) {
						GridSquare current = allGridSquares[i][j];
						current.setWall(Direction.Left, wall);
						wall.setPrimary(current);
					}
					
					if(i > 0) {
						GridSquare other = allGridSquares[i - 1][j];
						other.setWall(Direction.Right, wall);
						wall.setSecondary(other);
					}
				}
				
				// Don't create horizontal walls on the right side
				if(i < mazeWidth) {
					boolean permanent = (j == 0 || j == mazeHeight);
					
					Wall wall = new Wall(i, j, Orientation.Horizontal, permanent);
					int dirIndex = Orientation.Horizontal.getIndex();
					
					allWalls[i][j][dirIndex] = wall;
					if(!permanent) {
						remainingWalls.add(wall);
					}
					
					if(j < mazeHeight) {
						GridSquare current = allGridSquares[i][j];
						current.setWall(Direction.Up, wall);
						wall.setPrimary(current);
					}
					
					if(j > 0) {
						GridSquare other = allGridSquares[i][j - 1];
						other.setWall(Direction.Down, wall);
						wall.setSecondary(other);
					}
				}
			}
		}
	}
	
	public void printMaze() {
		printMaze(DEFAULT_PRINT_WIDTH, DEFAULT_PRINT_HEIGHT, DEFAULT_WALL_CHAR);
	}
	
	public void printMaze(int printWidth, int printHeight) {
		printMaze(printWidth, printHeight, DEFAULT_WALL_CHAR);
	}
		
	public void printMaze(int printWidth, int printHeight, char wallChar) {
		int horzRange = mazeWidth * (printWidth + 1) + 1;
		int vertRange = mazeHeight * (printHeight + 1) + 1;
		
		for(int vertIndex = 0; vertIndex < vertRange; vertIndex++) {
			for(int horzIndex = 0; horzIndex < horzRange; horzIndex++) {
				// Translate the current point to grid square coordinates
				int x = horzIndex / (printWidth + 1);
				int y = vertIndex / (printHeight + 1);
				
				// Determine if we at a point that corresponds to a wall
				// of the current grid square
				boolean horzWallFlag = (vertIndex % (printHeight + 1) == 0);
				boolean vertWallFlag = (horzIndex % (printWidth + 1) == 0);
				
				boolean wallPresentFlag = false;
				if(vertWallFlag && horzWallFlag) {
					wallPresentFlag = showCorner(x, y);
				}
				else if(vertWallFlag && !horzWallFlag) {
					wallPresentFlag = wallIsPresent(x, y, Orientation.Vertical);
				}
				else if(!vertWallFlag && horzWallFlag) {
					wallPresentFlag = wallIsPresent(x, y, Orientation.Horizontal);
				}
				
				char outputChar = (wallPresentFlag ? wallChar : ' ');
				System.out.print(outputChar);
			}
			System.out.println();
		}
	}
	
	private boolean wallIsPresent(int x, int y, Orientation orientation) {
		if(x >= 0 && x < mazeWidth + 1 && y >= 0 && y < mazeHeight + 1) {
			int orientationIndex = orientation.getIndex();
			Wall testWall = allWalls[x][y][orientationIndex];
			if(testWall != null) { 
				if(testWall.isPermanent() || !testWall.isRemoved()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean showCorner(int x, int y) {
		boolean result = false;
		result = (result || wallIsPresent(x, y, Orientation.Vertical));
		result = (result || wallIsPresent(x, y, Orientation.Horizontal));
		result = (result || wallIsPresent(x, y - 1, Orientation.Vertical));
		result = (result || wallIsPresent(x - 1, y, Orientation.Horizontal));
		
		return result;
	}
	
	private void createRandomMaze() {
		Random rng = new Random();
		
		while(allRoomsMap.size() > 1) {
			boolean foundWallToRemove = false;
			while(!foundWallToRemove) {
				int wallIndex = rng.nextInt(remainingWalls.size());
				Wall wallToRemove = remainingWalls.get(wallIndex);
				
				if(!wallToRemove.isPermanent() && !wallToRemove.isRemoved()) {
					GridSquare primary = wallToRemove.getPrimary();
					GridSquare secondary = wallToRemove.getSecondary();
							
					Integer primaryRoomId = primary.getRoomId();
					Integer secondaryRoomId = secondary.getRoomId();
					
					if(!primaryRoomId.equals(secondaryRoomId)) {
						foundWallToRemove = true;
						wallToRemove.setRemoved();
						remainingWalls.remove(wallIndex);
						mergeRooms(primaryRoomId, secondaryRoomId);
					}
				}
			}
		}
	}
	
	private void cleanupCommonWalls(Integer roomId1, Integer roomId2) {
		for(int i = remainingWalls.size() - 1; i >= 0; i--) {
			Wall checkWall = remainingWalls.get(i);
			
			GridSquare primary = checkWall.getPrimary();
			GridSquare secondary = checkWall.getSecondary();
			
			if(primary != null && secondary != null) {
				Integer primaryRoomId = primary.getRoomId();
				Integer secondaryRoomId = secondary.getRoomId();
				
				if((primaryRoomId.equals(roomId1) || primaryRoomId.equals(roomId2)) &&
				   (secondaryRoomId.equals(roomId1) || secondaryRoomId.equals(roomId2))) 
				{
					remainingWalls.remove(i);					
				}
			}
		}
	}
	
	private void cleanupCommonWalls(Room smallerRoom, Integer largerRoomId) {
		Set<Wall> checkedWalls = new HashSet<>();
		
		for(GridSquare smallRoomSquare : smallerRoom.getGridSquares()) {
			for(Direction direction : Direction.values()) {
				Wall checkWall = smallRoomSquare.getWall(direction);
			
				if(!checkWall.isPermanent() && !checkWall.isRemoved() && !checkedWalls.contains(checkWall)) {
					checkedWalls.add(checkWall);
					
					GridSquare checkSquare = null;
					if(Direction.Left == direction || Direction.Up == direction) {
						checkSquare = checkWall.getSecondary();
					}
					else if(Direction.Right == direction || Direction.Down == direction) {
						checkSquare = checkWall.getPrimary();
					}
					
					if(checkSquare != null && checkSquare.getRoomId().equals(largerRoomId)) {
						remainingWalls.remove(checkWall);
					}
				}
			}
		}
	}
	
	private void mergeRooms(Integer roomId1, Integer roomId2) {
		Room room1 = allRoomsMap.get(roomId1);
		Room room2 = allRoomsMap.get(roomId2);
		
		int remainingWallCount = remainingWalls.size();
		int room1Size = room1.size();
		int room2Size = room2.size();
		
		// Note that both versions of cleanupCommonWalls() will work, but
		// at different points in the maze generation, one version might be
		// faster.
		// 
		// For example, at the beginning, the rooms we are merging are small,
		// so it is fast to simply check all of the walls that are associated
		// with the rooms being merged.
		// 
		// At the end of maze generation, the rooms are large, but there are
		// fewer eligible walls to remove, so it will be faster to simply
		// iterate through that list to do the check.
		if(room1Size <= room2Size && (room1Size * 4 < remainingWallCount)) {
			cleanupCommonWalls(room1, roomId2);
		}
		else if(room2Size < room1Size && (room2Size * 4 < remainingWallCount)) {
			cleanupCommonWalls(room2, roomId1);
		}
		else {
			cleanupCommonWalls(roomId1, roomId2);
		}

		// Merge the smaller room into the larger room
		if(room1Size < room2Size) {
			room2.merge(room1);
			allRoomsMap.remove(roomId1);
		}
		else {
			room1.merge(room2);
			allRoomsMap.remove(roomId2);
		}
	}
}
