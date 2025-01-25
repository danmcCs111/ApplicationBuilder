package WindowListeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ActionListeners.LaunchActionListener;

public class ProcessExecutorWindowListener extends WindowAdapter
{
	@Override
	public void windowClosing(WindowEvent e) 
	{
		LaunchActionListener.destroyRunningProcess();
	}
}
