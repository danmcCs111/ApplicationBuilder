package WidgetExtensions;


import java.awt.event.ActionListener;

import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.ActionListenerConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerArray implements ExtendedAttributeStringParam 
{

	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		ArrayActionListener aal = ArrayActionListener.findInstanceOfArrayActionListener(wbc);
		ActionListener al = ActionListenerConverter.getActionListener(arg0);
		aal.addActionListener(al);
	}

}
