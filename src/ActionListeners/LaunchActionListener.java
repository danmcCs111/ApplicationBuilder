package ActionListeners;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractButton;

import Properties.LoggingMessages;
import WidgetComponents.ArrayActionListener;

public class LaunchActionListener implements ActionListener
{
	private static Process runningProcess = null;
	private static final String
		PROCESS_WINDOWS = "chrome.exe",
		PROCESS_NOT_WINDOWS = "google-chrome",
		CLOSE_LAUNCH_ACTION_EVENT="closeLaunchAction";
	private static AbstractButton lastButton = null;
	private static Container lastButtonParent = null;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		AbstractButton button = (AbstractButton) e.getSource();
		LoggingMessages.printOut("Button Pressed. " + button.getText());
		if(button.getName().equals(CLOSE_LAUNCH_ACTION_EVENT))
		{
			destroyRunningProcess();
			if(lastButton != null && !button.equals(lastButton) && lastButtonParent instanceof ArrayActionListener)
			{
				ArrayActionListener aal = (ArrayActionListener)lastButtonParent;
				aal.unselect();
			}
		}
		else
		{
			executeProcess(System.getProperty("os.name").startsWith("Windows")?PROCESS_WINDOWS:PROCESS_NOT_WINDOWS, button.getName());
		}
		lastButton = button;
		lastButtonParent = lastButton.getParent();
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
	
	public static boolean destroyRunningProcess()
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
