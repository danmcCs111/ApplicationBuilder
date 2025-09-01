package WidgetExtensionsImpl;

import javax.swing.JScrollPane;

import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedScrollBarSetUnit implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		JScrollPane jsp = (JScrollPane) widgetProperties.getInstance();
		jsp.getVerticalScrollBar().setUnitIncrement(Integer.parseInt(arg0));
	}

}
