package ApplicationBuilder;

import java.io.IOException;
import java.lang.ProcessHandle;

import javax.swing.SwingUtilities;

public class CommandLauncher {
	private static Process rokuChannelProcess = null;
	
	public static void executeProcess(String ...args)
	{
		try {
			destroyRunningProcess();
			ProcessBuilder pb = new ProcessBuilder(args);
			rokuChannelProcess = pb.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean destroyRunningProcess()
	{
		if(rokuChannelProcess != null)
		{
			rokuChannelProcess.destroy();
			rokuChannelProcess.descendants().forEach(ProcessHandle::destroy);
			return true;
		}
		return false;
	}
	
	public static void closeRokuVideo()
	{
		destroyRunningProcess();
	}
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(() -> {
			 CommandLauncherWindow window = new CommandLauncherWindow();
		        window.setVisible(true);
		});
	}
}
