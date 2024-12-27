package ClassDefintions;

import java.awt.event.ActionListener;

import ActionListeners.ActionListenersRegistered;
import ApplicationBuilder.LoggingMessages;

public class ActionListenerConverter implements StringToObjectConverter 
{
	public static ActionListener getActionListener(String arg0)
	{
		ActionListenersRegistered alr = ActionListenersRegistered.getActionListener(arg0);
		Class<? extends ActionListener> actionListener = alr.getClazz();
		ActionListener al = null;
		try {
			al = actionListener.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoggingMessages.printOut("ActionListener name: " + alr.name());
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
