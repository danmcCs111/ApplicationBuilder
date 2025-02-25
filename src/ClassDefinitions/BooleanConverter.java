package ClassDefinitions;

public class BooleanConverter implements StringToObjectConverter 
{

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: Boolean.parseBoolean(args[0]);
	}

	@Override
	public Class<?> getDefinitionClass() {
		return boolean.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return false;
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
