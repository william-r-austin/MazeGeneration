package maze;

public class Main {
	
	public static void main(String[] args) {
		// Start time
		long start = System.currentTimeMillis();
		
		// Create random maze
		Maze randomMaze = new Maze(25, 15);
		
		// End time
		long end = System.currentTimeMillis();

		// Print the maze, specifying how many characters each room
		// covers, as well as the character to use to represent a wall
		randomMaze.printMaze(2, 1, '@');
		
		// Print total time.
		double seconds = (end - start) / 1000.0;
		System.out.printf("Generated maze in %.3f seconds\n", seconds);
	}
}
