package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractButton;

import ApplicationBuilder.LoggingMessages;

public class LaunchActionListener implements ActionListener
{
	private static Process runningProcess = null;
	private static final String PROCESS = "chrome.exe";
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		AbstractButton button = (AbstractButton) e.getSource();
		LoggingMessages.printOut("Button Pressed. " + button.getName());
		executeProcess(PROCESS, button.getName());
	}
	
	private static void executeProcess(String ...args)
	{
		try {
			destroyRunningProcess();
			ProcessBuilder pb = new ProcessBuilder(args);
			runningProcess = pb.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static boolean destroyRunningProcess()
	{
		if(runningProcess != null)
		{
			runningProcess.destroy();
			runningProcess.descendants().forEach(ProcessHandle::destroy);
			return true;
		}
		return false;
	}
	
}
