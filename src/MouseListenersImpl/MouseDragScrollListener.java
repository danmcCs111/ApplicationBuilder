package MouseListenersImpl;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseDragScrollListener extends MouseAdapter
{
	 private Point 
	 	compLastLocation,
	 	startPoint;
	 
	 private static int
	 	MOUSE_DRAG_DELAY = 50,
	 	MOUSE_WHEEL_SPIN = 1;
	 
	 public static void setMouseDragDelay(int delay)
	 {
		 MOUSE_DRAG_DELAY = delay;
	 }
	 
	 public static void setMouseWheelSpin(int spin)
	 {
		 MOUSE_WHEEL_SPIN = spin;
	 }
	 
     @Override
     public void mousePressed(MouseEvent e) 
     {
         startPoint = e.getPoint();
     }
     
     @Override
     public void mouseDragged(MouseEvent e) 
     {
    	 Component comp = (Component) e.getSource();
    	 Point p = comp.getLocationOnScreen();
    	 
    	 int movedDiff = 0;
    	 if(compLastLocation != null)
    	 {
    		 movedDiff = compLastLocation.y - p.y;
    	 }
    	 compLastLocation = p;
    	 
         Point currentPoint = e.getPoint();
       	 int dy = (startPoint.y + movedDiff) - currentPoint.y;
         int spin = MOUSE_WHEEL_SPIN;
         
         Robot robot;
		try {
			robot = new Robot();
			if(dy > 0)
			{
				robot.mouseWheel(-spin);
			}
			else if(dy < 0)
			{
				robot.mouseWheel(spin);
			}
			startPoint = currentPoint;
			robot.delay(MOUSE_DRAG_DELAY);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

     }
     
}
