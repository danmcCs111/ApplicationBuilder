package MouseListenersImpl;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseDragScrollListener extends MouseAdapter
{
	 private Point 
	 	startPoint;
	 
	 private static int
	 	MOUSE_DRAG_DELAY = 50,
	 	MOUSE_DRAG_SPEED = 1;
	 
	 public static void setMouseDragDelay(int delay)
	 {
		 MOUSE_DRAG_DELAY = delay;
	 }

     @Override
     public void mousePressed(MouseEvent e) 
     {
         startPoint = e.getPoint();
     }

     @Override
     public void mouseDragged(MouseEvent e) 
     {
         Point currentPoint = e.getPoint();
         int dy = startPoint.y - currentPoint.y;
         startPoint = currentPoint;
         
         Robot robot;
		try {
			robot = new Robot();
			robot.mouseWheel(dy > 0 ? -MOUSE_DRAG_SPEED : MOUSE_DRAG_SPEED);
			robot.delay(MOUSE_DRAG_DELAY);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

     }
}
