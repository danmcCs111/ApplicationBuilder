package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.CommandLauncher;
import ApplicationBuilder.LoggingMessages;
import Properties.WidgetTextProperties;

public class ExitActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		CommandLauncher.closeProcess();
		System.exit(0);
		LoggingMessages.printOut(WidgetTextProperties.MENU_OPTION_EXIT.getPropertiesValue());		
	}
}
