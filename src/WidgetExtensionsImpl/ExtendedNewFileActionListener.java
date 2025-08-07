package WidgetExtensionsImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.FileNewActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedNewFileActionListener implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		
		Object m = widgetProperties.getInstance();
		if(m instanceof AbstractButton)
		{
			AbstractButton ab = (AbstractButton) m;
			ab.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof FileNewActionExtension)
						{
							((FileNewActionExtension) o).performNewFile();
						}
					}
				}
			});
		}
	}
}
