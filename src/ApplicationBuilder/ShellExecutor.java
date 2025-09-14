package ApplicationBuilder;

import java.io.IOException;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class ShellExecutor 
{
	public static String 
		WINDOWS_BASH_SHELL_LOCATION = "C:\\Program Files\\Git\\git-bash.exe",//TODO
		LINUX_BASH_SHELL = "bash",
		INITIAL_COMMAND = "cd " + PathUtility.getCurrentDirectoryUnix() + "; ";
	public static String [] 
			LINUX_COMMAND_OPTION = new String [] {"-c", "-i"},
			WINDOWS_COMMAND_OPTION = new String [] {"-c"};
	
	public static void main(String [] args)
	{
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		int count = 0;
		String [] argsAll;
		if(isWindows)
		{
			argsAll = new String[args.length + WINDOWS_COMMAND_OPTION.length + 1];
			argsAll[count] = WINDOWS_BASH_SHELL_LOCATION;
			count++;
			for(String op : WINDOWS_COMMAND_OPTION)
			{
				argsAll[count] = op;
				count++;
			}
			for(String s : args)
			{
				argsAll[count] = INITIAL_COMMAND + s;
				count++;
			}
		}
		else
		{
			argsAll = new String[args.length + LINUX_COMMAND_OPTION.length + 1];
			argsAll[count] = LINUX_BASH_SHELL;
			count++;
			for(String op : LINUX_COMMAND_OPTION)
			{
				argsAll[count] = op;
				count++;
			}
			for(String s : args)
			{
				argsAll[count] = INITIAL_COMMAND + s;
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
