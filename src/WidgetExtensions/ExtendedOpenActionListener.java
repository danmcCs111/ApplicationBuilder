package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedOpenActionListener implements ExtendedAttributeParam
{
	private OpenActionExtension oae = null;
	
	protected OpenActionExtension getOpenActionExtension()
	{
		return this.oae;
	}
	
	public void performOpen()
	{
		getOpenActionExtension().performOpen();
	}
	
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		NameId name = arg0;
		Object m = widgetProperties.getInstance();
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name.getNameId());
		if(wcp != null)
		{
			Object o = wcp.getInstance();
			if(o instanceof OpenActionExtension)
			{
				oae = (OpenActionExtension) o;
			}
		}
		if(m instanceof AbstractButton)
		{
			AbstractButton ab = (AbstractButton) m;
			ab.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					performOpen();
				}
			});
		}
	}
}
