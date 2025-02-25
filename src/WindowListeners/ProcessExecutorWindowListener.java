package WindowListeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ActionListenersImpl.LaunchActionListener;

public class ProcessExecutorWindowListener extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent e) 
	{
		LaunchActionListener.destroyRunningProcess();
	}
	
	@Override 
	public void windowClosed(WindowEvent e)
	{
		LaunchActionListener.destroyRunningProcess();
	}
}
