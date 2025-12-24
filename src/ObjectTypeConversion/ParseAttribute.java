package ObjectTypeConversion;

public class ParseAttribute 
{
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
