package maze;

public enum Orientation {
	Vertical,
	Horizontal;
		
	public int getIndex() {
		return ordinal();
	}
	
	public static int size() {
		return Orientation.values().length;
	}
}
