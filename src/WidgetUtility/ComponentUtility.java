package WidgetUtility;

import java.awt.Component;
import java.awt.Container;

public interface ComponentUtility 
{
	public static Component findComponentByName(Container parentComponent, String name)
	{
		for(Component comp : parentComponent.getComponents())
		{
			if(name.equals(comp.getName()))
			{
				return comp;
			}
		}
		return null;
	}
}
