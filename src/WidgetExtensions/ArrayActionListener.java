package WidgetExtensions;

import java.awt.event.ActionListener;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ArrayActionListener 
{
	public abstract void addActionListener(ActionListener actionListener);
	
	public static ArrayActionListener findInstanceOfArrayActionListener(WidgetBuildController wbc)
	{
		for(WidgetCreatorProperty wcp : wbc.getWidgetCreationProperties())
		{
			if(wcp.getInstance() instanceof ArrayActionListener)
			{
				return (ArrayActionListener) wcp.getInstance();
			}
		}
		return null;
	}
}
