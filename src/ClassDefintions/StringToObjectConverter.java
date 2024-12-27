package ClassDefintions;

public interface StringToObjectConverter 
{
	public abstract int numberOfArgs();
	public abstract Object conversionCall(String ... args);
	public abstract Class<?> getDefinitionClass();
	
}
