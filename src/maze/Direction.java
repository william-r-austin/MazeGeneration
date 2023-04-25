package maze;

public enum Direction {
	Left,
	Up,
	Right,
	Down;
		
	public static int size() {
		return Direction.values().length;
	}
	
	public int getIndex() {
		return ordinal();
	}
}
