package ObjectTypeConvertersImpl;

import ObjectTypeConversion.PageParserCollection;
import ObjectTypeConversion.StringToObjectConverter;

public class PageParserCollectionConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return PageParserCollection.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new PageParserCollection("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
			? getDefaultNullValue()
			: new PageParserCollection(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
