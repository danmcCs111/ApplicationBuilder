package WidgetExtensions;

import java.awt.Font;

import javax.swing.JComponent;

import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetFontSize implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		int size = Integer.parseInt(arg0);
		if(m instanceof JComponent)
		{
			((JComponent) m).setFont(new Font(Font.SANS_SERIF, Font.PLAIN, size));
		}
	}
}
