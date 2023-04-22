package maze;

public class Main {
	
	public static void main(String[] args) {
		// Start time
		long start = System.currentTimeMillis();
		
		// Create random maze
		Maze randomMaze = new Maze(30, 15);
		
		// End time
		long end = System.currentTimeMillis();

		// Print the maze
		randomMaze.printMaze(2, 1, '@');
		
		// Print total time.
		double seconds = (end - start) / 1000.0;
		System.out.printf("Generated maze in %.3f seconds\n", seconds);
	}
}
