package MouseListenersImpl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class ClockMouseDragListener extends MouseAdapter implements MouseListener, MouseMotionListener
{
	private Point mouseDownCompCoords = null;
	
	public boolean mouse1Pressed = false;
	JComponent parentPanel;
	JFrame f;
	int heightDiff;
	
	public ClockMouseDragListener()
	{
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
        mouseDownCompCoords = null;
    }

	@Override
    public void mousePressed(MouseEvent e) 
	{
		checkToAddOffset(e);
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
		checkToAddOffset(e);
		for (MouseListener ml : parentPanel.getMouseListeners())//Relying on 2 instances. Only use the MouseListener instance...
		{
			if(ml instanceof ClockMouseDragListener)
			{
				if(((ClockMouseDragListener)ml).mouse1Pressed)
				{
					((ClockMouseDragListener)ml).mouseDragged(e.getLocationOnScreen());
				}
			}
		}
	}
	
	public void mouseDragged(Point p)
	{
        Point currCoords = p;
        currCoords.setLocation(currCoords.x, currCoords.y - heightDiff);
        this.f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
	}
	
	public void checkToAddOffset(MouseEvent e)
	{
		if(e != null && parentPanel == null)
		{
			if(e.getComponent() instanceof JComponent)
			{
				getOffsetCalc(((JComponent)e.getComponent()));
			}
		}
	}
	
	public void getOffsetCalc(JComponent parentPanel)
	{
		this.parentPanel = parentPanel;
		this.f = (JFrame) parentPanel.getRootPane().getParent();
		Dimension dimFrame = f.getSize();
		Dimension dimParentPanel = parentPanel.getSize();
		heightDiff = dimFrame.height - dimParentPanel.height;
	}
}
