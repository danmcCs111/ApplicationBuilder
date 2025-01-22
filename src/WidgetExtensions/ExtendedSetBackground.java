package WidgetExtensions;

import java.awt.Color;

import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.ColorConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetBackground implements ExtendedAttributeStringParam
{
	private Color backgroundColor;
	
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		String [] args = arg0.split("@");
		backgroundColor = ColorConverter.getColor(args[0], args[1], args[2]);
		JButtonArray buttonArray = (JButtonArray) widgetProperties.getInstance();
		buttonArray.setBackgroundButtonArray(backgroundColor);
	}
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

}
