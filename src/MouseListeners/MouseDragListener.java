package MouseListeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class MouseDragListener extends MouseAdapter 
{
	private static final int FRAME_AND_TITLE_HEIGHT = 45; 
	
	private Point mouseDownCompCoords = null;
	private JFrame f;
	
	public MouseDragListener(JFrame f)
	{
		super();
		this.f = f;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

	@Override
    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }
        
	@Override
	public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        currCoords.setLocation(currCoords.x, currCoords.y - FRAME_AND_TITLE_HEIGHT);
        f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
	}
}
