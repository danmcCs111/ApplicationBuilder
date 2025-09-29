package DrawModesAbstract;

public interface DrawModeInstructions 
{
	public static final String [] 
		SINGLE_POINT_DIRECTIONS = new String [] {
			"Enter x, y"
		},
		TEXT_DIRECTIONS = new String [] {
			"Enter x, y"
		},
		LINE_DIRECTIONS = new String [] {
			"",
			"Enter x, y", 
			"Enter x2, y2"
		},
		QUADRATIC_CURVE_DIRECTIONS = new String [] {
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
		},
		CUBIC_CURVE_DIRECTIONS = new String [] {
			"Enter x, y", 
			"Enter x2, y2", 
			"Enter Control Point 1", 
			"Enter Control Point 2"
		},
		ELLIPSE_DIRECTIONS = new String [] {
			"Enter x, y",
			"Enter x2, y2"
		},
		RECTANGLE_CUBIC_DIRECTIONS = new String [] {
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3",
			"Enter x4, y4",
			
			"Enter control 1, y1",
			"Enter control 1.2, y1.2",
			
			"Enter control 2, y2",
			"Enter control 2.2, y2.2",
			
			"Enter control 3, y3",
			"Enter control 3.2, y3.2",
			
			"Enter control 4, y4",
			"Enter control 4.2, y4.2"
		},
		RECTANGLE_DIRECTIONS = new String [] {
			"Enter x, y",
			"Enter x2, y2"
		},
		TRIANGLE_CUBIC_DIRECTIONS = new String [] {
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3",
			
			"Enter control 1, y1",
			"Enter control 1.2, y1.2",
			
			"Enter control 2, y2",
			"Enter control 2.2, y2.2",
			
			"Enter control 3, y3",
			"Enter control 3.2, y3.2"
		},
		TRIANGLE_DIRECTIONS = new String [] {
			"Enter x, y",
			"Enter x2, y2",
			"Enter x3, y3"
		};
}
