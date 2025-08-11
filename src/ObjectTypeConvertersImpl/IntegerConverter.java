package ObjectTypeConvertersImpl;

public class IntegerConverter extends IntConverter 
{
	@Override
	public String toString()
	{
		return IntegerConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return Integer.class;
	}
}
