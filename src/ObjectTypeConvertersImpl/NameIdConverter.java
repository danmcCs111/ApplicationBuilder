package ObjectTypeConvertersImpl;

import ObjectTypeConversion.NameId;
import ObjectTypeConversion.StringToObjectConverter;

public class NameIdConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}
	
	@Override
	public String toString()
	{
		return NameIdConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return NameId.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new NameId(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
