package MouseListenersImpl;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;

public class MouseDragScrollListener extends MouseAdapter
{
	private static int
		SCROLL_ADJUSTMENT = -1,
		MOUSE_DRAG_DELAY = 50,
		MOUSE_WHEEL_SPIN = 1;
	
	 private Point 
	 	lastPoint,
	 	compLastLocation,
	 	startPoint;
	 private JScrollPane 
	 	sPane;
	 private int
	 	originalValue = -1;
	 
	 public static void setUnitIncrementAdjustment(int unitIncrement)
	 {
		 SCROLL_ADJUSTMENT = unitIncrement;
	 }
	 public static int getUnitIncrementAdjustment()
	 {
		 return SCROLL_ADJUSTMENT;
	 }
	 public static void setMouseDragDelay(int delay)
	 {
		 MOUSE_DRAG_DELAY = delay;
	 }
	 public static int getMouseDragDelay()
	 {
		 return MOUSE_DRAG_DELAY;
	 }
	 public static void setMouseWheelSpin(int spin)
	 {
		 MOUSE_WHEEL_SPIN = spin;
	 }
	 public static int getMouseWheelSpin()
	 {
		 return MOUSE_WHEEL_SPIN;
	 }
	 
     @Override
     public void mousePressed(MouseEvent e) 
     {
         startPoint = e.getPoint();
     }
     
     @Override
     public void mouseReleased(MouseEvent e)
     {
    	 lastPoint = null;
    	 if(SCROLL_ADJUSTMENT != -1 && originalValue != -1)
    	 {
    		 sPane.getVerticalScrollBar().setUnitIncrement(originalValue);
    	 }
     }
     
     @Override
     public void mouseDragged(MouseEvent e) 
     {
    	 Component 
    	 	comp = (Component) e.getSource();
    	 if(SCROLL_ADJUSTMENT != -1)//perform everytime incase scroll pane rebuilt...
    	 {
    		 sPane = findScrollPane(comp);
    		 if(sPane != null)
    		 {
    			 int scrollInc = sPane.getVerticalScrollBar().getUnitIncrement();
    			 if(originalValue == -1)
    			 {
    				 originalValue = scrollInc; 
    			 }
    			 sPane.getVerticalScrollBar().setUnitIncrement(SCROLL_ADJUSTMENT);
    		 }
    	 }
    	 
    	 Point 
    	 	compLoc = comp.getLocationOnScreen(),
			currentPoint = e.getPoint();
    	 int 
    	 	movedDiff = 0;
		
    	 if(compLastLocation != null)
    	 {
    		 movedDiff = compLastLocation.y - compLoc.y;
    	 }
    	 compLastLocation = compLoc;
		 
    	 if(movedDiff == 0 && lastPoint != null)
    	 {
    		 return;
    	 }
    	 
    	 if(lastPoint == null)
    	 {
    		 lastPoint = startPoint;
    	 }
    	 
    	 int dy = (lastPoint.y + movedDiff) - currentPoint.y;
 
    	 Robot robot;
    	 try {
    		 robot = new Robot();
    		 if(dy > 0)
    		 {
    			 robot.mouseWheel(-MOUSE_WHEEL_SPIN);
    		 }
    		 else if(dy < 0)
    		 {
    			 robot.mouseWheel(MOUSE_WHEEL_SPIN);
    		 }
    		 lastPoint = currentPoint;
    		 robot.delay(MOUSE_DRAG_DELAY);
    	 } catch (AWTException e1) {
			e1.printStackTrace();
    	 }
     }
     
     public JScrollPane findScrollPane(Component c)
     {
    	 if(c == null)
    		 return null;
    	 
    	 Container parent = c.getParent();
    	 if(parent == null)
    	 {
    		 return null;
    	 }
    	 if(parent instanceof JScrollPane)
    	 {
    		 return (JScrollPane) parent;
    	 }
    	 else
    	 {
    		 return findScrollPane(parent);
    	 }
     }
     
}
