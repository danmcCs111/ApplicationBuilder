package WidgetExtensions;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.TouchFrameDragActionListener;
import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedTouchOptionLookup implements ExtendedAttributeParam 
{
	private TouchFrameDragActionListener 
		tfdal;
	private NameId
		nameId;
	
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		nameId = arg0;
	}
	
	public boolean isTouch()
	{
		Object o = WidgetBuildController.getInstance().findRefByName(nameId.getNameId()).getInstance();
		if(o instanceof AbstractButton)
		{
			AbstractButton ab = (AbstractButton) o;
			for(ActionListener al : ab.getActionListeners())
			{
				if(al instanceof TouchFrameDragActionListener)
				{
					tfdal = (TouchFrameDragActionListener) al;
					return tfdal.isTouch();
				}
			}
		}
		return false;
	}
}
