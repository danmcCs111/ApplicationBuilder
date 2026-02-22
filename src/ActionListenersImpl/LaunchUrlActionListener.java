package ActionListenersImpl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	
	private static ArrayList<AbstractButton> 
		lastButtons = new ArrayList<AbstractButton>();
	private static AbstractButton
		lastButtonOrigin;
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
	
	public static AbstractButton getLastButtonOrigin()
	{
		return lastButtonOrigin;
	}
	public static void setLastButtonOrigin(AbstractButton ab)
	{
		lastButtonOrigin = ab;
		storeLast(lastButtonOrigin);
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
			for(AbstractButton lastButton : lastButtons)
			{
				if(lastButton != null)
				{
					Container lastButtonParent = lastButton.getParent();
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
		}
		else
		{
			if(lastButtonOrigin != null && 
					!lastButtonOrigin.equals(button.getParent()))
			{
				for(AbstractButton lastButton : lastButtons)
				{
					if(lastButton != null)
					{
						Container lastButtonParent = lastButton.getParent();
						if(!lastButtonParent.equals(button.getParent()) && lastButtonParent instanceof ArrayActionListener)
						{
							ArrayActionListener aal = (ArrayActionListener)lastButtonParent;
							aal.unselect();
						}
					}
				}
			}
			
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
		storeLast(button);
	}
	
	private static void storeLast(AbstractButton button)
	{
		lastButtons = new ArrayList<AbstractButton>();
		if(button instanceof LaunchUrlButton)
		{
			AbstractButton highlight = ((LaunchUrlButton) button).getHighlightButton();
			lastButtons.add(highlight);
			if(highlight != button)
			{
				lastButtons.add(button);
			}
		}
		else
		{
			lastButtons.add(button);
		}
		lastButtonOrigin = button;
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
			while(runningProcess.isAlive())
			{
				try {
					Thread.sleep(100);//millis
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
	
}
