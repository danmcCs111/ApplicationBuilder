package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Actions.CommandExecutor;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;

public class CommandBuildListener implements ActionListener
{
	private CommandBuild commandBuild;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		try {
			if(commandBuild != null)
			{
				LoggingMessages.printOut(commandBuild.getArgs());
				CommandExecutor.executeProcess(commandBuild);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setCommandBuild(CommandBuild commandBuild) 
	{
		this.commandBuild = commandBuild;
	}
}
