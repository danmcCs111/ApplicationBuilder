package ObjectTypeConversion;

import java.util.ArrayList;

public class ParseAttribute 
{
	public ArrayList<String> values = new ArrayList<String>();
	private String name;
	
	public ParseAttribute(String name)
	{
		this.name = name;
	}
	
	public String name()
	{
		return name;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ParseAttribute)
			return name().equals(((ParseAttribute) o).name());
		else
			return false;
	}
}
