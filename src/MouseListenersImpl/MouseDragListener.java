package MouseListenersImpl;

import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import WidgetComponents.JButtonLengthLimited;

public class MouseDragListener extends MouseAdapter 
{
	private static final int FRAME_AND_TITLE_HEIGHT = 45; 
	private static final String OPEN_MENU_TEXT = "OPEN";
	
	
	private Point mouseDownCompCoords = null;
	private JFrame f;
	private AbstractButton component;
	private JLabel picLabel;
	
	private boolean mouse1Pressed = false;
	
	public MouseDragListener(JFrame f, AbstractButton component, JLabel picLabel)
	{
		super();
		this.f = f;
		this.component = component;
		this.picLabel = picLabel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			PopupMenu pm = new PopupMenu();
			MenuItem mi = new MenuItem();
			mi.setLabel(OPEN_MENU_TEXT);
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(ActionListener al : component.getActionListeners())
					{
						al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
						PicLabelMouseListener.highLightLabel((JButtonLengthLimited) component, true);//TODO
					}
				}
			});
			pm.add(mi);
			picLabel.add(pm);
			int x = e.getPoint().x, y = e.getPoint().y;
			pm.show(picLabel, x, y);
		}
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
	        currCoords.setLocation(currCoords.x, currCoords.y - FRAME_AND_TITLE_HEIGHT);
	        f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
}
