package ApplicationBuilder;

import java.io.IOException;

import Properties.LoggingMessages;

public class ShellExecutor 
{
	public static String 
		WINDOWS_BASH_SHELL_LOCATION = "C:\\Program Files\\Git\\git-bash.exe",
		WINDOWS_COMMAND_OPTION = "-c",
		LINUX_BASH_SHELL = "bash";
	
	public static void main(String [] args)
	{
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		int count = 0;
		String [] argsAll;
		if(isWindows)
		{
			argsAll = new String[args.length + 2];
			argsAll[count] = WINDOWS_BASH_SHELL_LOCATION;
			count++;
			argsAll[count] = WINDOWS_COMMAND_OPTION;
			count++;
			for(String s : args)
			{
				argsAll[count] = s;
				count++;
			}
		}
		else
		{
			argsAll = new String[args.length + 1];
			argsAll[count] = LINUX_BASH_SHELL;
			count++;
			for(String s : args)
			{
				argsAll[count] = s;
				count++;
			}
		}
		LoggingMessages.printOut(argsAll);
		ProcessBuilder pb = new ProcessBuilder(argsAll);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
