package WidgetExtensions;

import java.awt.Color;

import Graphics2D.ColorTemplate;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetBackgroundPanelColorTemplate implements ExtendedAttributeParam 
{
	public void applyMethod(Color arg0, WidgetCreatorProperty widgetProperties) 
	{
		ColorTemplate.setPanelBackgroundColor(arg0);
	}

}
