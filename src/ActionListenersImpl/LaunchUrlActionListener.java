package ActionListenersImpl;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

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
		CHROME_HIDE_OPTION = "--hide-crash-restore-bubble",
		CHROME_PROFILE_OPTION = "--user-data-dir=" + PathUtility.getCurrentDirectory() + "-ChromeProfile",
		CHROME_NO_DEFAULT_CHECK = "--no-default-browser-check",
		CHROME_KIOSK = "--kiosk",
		AHK_RELATIVE_PATH = "./plugin-projects/AutoHotKey-Utils/pid.txt";
	public static final String
		CLOSE_LAUNCH_ACTION_EVENT="closeLaunchAction";
	private static String
		processWindows = PROCESS_WINDOWS,
		processLinux = PROCESS_LINUX;
	
	private static HashMap<Integer, ProcessHandle>
		runningProcesses = new HashMap<Integer, ProcessHandle>();
	private static ArrayList<AbstractButton> 
		lastButtons = new ArrayList<AbstractButton>();
	private static AbstractButton
		lastButtonOrigin;
	private static boolean 
		executing = false,
		isKiosk = false;
	private static int
		defaultId = -1;
	private static ArrayList<ArrayActionListener> 
		aals = new ArrayList<ArrayActionListener>();
		
	
	public static void addArrayActionListener(ArrayActionListener aal)
	{
		aals.add(aal);
	}
	public static void removeArrayActionListener(ArrayActionListener aal)
	{
		aals.remove(aal);
	}
	
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
			executePrimaryProcess(args, button);
		}
		storeLast(button);
	}
	
	public static void performHighlight(AbstractButton button)
	{
		if(button.getName().equals(CLOSE_LAUNCH_ACTION_EVENT))
		{
			closeEvent();
		}
		else
		{
			notifyActionListeners(button);
		}
	}
	
	private static void closeEvent()
	{
		for(AbstractButton lastButton : lastButtons)
		{
			if(lastButton instanceof JButtonLengthLimited)
			{
				PicLabelMouseListener.highLightLabel((JButtonLengthLimited) lastButton, false);//TODO interface?
			}
		}
		notifyActionListeners(null);
	}
	
	public static void notifyActionListeners(AbstractButton button)
	{
		for(ArrayActionListener aal : aals)
		{
			aal.urlSelect(button);
		}
	}
	
	private static String [] buildCommand(AbstractButton button)
	{
		return buildCommand(button, defaultId);
	}
	
	public static String [] buildCommand(Component comp, int id)
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
				comp.getName()	
			};
		}
		else
		{
			args = new String [] {
				PathUtility.isWindows()?getProcessWindowsOS():getProcessLinuxOS(), 
				CHROME_HIDE_OPTION, chromeProfile, CHROME_NO_DEFAULT_CHECK, 
				comp.getName()
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
	
	public static long getProcessId()
	{
		ProcessHandle runningProcess = runningProcesses.get(defaultId);
		if(runningProcess != null && runningProcess.isAlive())
		{
			return runningProcess.pid();
		}
		return -1;
	}
	
	public static boolean setProcess(long processId, int id)
	{
		Optional<ProcessHandle> optionalHandle = ProcessHandle.of(processId);
		if(optionalHandle.isPresent())
		{
			ProcessHandle ph = optionalHandle.get();
			runningProcesses.put(id, ph);
			return true;
		}
		return false;
	}
	
	private static void executePrimaryProcess(String [] args, AbstractButton ab)
	{
		executing = true;
		try {
			destroyRunningProcess(defaultId);
			ProcessBuilder pb = new ProcessBuilder(args);
			ProcessHandle runningProcess = runningProcesses.get(defaultId);
			runningProcess = pb.start().toHandle();
			Long pid = runningProcess.pid();
			File f = new File(new DirectorySelection(AHK_RELATIVE_PATH).getFullPath());
			PathUtility.writeStringToFile(f, pid + "");
			runningProcesses.put(defaultId, runningProcess);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		executing = false;
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
	
	public static void bootCheckRunningProcess()
	{
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					ProcessHandle runningProcess = runningProcesses.get(defaultId);
					if(runningProcess != null && !runningProcess.isAlive() && !executing)
					{
						closeEvent();
						storeLast(null);
					}
					try {
						Thread.sleep(1000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public static boolean destroyRunningProcess()
	{
		return destroyRunningProcess(defaultId);
	}
	
	public static boolean destroyRunningProcess(int id)
	{
		ProcessHandle runningProcess = runningProcesses.get(id);
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
