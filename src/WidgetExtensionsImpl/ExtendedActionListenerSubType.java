package WidgetExtensionsImpl;

import java.awt.event.ActionListener;
import javax.swing.AbstractButton;

import ActionListeners.ActionListenerSubTypeExtension;
import Properties.LoggingMessages;
import WidgetComponents.Direction;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerSubType implements ExtendedAttributeParam
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String [] paths = arg0.split(ARG_DELIMITER);
		
		if(Direction.class.toString().endsWith(paths[0]))//TODO
		{
			try {
				Class<?> clazz = Class.forName(paths[0]);
				ActionListenerSubTypeExtension ale = getActionListener(widgetProperties);
				ale.setActionListenerSubTypeExtension(clazz, paths[1]);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ActionListenerSubTypeExtension getActionListener(WidgetCreatorProperty widgetProperties)
	{
		Object inst = widgetProperties.getInstance();
		//TODO supporting AbstractButton
		if(inst instanceof AbstractButton)
		{
			for(ActionListener al : ((AbstractButton) inst).getActionListeners())
			{
				LoggingMessages.printOut(al.toString());
				if(al instanceof ActionListenerSubTypeExtension)
				{
					return (ActionListenerSubTypeExtension) al;
				}
			}
		}
		return null;
	}
}
