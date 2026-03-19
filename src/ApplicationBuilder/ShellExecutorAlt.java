package ApplicationBuilder;

public class ShellExecutorAlt extends ShellExecutor 
{
	static {
		LINUX_BASH_SHELL = "gnome-terminal";
		LINUX_COMMAND_OPTION = new String [] {"--", "bash", "-c"};
	}
}
