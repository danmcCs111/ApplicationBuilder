package ClassDefinitions;

public class FileSelectionConverter implements StringToObjectConverter
{
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return FileSelection.class;
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
				: new FileSelection(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}

}
