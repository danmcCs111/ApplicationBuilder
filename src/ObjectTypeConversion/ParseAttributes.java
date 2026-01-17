package ObjectTypeConversion;

import java.util.ArrayList;

public class ParseAttributes 
{
	public ArrayList<ParseAttribute> parseAttributes = new ArrayList<ParseAttribute>();
	
	public ParseAttributes(ParseAttribute ... parseAttributes)
	{
		for(ParseAttribute pa : parseAttributes)
		{
			addAttribute(pa);
		}
	}
	
	public ParseAttribute valueOf(String val)
	{
		for(ParseAttribute pa : parseAttributes)
		{
			if(pa.name().equals(val))
			{
				return pa;
			}
		}
		return null;
	}
	
	public void addAttribute(ParseAttribute parseAttribute)
	{
		if(!parseAttributes.contains(parseAttribute))
		{
			parseAttributes.add(parseAttribute);
		}
	}
	
	public void removeAttribute(ParseAttribute parseAttribute)
	{
		parseAttributes.remove(parseAttribute);
	}
	
}
