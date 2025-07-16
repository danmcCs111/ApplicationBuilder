package Actions;

import java.io.IOException;

import ObjectTypeConversion.CommandBuild;

public class CommandExecutor 
{
	private static Process runningProcess = null;
	
	public static void executeProcess(CommandBuild commandBuild) throws IOException
	{
		executeProcess(commandBuild, false);
	}
	
	public static void executeProcess(CommandBuild commandBuild, boolean haltTillComplete) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder(commandBuild.getArgs());
		runningProcess = pb.start();
		if(haltTillComplete)
		{
			while(runningProcess.isAlive())
			{
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
