package ClassDefinitions;

public class DirectorySelectionConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return DirectorySelection.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return "";
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new DirectorySelection(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
	
}
