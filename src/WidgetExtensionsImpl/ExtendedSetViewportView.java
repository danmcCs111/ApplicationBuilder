package WidgetExtensionsImpl;

import java.awt.Component;

import javax.swing.JScrollPane;

import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetViewportView implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty parentWidget = WidgetBuildController.getInstance().findRef(widgetProperties.getParentRefWithID());
		JScrollPane scrollPane = (JScrollPane) parentWidget.getInstance();
		scrollPane.setViewportView((Component) widgetProperties.getInstance());
	}

}
