package ObjectTypeConversion;

public class CommandBuild 
{
	private static final String 
		DELIMITER_COMMANDLINE_OPTION = "@",
		DELIMITER_PARAMETER_OPTION = "\\|";
	
	private String 
		command;
	private String [] 
		parameters,
		commandLineOptions;
	
	public CommandBuild(String arg)
	{
		String command = "";
		String [] 
				commandLineTmp = arg.split(DELIMITER_COMMANDLINE_OPTION),
				commandLine = new String [commandLineTmp.length - 1],
				parametersTmp = arg.split(DELIMITER_PARAMETER_OPTION),
				parameters = new String [parametersTmp.length - 1];
		
		int index = commandLineTmp[commandLineTmp.length-1].indexOf("|");
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
	
	public void setCommand(String command, String [] parameters, String [] commandLineOptions)
	{
		this.command = command;
		this.parameters = parameters;
		this.commandLineOptions = commandLineOptions;
	}
	
	public String [] getArgs()
	{
		String [] args = new String[1 + commandLineOptions.length + parameters.length];
		args[0] = this.command;
		int count = 1;
		for(int i = 0; i < commandLineOptions.length; i++)
		{
			args[count] = commandLineOptions[i];
			count++;
		}
		for(int i = 0; i < parameters.length; i++)
		{
			args[count] = parameters[i];
			count++;
		}
		
		return args;
	}
	
//	public static void main (String [] args)
//	{
//		CommandBuild cb = new CommandBuild("new.sh @-i @-c |this |is |it");
//		LoggingMessages.printOut(cb.getArgs());
//		
//		CommandBuild cb2 = new CommandBuild("new.sh @-i @-c |this is it");
//		LoggingMessages.printOut(cb2.getArgs());
//		
//		CommandBuild cb3 = new CommandBuild("new.sh |this is it");
//		LoggingMessages.printOut(cb3.getArgs());
//		
//		CommandBuild cb4 = new CommandBuild("new.sh ");
//		LoggingMessages.printOut(cb4.getArgs());
//	}
}
