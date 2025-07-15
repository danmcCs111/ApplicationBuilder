package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import ActionListeners.CommandBuildListener;
import Actions.CommandExecutor;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;

public class ReloadExecuteProcessActionListener implements ActionListener, CommandBuildListener
{
	private CommandBuild commandBuild;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LoggingMessages.printOut("Reload");
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

	@Override
	public void setCommandBuild(CommandBuild commandBuild) 
	{
		this.commandBuild = commandBuild;
	}
}

