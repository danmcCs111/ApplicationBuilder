package ApplicationBuilder;

public class ShellHeadlessExecutor extends ShellExecutor 
{
	static {
		WINDOWS_COMMAND_OPTION = new String [] {"--hide", "-c"};
	}
}
