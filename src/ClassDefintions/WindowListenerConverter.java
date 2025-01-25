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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
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
