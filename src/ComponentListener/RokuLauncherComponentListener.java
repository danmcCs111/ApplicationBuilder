package ComponentListener;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import ApplicationBuilder.CommandLauncherWindow;

public class RokuLauncherComponentListener extends ComponentAdapter{
	
	private CommandLauncherWindow launcherWindow;
	
	public RokuLauncherComponentListener(CommandLauncherWindow rlWindow){
		this.launcherWindow = rlWindow;
	}
	@Override
	public void componentResized(ComponentEvent e) {
		launcherWindow.reSizeDetect();
	}
}
