package ObjectTypeConversion;

import java.util.ArrayList;

import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.ParamOption;
import WidgetComponentInterfaces.ParamOption.PathModifier;
import WidgetComponents.Parameter;

public class CommandBuild 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = "@",
		DELIMITER_PARAMETER_OPTION = "|",
		DELIMITER_PARAMETER_TYPE = "%";
	
	private String 
		command,
		commandXmlArg;
	private String [] 
		commandLineOptions;
	private ArrayList<Parameter> parameters = new ArrayList<Parameter>();
	
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
				parametersTmp = arg.split(PathUtility.ESCAPE_CHARACTER + DELIMITER_PARAMETER_OPTION);
		
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
			String pT = parametersTmp[i];
			Parameter parameter = new Parameter();
			parameters.add(convertParamType(pT, parameter));  
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
	}
	
	private Parameter convertParamType(String pT, Parameter parameter)
	{
		String [] pars;
		if(pT.contains(DELIMITER_PARAMETER_TYPE))
		{
			pars = pT.split(DELIMITER_PARAMETER_TYPE);
		}
		else
		{
			pars = new String[] {pT};
		}
		
		for(String par : pars)
		{
			if(par.length() < 2)
				continue;
			String typeXml = par.substring(0, 1);
			ParamOption po = ParamOption.getParamOption(typeXml);
			String content = par.substring(2, par.length());
			PathModifier pMod = PathModifier.getModifier(par.substring(1, 2));
			LoggingMessages.printOut(par);
			switch(po)
			{
			case TextField:
				parameter.addParamString(content);
				break;
			case Directory:
				parameter.addParamDirectory(new DirectorySelection(content, pMod));
				break;
			case File:
				parameter.addParamFile(new FileSelection(content, pMod));
				break;
			}
		}
		
		return parameter;
	}
	
	public void setCommand(String command, String [] commandLineOptions, String [] parameters)
	{
		this.command = command;
		this.commandLineOptions = commandLineOptions;
		ArrayList<Parameter> tmpParameters = new ArrayList<Parameter>();
		for(String param : parameters)
		{
			Parameter tmpP = new Parameter();
			tmpP.addParamString(param);
			tmpParameters.add(tmpP);
		}
		this.parameters = tmpParameters;
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
		String [] retParams = new String [parameters.size()];
		int count = 0;
		for(Parameter pm : parameters)
		{
			retParams[count] = pm.getCommandBuildString();
			count++;
		}
		return retParams;
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
		
		String [] args = new String[1 + commandLineOptions.length + parameters.size()];
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
		for(int i = 0; i < parameters.size(); i++)
		{
			if(isWindows)
			{
				args[count] = PathUtility.surroundString(parameters.get(i).getCommandBuildString(), "\"");
			}
			else
			{
				args[count] = parameters.get(i).getCommandBuildString();
			}
			count++;
		}
		
		return args;
	}
	
}
