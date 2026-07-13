package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

import MouseListenersImpl.FrameMouseDragListener;

public class TouchFrameDragActionListener implements ActionListener 
{
	public boolean isTouch()
	{
		return FrameMouseDragListener.isTouch(); 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		AbstractButton ab = (AbstractButton) e.getSource();
		if(ab instanceof JToggleButton)
		{
			JToggleButton tb = (JToggleButton) ab;
			if(tb.isSelected())
			{
				FrameMouseDragListener.setIsTouch(true);
			}
			else
			{
				FrameMouseDragListener.setIsTouch(false);
			}
		}
		else
		{
			FrameMouseDragListener.setIsTouch(!FrameMouseDragListener.isTouch());
		}
	}
}
