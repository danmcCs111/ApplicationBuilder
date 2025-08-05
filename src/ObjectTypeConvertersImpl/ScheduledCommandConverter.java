package ObjectTypeConvertersImpl;

import Actions.ScheduledCommand;
import ObjectTypeConversion.StringToObjectConverter;
import Properties.LoggingMessages;

public class ScheduledCommandConverter implements StringToObjectConverter
{

	@Override
	public int numberOfArgs() 
	{
		return 2;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return ScheduledCommand.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new ScheduledCommand();
	}

	@Override
	public Object conversionCall(String... args) {
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new ScheduledCommand(args);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return LoggingMessages.combine(args);
	}

}
