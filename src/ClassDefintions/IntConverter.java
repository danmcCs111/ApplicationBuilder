package ClassDefintions;

public class IntConverter implements StringToObjectConverter {
	public static int getInt(String arg0)
	{
		return Integer.parseInt(arg0);
	}

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Object conversionCall(String... args) {
		return getInt(args[0]);
	}
	
	@Override
	public String toString()
	{
		return IntConverter.class.toString();
	}
}
