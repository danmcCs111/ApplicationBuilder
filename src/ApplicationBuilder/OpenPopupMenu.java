package ApplicationBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetUtility.WidgetCreator;

public class OpenPopupMenu implements ActionListener{
	
	private CommandLauncherWindow rlWindow;
	
	public OpenPopupMenu(CommandLauncherWindow rlWindow)
	{
		this.rlWindow = rlWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		rlWindow.setVisible(true);
		WidgetCreator.destroySystemTray(rlWindow.getTrayIcon());
	}
}
