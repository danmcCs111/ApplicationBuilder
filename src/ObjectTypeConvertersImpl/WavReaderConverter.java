package ObjectTypeConvertersImpl;

import ObjectTypeConversion.StringToObjectConverter;
import ObjectTypeConversion.WavReader;

public class WavReaderConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return WavReader.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new WavReader("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new WavReader(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
