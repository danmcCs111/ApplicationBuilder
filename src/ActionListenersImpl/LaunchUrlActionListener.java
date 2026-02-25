package ActionListenersImpl;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	private static final String
		PROCESS_WINDOWS = "chrome.exe",
		PROCESS_LINUX = "google-chrome",
		CLOSE_LAUNCH_ACTION_EVENT="closeLaunchAction",
		CHROME_HIDE_OPTION = "--hide-crash-restore-bubble",
		CHROME_PROFILE_OPTION = "--user-data-dir=" + PathUtility.getCurrentDirectory() + "-ChromeProfile",
		CHROME_NO_DEFAULT_CHECK = "--no-default-browser-check",
		CHROME_KIOSK = "--kiosk",
		AHK_RELATIVE_PATH = "./plugin-projects/AutoHotKey-Utils/pid.txt";
	private static String
		processWindows = PROCESS_WINDOWS,
		processLinux = PROCESS_LINUX;
	
	private static HashMap<Integer, Process>
		runningProcesses = new HashMap<Integer, Process>();
	private static ArrayList<AbstractButton> 
		lastButtons = new ArrayList<AbstractButton>();
	private static AbstractButton
		lastButtonOrigin;
	private static boolean 
		isKiosk = false;
	private static int 
		defaultId = -1;
	
	public static String getProcessWindowsOS()
	{
		return processWindows;
	}
	public static void setProcessWindowsOS(String windowsProc)
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
	
	public static String getProcessLinuxOS()
	{
		return processLinux;
	}
	public static void setProcessLinuxOS(String linuxProc)
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
			destroyRunningProcess(defaultId);
		}
		
		performHighlight(button);
		
		if(!button.getName().equals(CLOSE_LAUNCH_ACTION_EVENT))
		{
			String [] args = buildCommand(button);
			executePrimaryProcess(args);
		}
		storeLast(button);
	}
	
	private static void performHighlight(AbstractButton button)
	{
		if(button.getName().equals(CLOSE_LAUNCH_ACTION_EVENT))
		{
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
						if(lastButtonParent != null && !lastButtonParent.equals(button.getParent()) 
								&& lastButtonParent instanceof ArrayActionListener)
						{
							ArrayActionListener aal = (ArrayActionListener)lastButtonParent;
							aal.unselect();
						}
					}
				}
			}
		}
	}
	
	private static String [] buildCommand(AbstractButton button)
	{
		return buildCommand(button, defaultId);
	}
	
	public static String [] buildCommand(AbstractButton button, int id)
	{
		String chromeProfile = (id == -1)
				?CHROME_PROFILE_OPTION
				:CHROME_PROFILE_OPTION + id;
		String [] args = null;
		if(LaunchUrlActionListener.isKiosk)
		{
			args = new String [] {
				PathUtility.isWindows()?getProcessWindowsOS():getProcessLinuxOS(), 
				CHROME_HIDE_OPTION, chromeProfile, CHROME_NO_DEFAULT_CHECK, CHROME_KIOSK, 
				button.getName()	
			};
		}
		else
		{
			args = new String [] {
				PathUtility.isWindows()?getProcessWindowsOS():getProcessLinuxOS(), 
				CHROME_HIDE_OPTION, chromeProfile, CHROME_NO_DEFAULT_CHECK, 
				button.getName()
			};
		}
		
		return args;
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
	
	private static void executePrimaryProcess(String [] args)
	{
		try {
			destroyRunningProcess(defaultId);
			ProcessBuilder pb = new ProcessBuilder(args);
			Process runningProcess = runningProcesses.get(defaultId);
			runningProcess = pb.start();
			Long pid = runningProcess.pid();
			File f = new File(new DirectorySelection(AHK_RELATIVE_PATH).getFullPath());
			PathUtility.writeStringToFile(f, pid + "");
			runningProcesses.put(defaultId, runningProcess);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void executeProcess(int id, String ...args)
	{
		try {
			ProcessBuilder pb = new ProcessBuilder(args);
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean destroyRunningProcess()
	{
		return destroyRunningProcess(defaultId);
	}
	
	public static boolean destroyRunningProcess(int id)
	{
		Process runningProcess = runningProcesses.get(id);
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
