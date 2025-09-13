package ObjectTypeConvertersImpl;

import ObjectTypeConversion.PathArrayProcessing;
import ObjectTypeConversion.StringToObjectConverter;

public class PathArrayProcessingConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}
	
	@Override
	public String toString()
	{
		return PathArrayProcessingConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return PathArrayProcessing.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new PathArrayProcessing("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new PathArrayProcessing(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
	
}
