package ClassDefintions;

import java.awt.event.WindowListener;

public class WindowListenerConverter implements StringToObjectConverter
{
	
	@Override
	public String toString()
	{
		return ActionListenerConverter.class.toString();
	}
	
	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		Class<?> c;
		try {
			c = Class.forName(args[0]);
			return c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return WindowListener.class;
	}

}
