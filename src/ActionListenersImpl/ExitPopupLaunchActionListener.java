package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitPopupLaunchActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LaunchUrlActionListener.destroyRunningProcess();
		System.exit(0);
	}
	
}
