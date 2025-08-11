package ObjectTypeConvertersImpl;

import java.sql.Timestamp;

import ObjectTypeConversion.StringToObjectConverter;

public class TimestampConverter implements StringToObjectConverter 
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
			:new Timestamp(Long.parseLong(args[0]));
	}
	
	@Override
	public String toString()
	{
		return TimestampConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return Timestamp.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new Timestamp(0L);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
