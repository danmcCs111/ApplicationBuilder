package ObjectTypeConversion;

import Properties.PathUtility;

public class CommandBuild 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = "@",
		DELIMITER_PARAMETER_OPTION = "|";
	
	private String 
		command,
		commandXmlArg;
	private String [] 
		parameters,
		commandLineOptions;
	
	public CommandBuild(String arg)
	{
		commandXmlArg = arg;
		if(!arg.contains(DELIMITER_COMMANDLINE_OPTION) && !arg.contains(DELIMITER_PARAMETER_OPTION))
		{
			command = arg;
			return;
		}
		String command = "";
		String [] 
				commandLineTmp = arg.split(DELIMITER_COMMANDLINE_OPTION),
				commandLine = new String [commandLineTmp.length - 1],
				parametersTmp = arg.split(PathUtility.ESCAPE_CHARACTER + DELIMITER_PARAMETER_OPTION),
				parameters = new String [parametersTmp.length - 1];
		
		int index = commandLineTmp[commandLineTmp.length-1].indexOf(DELIMITER_PARAMETER_OPTION);
		if(index >= 0) commandLineTmp[commandLineTmp.length-1] = commandLineTmp[commandLineTmp.length-1].substring(0, index);
		
		int count = 0;
		int commandOptionsStartIndex = 1;
		for(int i = commandOptionsStartIndex; i < commandLineTmp.length; i++)
		{
			commandLine[count] = commandLineTmp[i];
			count++;
		}
		count = 0;
		int paramStartIndex = 1;
		for(int i = paramStartIndex; i < parametersTmp.length; i++)
		{
			parameters[count] = parametersTmp[i];
			count++;
		}
		
		if(commandLineTmp.length > 1)
			command = commandLineTmp[0];
		else if (parametersTmp.length > 1)
			command = parametersTmp[0];
		else
			command = arg;
		
		this.command = command;
		this.commandLineOptions = commandLine;
		this.parameters = parameters;
	}
	
	public void setCommand(String command, String [] commandLineOptions, String [] parameters)
	{
		this.command = command;
		this.commandLineOptions = commandLineOptions;
		this.parameters = parameters;
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public String [] getCommandOptions()
	{
		return commandLineOptions;
	}
	
	public String [] getParameters()
	{
		return parameters;
	}
	
	public String getCommandXmlString()
	{
		return this.commandXmlArg;
	}
	
	public String [] getArgs()
	{
		boolean isWindows = System.getProperty("os.name").contains("Windows");
		
		if(commandLineOptions == null || parameters == null)
		{
			return new String [] {this.command};
		}
		
		String [] args = new String[1 + commandLineOptions.length + parameters.length];
		if(isWindows)
		{
			args[0] = PathUtility.surroundString(this.command , "\"");
		}
		else
		{
			args[0] = this.command;
		}
		int count = 1;
		for(int i = 0; i < commandLineOptions.length; i++)
		{
			args[count] = commandLineOptions[i];
			count++;
		}
		for(int i = 0; i < parameters.length; i++)
		{
			if(isWindows)
			{
				args[count] = PathUtility.surroundString(parameters[i], "\"");
			}
			else
			{
				args[count] = parameters[i];
			}
			count++;
		}
		
		return args;
	}
	
}
