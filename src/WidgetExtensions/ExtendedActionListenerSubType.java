package WidgetExtensions;

import java.awt.event.ActionListener;
import javax.swing.JComponent;

import ActionListeners.ActionListenerSubTypeExtension;
import ActionListeners.ActionListenersRegistered;
import ApplicationBuilder.LoggingMessages;

public class ExtendedActionListenerSubType implements ExtendedAttributeStringParam
{
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
			if(alr.acceptsActionListenerSubType())
			{
				ActionListenerSubTypeExtension ale = (ActionListenerSubTypeExtension) al;
			}
		}
	}
}
