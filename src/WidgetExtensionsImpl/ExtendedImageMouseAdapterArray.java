package WidgetExtensionsImpl;

import java.awt.event.MouseListener;
import java.util.HashMap;

import MouseListenersImpl.ImageMouseAdapter;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.MouseAdapterArrayExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedImageMouseAdapterArray implements ExtendedAttributeParam 
{
	private static HashMap<String, MouseListener> pathAndMouseAdapter;
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(arg0);
		ExtendedStringCollection esc = (ExtendedStringCollection) wcp.getInstance();
		pathAndMouseAdapter = new HashMap<String, MouseListener>();
		MouseAdapterArrayExtension mae = (MouseAdapterArrayExtension)widgetProperties.getInstance();
		for(String key : esc.getPathAndFileList().keySet())
		{
			pathAndMouseAdapter.put(
					key, 
					new ImageMouseAdapter(WidgetBuildController.getInstance().getFrame(), key)
			);
		}
		mae.setPathAndMouseListenerAdapter(pathAndMouseAdapter);
	}

}
