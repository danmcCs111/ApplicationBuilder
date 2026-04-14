package WidgetExtensions;

import javax.swing.AbstractButton;

import ActionListenersImpl.OpenAllVideoChannelsActionListener;
import ObjectTypeConversion.NameId;
import WidgetComponents.JButtonArray;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedAllVideoChannelsOpenedList implements ExtendedAttributeParam
{
	public void applyMethod(NameId nameId, WidgetCreatorProperty widgetProperties)
	{
		JButtonArray ba = (JButtonArray) WidgetBuildController.getInstance().findRefByName(nameId.getNameId()).getInstance();
		
		AbstractButton ab = (AbstractButton) widgetProperties.getInstance();
		OpenAllVideoChannelsActionListener oval = new OpenAllVideoChannelsActionListener(ba);
		ab.addActionListener(oval);
	}
}
