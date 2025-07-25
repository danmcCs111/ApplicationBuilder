package WidgetExtensionsImpl;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.CommandBuildListener;
import ObjectTypeConversion.CommandBuild;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCommandExecution implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		if(m instanceof AbstractButton)
		{
			AbstractButton mi = ((AbstractButton)m);
			if(arg0 != null && !arg0.strip().isEmpty())
			{
				for(ActionListener al : mi.getActionListeners())
				{
					if(al instanceof CommandBuildListener)
					{
						((CommandBuildListener) al).setCommandBuild(new CommandBuild(arg0));
					}
				}
			}
		}
	}

}
