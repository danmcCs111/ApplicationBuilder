package Graphics2D;

import java.awt.Container;
import java.awt.Point;

public class GraphicsUtil 
{
	public static void centerWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		int rw = referenceComponent.getWidth();
		int rh = referenceComponent.getHeight();
		int w = comp.getWidth();
		int h = comp.getHeight();
		
		comp.setLocation(new Point(loc.x + (rw/2) - (w/2), loc.y + (rh/2) - (h/2)));
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp)
	{
		rightEdgeTopWindow(referenceComponent, comp, 1);
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp, int numberOfMatchedVisible)
	{
		Point loc = referenceComponent.getLocation();
		int rw = referenceComponent.getWidth();
		int h = comp.getHeight();
		
		comp.setLocation(new Point(loc.x + (rw), loc.y + (h * (numberOfMatchedVisible-1) )));
	}
}
