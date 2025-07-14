package WidgetExtensionsImpl;

import javax.swing.JButton;

import ActionListenersImpl.LoadVideosActionListener;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedLoadVideos implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		if(m instanceof JButton)
		{
			JButton b = ((JButton)m);
			b.addActionListener(new LoadVideosActionListener());
		}
	}

}
