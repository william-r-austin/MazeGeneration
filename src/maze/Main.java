package maze;

public class Main {
	
	public static void main(String[] args) {
		Maze randomMaze = new Maze(10, 5);
		randomMaze.createRandomMaze();
		randomMaze.printMaze();
	}
}
