package ShapeWidgetComponents;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ClockMouseDragListener extends MouseAdapter 
{
	private Point mouseDownCompCoords = null;
	
	private boolean mouse1Pressed = false;
	JComponent parentPanel;
	JFrame f;
	int heightDiff;
	
	public ClockMouseDragListener(JComponent parentPanel)
	{
		this.parentPanel = parentPanel;
		f = (JFrame) parentPanel.getRootPane().getParent();
		Dimension dimFrame = f.getSize();
		Dimension dimParentPanel = parentPanel.getSize();
		heightDiff = dimFrame.height - dimParentPanel.height;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
        mouseDownCompCoords = null;
    }

	@Override
    public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			mouseDownCompCoords = e.getPoint();
			mouse1Pressed = true;
		}
		else
		{
			mouse1Pressed = false;
		}
    }
        
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		if(mouse1Pressed)
		{
	        Point currCoords = e.getLocationOnScreen();
	        currCoords.setLocation(currCoords.x, currCoords.y - heightDiff);
	        f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
}
