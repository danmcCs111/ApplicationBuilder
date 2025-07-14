package ObjectTypeConvertersImpl;

import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.StringToObjectConverter;

public class CommandBuildConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return CommandBuild.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new CommandBuild("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new CommandBuild(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
	
}
