package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
	private static final int MAZE_WIDTH = 40;
	private static final int MAZE_HEIGHT = 18;
	
	private static final int GS_CHAR_WIDTH = 4;
	private static final int GS_CHAR_HEIGHT = 2;
	private static final char WALL_CHAR = '#';
	
	private static GridSquare[][] allGridSquares;
	private static Wall[][][] allWalls; 
	private static List<Room> allRooms;
	private static List<Wall> remainingWalls;
	
	private static boolean wallIsPresent(int x, int y, int direction) {
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
	
	private static boolean showCorner(int x, int y) {
		boolean result = false;
		result = (result || wallIsPresent(x, y, 0));
		result = (result || wallIsPresent(x, y, 1));
		result = (result || wallIsPresent(x, y - 1, 0));
		result = (result || wallIsPresent(x - 1, y, 1));
		
		return result;
	}
	
	private static void print() {
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
	
	public static void main(String[] args) {
		allGridSquares = new GridSquare[MAZE_WIDTH][MAZE_HEIGHT];
		allRooms = new ArrayList<>();
		
		for(int i = 0; i < MAZE_WIDTH; i++) {
			for(int j = 0; j < MAZE_HEIGHT; j++) {
				allGridSquares[i][j] = new GridSquare(i, j);
				allRooms.add(new Room(allGridSquares[i][j]));
			}
		}
		
		remainingWalls = new ArrayList<>();
				
		// 0 = vertical wall
		// 1 = horizontal wall
		allWalls = new Wall[MAZE_WIDTH + 1][MAZE_HEIGHT+1][2];
		for(int i = 0; i < MAZE_WIDTH + 1; i++) {
			for(int j = 0; j < MAZE_HEIGHT + 1; j++) {
				// Don't create vertical walls at the bottom
				if(j < MAZE_HEIGHT) {
					boolean permanent = (i == 0 || i == MAZE_WIDTH);
					allWalls[i][j][0] = new Wall(i, j, 0, permanent);
					if(!permanent) {
						remainingWalls.add(allWalls[i][j][0]);
					}
				}
				
				if(i < MAZE_WIDTH) {
					boolean permanent = (j == 0 || j == MAZE_HEIGHT);
					allWalls[i][j][1] = new Wall(i, j, 1, permanent);
					if(!permanent) {
						remainingWalls.add(allWalls[i][j][1]);
					}
				}
			}
		}
		
		Random rng = new Random();
		
		System.out.println("Start State. Room count = " + allRooms.size());
		print();
		
		while(allRooms.size() > 1) {
			boolean isValidWall = false;
			while(!isValidWall) {
				int wallIndex = rng.nextInt(remainingWalls.size());
				Wall wallToRemove = remainingWalls.get(wallIndex);
				
				if(!wallToRemove.isPermanent() && !wallToRemove.isRemoved()) {
					int direction = wallToRemove.getDirection();
					int xWall = wallToRemove.getX();
					int yWall = wallToRemove.getY();
					
					int x1, y1, x2, y2;
					if(direction == 0) {
						x1 = xWall - 1;
						y1 = yWall;
						x2 = xWall;
						y2 = yWall;
					}
					else {
						x1 = xWall;
						y1 = yWall - 1;
						x2 = xWall;
						y2 = yWall;
					}
					
					int r1 = findRoomForGridSquare(x1, y1);
					int r2 = findRoomForGridSquare(x2, y2);
					
					if(r1 >= 0 && r2 >= 0 && r1 != r2) {
						isValidWall = true;
						wallToRemove.setRemoved();
						Room room1 = allRooms.get(r1);
						Room room2 = allRooms.get(r2);
						
						room1.getGridSquares().addAll(room2.getGridSquares());
						allRooms.remove(r2);
					}
				}
			}
			
			System.out.println("\n\nNext Iteration. Room count = " + allRooms.size());
			print();
		}
	}
	
	private static int findRoomForGridSquare(int x, int y) {
		for(int r = 0; r < allRooms.size(); r++) {
			Room test = allRooms.get(r);
			for(GridSquare gs : test.getGridSquares()) {
				if(gs.getX() == x && gs.getY() == y) {
					return r;
				}
			}
		}
		return -1;
	}
}
