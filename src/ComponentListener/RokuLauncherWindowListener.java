package ComponentListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ApplicationBuilder.CommandLauncher;
import ApplicationBuilder.CommandLauncherWindow;

public class RokuLauncherWindowListener extends WindowAdapter 
{
	private CommandLauncherWindow launcherWindow = null;

	public RokuLauncherWindowListener(CommandLauncherWindow rlWindow)
	{
		this.launcherWindow = rlWindow;
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		launcherWindow.initialSizeDetect();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		CommandLauncher.closeProcess();
	}
}
