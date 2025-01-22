package WidgetExtensions;

import java.awt.Color;

import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.ColorConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetForeground implements ExtendedAttributeStringParam
{
	private Color foregroundColor;
	
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		String [] args = arg0.split("@");
		foregroundColor = ColorConverter.getColor(args[0], args[1], args[2]);
		JButtonArray buttonArray = (JButtonArray) widgetProperties.getInstance();
		buttonArray.setForegroundButtonArray(foregroundColor);
	}
	
	public Color getForegroundColor()
	{
		return foregroundColor;
	}

}
