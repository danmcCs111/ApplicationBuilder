package WidgetExtensionsImpl;


import java.awt.event.ActionListener;

import ActionListeners.ArrayActionListener;
import ObjectTypeConvertersImpl.ActionListenerConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerArray implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ArrayActionListener aal = ArrayActionListener.findInstanceOfArrayActionListener();
		ActionListener al = ActionListenerConverter.getActionListener(arg0);
		aal.addActionListener(al);
	}

}
