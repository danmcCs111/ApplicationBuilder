package ObjectTypeConvertersImpl;

import ObjectTypeConversion.CsvReader;
import ObjectTypeConversion.StringToObjectConverter;

public class CsvReaderConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return CsvReader.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new CsvReader("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new CsvReader(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
