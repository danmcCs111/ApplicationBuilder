package ClassDefintions;

import java.awt.event.ActionListener;

import ActionListeners.ActionListenersRegistered;

public class ActionListenerConverter implements StringToObjectConverter {
	
	public static ActionListener getActionListener(String arg0)
	{
		Class<? extends ActionListener> actionListener = ActionListenersRegistered.getActionListener(arg0).getClazz();
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
		return al;
	}

	@Override
	public int numberOfArgs() {
		return 1;
	}
}
