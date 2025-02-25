package ClassDefinitions;

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
		return conversionCallIsBlankCheck(args)
			? getDefaultNullValue()
			:args[0];
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

	@Override
	public Object getDefaultNullValue() 
	{
		return "";
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
