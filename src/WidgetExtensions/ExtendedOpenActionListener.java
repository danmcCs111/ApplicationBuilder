package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedOpenActionListener implements ExtendedAttributeStringParam
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
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object m = widgetProperties.getInstance();
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
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
