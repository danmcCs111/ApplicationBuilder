package WidgetExtensions;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import ActionListeners.ActionListenersRegistered;

public class ExtendedActionListenerSubType 
{
	private static final String METHOD_DEF = "extendedActionListenerSubType [java.lang.String arg0]";

	public static String getMethodDefinition() 
	{
		return METHOD_DEF;
	}
	
	public static void applyMethod(JComponent component, String listenerSubType)
	{
		ActionListener [] al = component.getListeners(ActionListener.class);
		applySubTypeActionListener(al, listenerSubType);
	}
	
	private static void applySubTypeActionListener(ActionListener [] actionListener, String listenerSubType)
	{
		for(ActionListener al : actionListener)
		{
			ActionListenersRegistered alr = ActionListenersRegistered.getActionListener(al.getClass().getName());
		}
	}
}
