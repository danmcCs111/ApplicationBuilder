package ObjectTypeConvertersImpl;

import HttpDatabaseRequest.PageParser;
import ObjectTypeConversion.StringToObjectConverter;

public class PageParserConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return PageParser.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return new PageParser("");
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new PageParser(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
