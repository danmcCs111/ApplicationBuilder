package WidgetExtensionsImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetExtensions.ClearActionExtension;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedClearActionListener implements ExtendedAttributeStringParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object m = widgetProperties.getInstance();
		if(m instanceof AbstractButton)
		{
			AbstractButton ab = (AbstractButton) m;
			ab.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof ClearActionExtension)
						{
							((ClearActionExtension) o).performClear();
						}
					}
				}
			});
		}
	}
}
