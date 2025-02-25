package ActionListenersImpl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import MouseListenersImpl.PicLabelMouseListener;
import Properties.LoggingMessages;

public class LaunchActionListener implements ActionListener
{
	private static Process runningProcess = null;
	private static final String
		PROCESS_WINDOWS = "chrome.exe",
		PROCESS_NOT_WINDOWS = "google-chrome",
		CLOSE_LAUNCH_ACTION_EVENT="closeLaunchAction";
	private String
		processWindows = PROCESS_WINDOWS,
		processLinux = PROCESS_NOT_WINDOWS;
	
	private static AbstractButton lastButton = null;
	private static Container lastButtonParent = null;
	
	public String getProcessWindowsOS()
	{
		return processWindows;
	}
	public void setProcessWindowsOS(String windowsProc)
	{
		processWindows = windowsProc;
	}
	
	public String getProcessLinuxOS()
	{
		return processLinux;
	}
	public void setProcessLinuxOS(String linuxProc)
	{
		processLinux = linuxProc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		AbstractButton button = (AbstractButton) e.getSource();
		LoggingMessages.printOut("Button Pressed. " + button.getText());
		LoggingMessages.printOut("Button Pressed. " + e);
		if(button.getName().equals(CLOSE_LAUNCH_ACTION_EVENT))
		{
			destroyRunningProcess();
			if(lastButton != null && !button.equals(lastButton) && lastButtonParent instanceof ArrayActionListener)
			{
				ArrayActionListener aal = (ArrayActionListener)lastButtonParent;
				aal.unselect();
			}
			PicLabelMouseListener.highLightLabel(lastButton, false);//TODO interface?
		}
		else
		{
			executeProcess(System.getProperty("os.name").startsWith("Windows")?getProcessWindowsOS():getProcessLinuxOS(), button.getName());
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
