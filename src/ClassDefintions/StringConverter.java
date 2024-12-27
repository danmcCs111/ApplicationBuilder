package ClassDefintions;

public class StringConverter implements StringToObjectConverter 
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return args[0];
	}
	
	@Override
	public String toString()
	{
		return StringConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return String.class;
	}
}
