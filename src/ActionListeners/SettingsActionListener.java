package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.CommandLauncherWindow;
import WidgetUtility.WidgetCreator;

public class SettingsActionListener implements ActionListener {

	private CommandLauncherWindow clWindow;
	
	public SettingsActionListener(CommandLauncherWindow clWindow)
	{
		this.clWindow = clWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		WidgetCreator.createDialog(clWindow);
	}
}
