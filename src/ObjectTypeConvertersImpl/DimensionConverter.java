package ObjectTypeConvertersImpl;

import java.awt.Dimension;

import ObjectTypeConversion.StringToObjectConverter;
import Properties.LoggingMessages;

public class DimensionConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 2;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return Dimension.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new Dimension();
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return LoggingMessages.combine(args);
	}

}
