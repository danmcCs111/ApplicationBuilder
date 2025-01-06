package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.LoggingMessages;

public class ReloadActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LoggingMessages.printOut("Reload");
	}
}
