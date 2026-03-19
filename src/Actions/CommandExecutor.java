package Actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;

public class CommandExecutor 
{
	
	public static void executeProcess(CommandBuild commandBuild) throws IOException
	{
		executeProcess(commandBuild, false);
	}
	
	public static void executeProcess(CommandBuild commandBuild, boolean haltTillComplete) throws IOException
	{
		Process runningProcess = null;
		ProcessBuilder pb = new ProcessBuilder(commandBuild.getArgs());
		pb.redirectErrorStream(true);
		runningProcess = pb.start();
		LoggingMessages.printOut(commandBuild.getArgs());
		LoggingMessages.printOut(" - executed");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(runningProcess.getInputStream())))
		{
			//required for java jar launch
		}
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
