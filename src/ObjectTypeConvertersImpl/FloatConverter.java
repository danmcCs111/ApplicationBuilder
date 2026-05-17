package ObjectTypeConvertersImpl;

import ObjectTypeConversion.StringToObjectConverter;

public class FloatConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return float.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return 0f;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: Float.parseFloat(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
