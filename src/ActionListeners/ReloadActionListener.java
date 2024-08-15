package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.CommandLauncherWindow;
import ApplicationBuilder.LoggingMessages;
import Properties.WidgetTextProperties;

public class ReloadActionListener implements ActionListener{

	private CommandLauncherWindow clWindow;
	
	public ReloadActionListener(CommandLauncherWindow clWindow)
	{
		this.clWindow = clWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		clWindow.reloadPropertiesFile();
		LoggingMessages.printOut(WidgetTextProperties.MENU_OPTION_RELOAD.getPropertiesValue());		
	}
}
