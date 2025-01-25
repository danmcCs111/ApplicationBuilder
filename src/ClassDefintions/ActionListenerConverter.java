package ClassDefintions;

import java.awt.event.ActionListener;

import ApplicationBuilder.LoggingMessages;

public class ActionListenerConverter implements StringToObjectConverter 
{
	public static ActionListener getActionListener(String arg0)
	{
		ActionListener al = null;
		try {
			Class<?> c = Class.forName(arg0);
			al = (ActionListener) c.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoggingMessages.printOut("ActionListener name: " + al.getClass().toString());
		return al;
	}

	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return getActionListener(args[0]);
	}
	
	@Override
	public String toString()
	{
		return ActionListenerConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return ActionListener.class;
	}
}
