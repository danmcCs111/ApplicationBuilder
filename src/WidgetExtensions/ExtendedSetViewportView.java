package WidgetExtensions;

import java.awt.Component;

import javax.swing.JScrollPane;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetViewportView implements ExtendedAttributeStringParam 
{

	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty parentWidget = wbc.findRef(widgetProperties.getParentRef());
		JScrollPane scrollPane = (JScrollPane) parentWidget.getInstance();
		scrollPane.setViewportView((Component) widgetProperties.getInstance());
	}

}
