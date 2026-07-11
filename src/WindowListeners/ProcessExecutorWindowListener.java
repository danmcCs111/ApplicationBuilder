package WindowListeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ActionListenersImpl.LaunchUrlActionListener;

public class ProcessExecutorWindowListener extends WindowAdapter
{
	@Override
	public void windowOpened(WindowEvent e)
	{
		LaunchUrlActionListener.bootCheckRunningProcess();
	}
	
	@Override
	public void windowClosing(WindowEvent e) 
	{
		//TODO. write to file.
		LaunchUrlActionListener.destroyRunningProcess();
	}
	
}
