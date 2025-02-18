package WidgetExtensions;


import java.awt.event.ActionListener;

import ClassDefintions.ActionListenerConverter;
import WidgetComponents.ArrayActionListener;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerArray implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ArrayActionListener aal = ArrayActionListener.findInstanceOfArrayActionListener();
		ActionListener al = ActionListenerConverter.getActionListener(arg0);
		aal.addActionListener(al);
	}

}
