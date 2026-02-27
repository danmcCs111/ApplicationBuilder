package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetExtensionInterfaces.RefreshActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedRefreshAction implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		RefreshActionExtension rae = (RefreshActionExtension) WidgetBuildController.getInstance().findRefByName(arg0).getInstance();
		AbstractButton ab = (AbstractButton) widgetProperties.getInstance();
		ab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rae.refresh();
			}
		});
	}
}
