package WidgetExtensions;

import java.awt.Component;

import javax.swing.JScrollPane;

import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetViewportView implements ExtendedAttributeStringParam 
{

	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty parentWidget = WidgetBuildController.findRef(widgetProperties.getParentRefWithID());
		JScrollPane scrollPane = (JScrollPane) parentWidget.getInstance();
		scrollPane.setViewportView((Component) widgetProperties.getInstance());
	}

}
