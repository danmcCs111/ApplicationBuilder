package WindowListeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ActionListenersImpl.LaunchUrlActionListener;

public class ProcessExecutorWindowListener extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent e) 
	{
		LaunchUrlActionListener.destroyRunningProcess();
	}
	
	@Override 
	public void windowClosed(WindowEvent e)
	{
		LaunchUrlActionListener.destroyRunningProcess();
	}
}
