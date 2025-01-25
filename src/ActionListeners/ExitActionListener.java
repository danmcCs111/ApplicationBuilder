package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ApplicationBuilder.LoggingMessages;

public class ExitActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LoggingMessages.printOut("Exit");
		System.exit(0);
	}

}
