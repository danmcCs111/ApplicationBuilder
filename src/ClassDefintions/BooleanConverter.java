package ClassDefintions;

public class BooleanConverter implements StringToObjectConverter 
{

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Object conversionCall(String... args) {
		return Boolean.parseBoolean(args[0]);
	}

	@Override
	public Class<?> getDefinitionClass() {
		return boolean.class;
	}

}
