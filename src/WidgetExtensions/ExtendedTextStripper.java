package WidgetExtensions;

import ActionListeners.ArrayActionListener;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTextStripper implements ExtendedAttributeStringParam
{
	private String 
		textStrip = "";
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties)
	{
		ArrayActionListener aal = (ArrayActionListener) widgetProperties.getInstance();
		textStrip = arg0;
		applyTextStripper(aal);
	}
	
	public void applyTextStripper(ArrayActionListener aal)
	{
		for(String s : textStrip.split(ARG_DELIMITER))
		{
			aal.addStripFilter(s);
		}
	}

}
