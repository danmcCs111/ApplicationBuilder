package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetExtensionInterfaces.ShiftFramesExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedShiftFramesActionListener implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object m = widgetProperties.getInstance();
		
		if(m instanceof AbstractButton)
		{
			AbstractButton mi = (AbstractButton) m;
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof ShiftFramesExtension)
						{
							ShiftFramesExtension cae = ((ShiftFramesExtension) o);
							cae.openShiftFramesDialog();
						}
					}
				}
			});
		}
	}
}
