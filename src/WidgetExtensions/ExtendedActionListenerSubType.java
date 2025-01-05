package WidgetExtensions;

import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import ActionListeners.ActionListenerSubTypeExtension;
import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerSubType implements ExtendedAttributeStringParam
{
	private static final String ARG_DELIMITER = "@";
	
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		LoggingMessages.printOut(arg0);
		String [] paths = arg0.split(ARG_DELIMITER);
		
		if(Direction.class.toString().endsWith(paths[0]))
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
	
	private static ActionListenerSubTypeExtension getActionListener(WidgetCreatorProperty widgetProperties)
	{
		Object inst = widgetProperties.getInstance();
		//TODO supporting AbstractButton
		if(inst instanceof AbstractButton)
		{
			for(ActionListener al : ((AbstractButton) inst).getActionListeners())
			{
				if(al instanceof ActionListenerSubTypeExtension)
				{
					return (ActionListenerSubTypeExtension) al;
				}
			}
		}
		return null;
	}
}
