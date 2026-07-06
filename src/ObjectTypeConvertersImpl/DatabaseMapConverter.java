package ObjectTypeConvertersImpl;

import ObjectTypeConversion.StringToObjectConverter;
import Properties.DatabaseMap;

public class DatabaseMapConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new DatabaseMap(args[0]);
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return DatabaseMap.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new DatabaseMap();
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
