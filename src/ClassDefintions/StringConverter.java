package ClassDefintions;

public class StringConverter implements StringToObjectConverter {
	
	public static String getString(String arg0)
	{
		return arg0;
	}

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Object conversionCall(String... args) {
		return getString(args[0]);
	}
	
	@Override
	public String toString()
	{
		return StringConverter.class.toString();
	}
}
