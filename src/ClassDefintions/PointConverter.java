package ClassDefintions;

import java.awt.Point;

public class PointConverter implements StringToObjectConverter{
	public static Point getPoint(String arg0, String arg1)
	{
		Point p = new Point();
		p.setLocation(Integer.parseInt(arg0), Integer.parseInt(arg1));
		return p;
	}

	@Override
	public int numberOfArgs() {
		return 2;
	}
}
