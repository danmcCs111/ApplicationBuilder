package ObjectTypeConvertersImpl;

import Actions.ScheduledCommand;
import ObjectTypeConversion.StringToObjectConverter;

public class ScheduledCommandConverter implements StringToObjectConverter
{

	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return ScheduledCommand.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new ScheduledCommand("");
	}

	@Override
	public Object conversionCall(String... args) {
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new ScheduledCommand(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
