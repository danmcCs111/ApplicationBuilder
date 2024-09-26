package ApplicationBuilder;

import java.io.IOException;
import java.lang.ProcessHandle;

import javax.swing.SwingUtilities;

public class CommandLauncher {
	private static Process runningProcess = null;
	
	public static void executeProcess(String ...args)
	{
		try {
			destroyRunningProcess();
			ProcessBuilder pb = new ProcessBuilder(args);
			runningProcess = pb.start();
			
		} catch (IOException e) {
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
	
	public static void closeProcess()
	{
		destroyRunningProcess();
	}
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(() -> {
			 BuilderWindow window = new BuilderWindow();
		        window.setVisible(true);
		});
	}
}
