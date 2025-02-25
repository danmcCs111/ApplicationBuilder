package WidgetExtensionsImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.CloseAllActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCloseAllActionListener implements ExtendedAttributeParam
{

	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object m = widgetProperties.getInstance();
		
		if(m instanceof JMenuItem)
		{
			JMenuItem mi = (JMenuItem) m;
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof CloseActionExtension)
						{
							CloseAllActionExtension cae = ((CloseAllActionExtension) o);
							cae.closeAll();
						}
					}
				}
			});
		}
	}

}
