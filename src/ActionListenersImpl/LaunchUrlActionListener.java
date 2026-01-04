package ActionListenersImpl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractButton;

import ActionListeners.ArrayActionListener;
import MouseListenersImpl.PicLabelMouseListener;
import ObjectTypeConversion.DirectorySelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.LaunchUrlButton;
import WidgetComponents.JButtonLengthLimited;

public class LaunchUrlActionListener implements ActionListener
{
	private static Process runningProcess = null;
	private static final String
		PROCESS_WINDOWS = "chrome.exe",
		PROCESS_LINUX = "google-chrome",
		CLOSE_LAUNCH_ACTION_EVENT="closeLaunchAction",
		CHROME_HIDE_OPTION = "--hide-crash-restore-bubble",
		CHROME_SEPERATE_OPTION = "--user-data-dir=" + PathUtility.getCurrentDirectory() + "\"NewProfile\"",
		CHROME_NO_DEFAULT_CHECK = "--no-default-browser-check",
		CHROME_KIOSK = "--kiosk",
		AHK_RELATIVE_PATH = "./plugin-projects/AutoHotKey-Utils/pid.txt";
	private String
		processWindows = PROCESS_WINDOWS,
		processLinux = PROCESS_LINUX;
	
	private static AbstractButton lastButton = null;
	private static Container lastButtonParent = null;
	private static boolean isKiosk = false;
	
	public String getProcessWindowsOS()
	{
		return processWindows;
	}
	public void setProcessWindowsOS(String windowsProc)
	{
		processWindows = windowsProc;
	}
	
	public static void setIsKiosk(boolean isKiosk)
	{
		LaunchUrlActionListener.isKiosk = isKiosk;
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
			if(lastButton != null)
			{
				if(!button.equals(lastButton) && lastButtonParent instanceof ArrayActionListener)
				{
					ArrayActionListener aal = (ArrayActionListener)lastButtonParent;
					aal.unselect();
				}
				if(lastButton instanceof JButtonLengthLimited)
				{
					PicLabelMouseListener.highLightLabel((JButtonLengthLimited) lastButton, false);//TODO interface?
				}
			}
		}
		else
		{
			String [] args = null;
			if(LaunchUrlActionListener.isKiosk)
			{
				args = new String [] {
					PathUtility.isWindows()?getProcessWindowsOS():getProcessLinuxOS(), 
					CHROME_HIDE_OPTION, CHROME_SEPERATE_OPTION, CHROME_NO_DEFAULT_CHECK, CHROME_KIOSK, 
					button.getName()	
				};
			}
			else
			{
				args = new String [] {
						PathUtility.isWindows()?getProcessWindowsOS():getProcessLinuxOS(), 
						CHROME_HIDE_OPTION, CHROME_SEPERATE_OPTION, CHROME_NO_DEFAULT_CHECK, 
						button.getName()
				};
			}
			executeProcess(args);
		}
		if(button instanceof LaunchUrlButton)
		{
			lastButton = ((LaunchUrlButton) button).getHighlightButton();
		}
		else
		{
			lastButton = button;
		}
		lastButtonParent = lastButton.getParent();
	}
	
	private static void executeProcess(String ...args)
	{
		try {
			destroyRunningProcess();
			ProcessBuilder pb = new ProcessBuilder(args);
			runningProcess = pb.start();
			Long pid = runningProcess.pid();
			File f = new File(new DirectorySelection(AHK_RELATIVE_PATH).getFullPath());
			PathUtility.writeStringToFile(f, pid + "");
			
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
