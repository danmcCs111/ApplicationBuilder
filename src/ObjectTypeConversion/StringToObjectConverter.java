package ObjectTypeConversion;

public interface StringToObjectConverter 
{
	public abstract int numberOfArgs();
	public abstract Class<?> getDefinitionClass();
	public abstract Object getDefaultNullValue();
	public abstract Object conversionCall(String ... args);
	public abstract String conversionCallStringXml(String ... args);
	public default boolean conversionCallIsBlankCheck(String ... args)
	{
		if((args == null || args.length == 0) && numberOfArgs() > 0)
		{
			return true;
		}
		for(int i = 0; i < numberOfArgs(); i++)
		{
			if(args[i] == null || args[i].isBlank())
			{
				return true;
			}
		}
		return false;
	}
	
}
