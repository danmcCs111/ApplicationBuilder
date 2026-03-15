package WidgetExtensions;

import javax.swing.AbstractButton;

import ObjectTypeConversion.NameId;
import WidgetComponents.JButtonArray;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.OpenVideoChannelsUpdaterActionListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedOpenVideoChannelsUpdater implements ExtendedAttributeParam
{
	public void applyMethod(NameId nameId, WidgetCreatorProperty widgetProperties)
	{
		JButtonArray ba = (JButtonArray) WidgetBuildController.getInstance().findRefByName(nameId.getNameId()).getInstance();
		
		AbstractButton ab = (AbstractButton) widgetProperties.getInstance();
		OpenVideoChannelsUpdaterActionListener oval = new OpenVideoChannelsUpdaterActionListener(ba);
		ab.addActionListener(oval);
	}
}
