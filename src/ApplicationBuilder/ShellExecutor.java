package ApplicationBuilder;

import java.io.IOException;

import Actions.CommandExecutor;
import ObjectTypeConversion.CommandBuild;

public class ShellExecutor 
{
	public static String 
		WINDOWS_BASH_SHELL_LOCATION = "C:\\Program Files\\Git\\git-bash.exe",//TODO
		LINUX_BASH_SHELL = "bash";
	public static String [] 
			LINUX_COMMAND_OPTION = new String [] {"-c", "-i"},
			WINDOWS_COMMAND_OPTION = new String [] {"-c"};
	
	public static void main(String [] args) 
	{
		boolean isWindows = System.getProperty("os.name").startsWith("Windows");
		CommandBuild cb;
		if(isWindows)
		{
			cb = new CommandBuild();
			cb.setCommand(WINDOWS_BASH_SHELL_LOCATION, WINDOWS_COMMAND_OPTION, args);
		}
		else
		{
			cb = new CommandBuild();
			cb.setCommand(LINUX_BASH_SHELL, LINUX_COMMAND_OPTION, args);
		}
		try {
			CommandExecutor.executeProcess(cb, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
