package ObjectTypeConvertersImpl;

import ObjectTypeConversion.StringToObjectConverter;

public class DoubleConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return double.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return false;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: Double.parseDouble(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
	
}
