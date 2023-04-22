package maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Maze {
	private  int MAZE_WIDTH = 40;
	private  int MAZE_HEIGHT = 18;
	
	private static final int GS_CHAR_WIDTH = 4;
	private static final int GS_CHAR_HEIGHT = 2;
	private static final char WALL_CHAR = '#';
	
	private GridSquare[][] allGridSquares;
	private Wall[][][] allWalls;
	private List<Wall> remainingWalls;
	private Map<Integer, Room> allRoomsMap;
	private Integer currentRoomId = 0;
	
	public Maze(int width, int height) {
		this.MAZE_WIDTH = width;
		this.MAZE_HEIGHT = height;
		
		this.allGridSquares = new GridSquare[MAZE_WIDTH][MAZE_HEIGHT];

		int numDirections = Direction.values().length;
		this.allWalls = new Wall[MAZE_WIDTH + 1][MAZE_HEIGHT+1][numDirections];

		this.remainingWalls = new ArrayList<>();
		this.allRoomsMap = new HashMap<>();
		
		initializeMaze();
	}
	
	private void initializeMaze() {
		for(int i = 0; i < MAZE_WIDTH; i++) {
			for(int j = 0; j < MAZE_HEIGHT; j++) {
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
		
		for(int i = 0; i < MAZE_WIDTH + 1; i++) {
			for(int j = 0; j < MAZE_HEIGHT + 1; j++) {
				// Don't create vertical walls at the bottom
				if(j < MAZE_HEIGHT) {
					boolean permanent = (i == 0 || i == MAZE_WIDTH);
					
					Wall wall = new Wall(i, j, Direction.Vertical, permanent);
					int dirIndex = Direction.Vertical.getIntValue();
					
					allWalls[i][j][dirIndex] = wall;
					if(!permanent) {
						remainingWalls.add(wall);
					}
					
					if(i < MAZE_WIDTH) {
						GridSquare current = allGridSquares[i][j];
						current.setWall(0, wall);
						wall.setPrimary(current);
					}
					
					if(i > 0) {
						GridSquare other = allGridSquares[i - 1][j];
						other.setWall(2, wall);
						wall.setSecondary(other);
					}
				}
				
				// Don't create horizontal walls on the right side
				if(i < MAZE_WIDTH) {
					boolean permanent = (j == 0 || j == MAZE_HEIGHT);
					
					Wall wall = new Wall(i, j, Direction.Horizontal, permanent);
					int dirIndex = Direction.Horizontal.getIntValue();
					
					allWalls[i][j][dirIndex] = wall;
					if(!permanent) {
						remainingWalls.add(wall);
					}
					
					if(j < MAZE_HEIGHT) {
						GridSquare current = allGridSquares[i][j];
						current.setWall(1, wall);
						wall.setPrimary(current);
					}
					
					if(j > 0) {
						GridSquare other = allGridSquares[i][j - 1];
						other.setWall(3, wall);
						wall.setSecondary(other);
					}
				}
			}
		}
	}
	
	public void printMaze() {
		int xRange = MAZE_WIDTH * (GS_CHAR_WIDTH + 1) + 1;
		int yRange = MAZE_HEIGHT * (GS_CHAR_HEIGHT + 1) + 1;
		
		for(int y = 0; y < yRange; y++) {
			for(int x = 0; x < xRange; x++) {
				int vWallIndex = x / (GS_CHAR_WIDTH + 1);
				boolean vWallFlag = (x % (GS_CHAR_WIDTH + 1) == 0);
				
				int hWallIndex = y / (GS_CHAR_HEIGHT + 1);
				boolean hWallFlag = (y % (GS_CHAR_HEIGHT + 1) == 0);
				
				boolean wallPresentFlag = false;
				if(vWallFlag && hWallFlag) {
					wallPresentFlag = showCorner(vWallIndex, hWallIndex);
				}
				else if(vWallFlag && !hWallFlag) {
					wallPresentFlag = wallIsPresent(vWallIndex, hWallIndex, 0);
				}
				else if(!vWallFlag && hWallFlag) {
					wallPresentFlag = wallIsPresent(vWallIndex, hWallIndex, 1);
				}
				
				char outputChar = (wallPresentFlag ? WALL_CHAR : ' ');
				System.out.print(outputChar);
			}
			System.out.println();
		}
	}
	
	private boolean wallIsPresent(int x, int y, int direction) {
		if(x >= 0 && x < MAZE_WIDTH + 1 && y >= 0 && y < MAZE_HEIGHT + 1) {
			Wall testWall = allWalls[x][y][direction];
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
		result = (result || wallIsPresent(x, y, 0));
		result = (result || wallIsPresent(x, y, 1));
		result = (result || wallIsPresent(x, y - 1, 0));
		result = (result || wallIsPresent(x - 1, y, 1));
		
		return result;
	}
	
	public void createRandomMaze() {
		Random rng = new Random();
		
		while(allRoomsMap.size() > 1) {
			boolean isValidWall = false;
			while(!isValidWall) {
				int wallIndex = rng.nextInt(remainingWalls.size());
				Wall wallToRemove = remainingWalls.get(wallIndex);
				
				if(!wallToRemove.isPermanent() && !wallToRemove.isRemoved()) {
					GridSquare primary = wallToRemove.getPrimary();
					GridSquare secondary = wallToRemove.getSecondary();
							
					Integer primaryRoomId = primary.getRoomId();
					Integer secondaryRoomId = secondary.getRoomId();
					
					if(!primaryRoomId.equals(secondaryRoomId)) {
						isValidWall = true;
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
			for(int z = 0; z < 4; z++) {
				Wall checkWall = smallRoomSquare.getWall(z);
				if(!checkWall.isPermanent() && !checkWall.isRemoved() && !checkedWalls.contains(checkWall)) {
					checkedWalls.add(checkWall);
					
					GridSquare checkSquare = null;
					if(z <= 1) {
						checkSquare = checkWall.getSecondary();
					}
					else {
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
		
		if(room1Size <= room2Size && (room1Size * 4 < remainingWallCount)) {
			cleanupCommonWalls(room1, roomId2);
		}
		else if(room2Size < room1Size && (room2Size * 4 < remainingWallCount)) {
			cleanupCommonWalls(room2, roomId1);
		}
		else {
			cleanupCommonWalls(roomId1, roomId2);
		}
		
		if(room1Size <= room2Size) {
			room1.merge(room2);
			allRoomsMap.remove(roomId2);
		}
		else {
			room2.merge(room1);
			allRoomsMap.remove(roomId1);
		}
	}
}
