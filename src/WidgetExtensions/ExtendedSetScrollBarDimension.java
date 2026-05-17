package WidgetExtensions;

import java.awt.Dimension;

import javax.swing.UIManager;

import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetScrollBarDimension implements ExtendedAttributeParam   
{
	public void applyMethod(Dimension arg0, WidgetCreatorProperty widgetProperties) 
	{
		UIManager.put("ScrollBar.width", arg0.width);
		UIManager.put("ScrollBar.height", arg0.height);
	}

}
