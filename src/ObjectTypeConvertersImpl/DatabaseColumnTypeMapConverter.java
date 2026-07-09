package ObjectTypeConvertersImpl;

import ObjectTypeConversion.StringToObjectConverter;
import Properties.DatabaseColumnTypeMap;

public class DatabaseColumnTypeMapConverter implements StringToObjectConverter
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
				: new DatabaseColumnTypeMap(args[0]);
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return DatabaseColumnTypeMap.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new DatabaseColumnTypeMap("");
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
