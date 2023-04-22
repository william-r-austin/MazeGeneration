package maze;

public enum Direction {
	Vertical(0),
	Horizontal(1);
		
	private int intValue;
	
	private Direction(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue() {
		return intValue;
	}
}
