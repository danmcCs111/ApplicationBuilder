package WidgetComponentInterfaces;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;

public interface RegisterArrayActionListener 
{
	public static void addListener(Container con)
	{
		if(con instanceof ArrayActionListener)
		{
			ArrayActionListener aal = (ArrayActionListener) con;
			LaunchUrlActionListener.addArrayActionListener(aal);
			
			Window window = SwingUtilities.getWindowAncestor(con);
			if (window instanceof JFrame) 
			{
				JFrame frame = (JFrame) window;
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) 
					{
						aal.removeArrayActionListener();
					}
				});
			}
		}//else nop;
	}
}
