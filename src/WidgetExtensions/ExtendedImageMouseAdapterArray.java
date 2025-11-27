package WidgetExtensions;

import java.awt.event.MouseListener;
import java.util.HashMap;

import MouseListenersImpl.ImageMouseAdapter;
import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionDefs.ExtendedStringCollection;
import WidgetExtensionInterfaces.MouseAdapterArrayExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedImageMouseAdapterArray implements ExtendedAttributeParam 
{
	private static HashMap<String, MouseListener> pathAndMouseAdapter;
	
	public void applyMethod(NameId arg0, boolean isSingleClick, WidgetCreatorProperty widgetProperties) 
	{
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(arg0.getNameId());
		ExtendedStringCollection esc = (ExtendedStringCollection) wcp.getInstance();
		pathAndMouseAdapter = new HashMap<String, MouseListener>();
		MouseAdapterArrayExtension mae = (MouseAdapterArrayExtension)widgetProperties.getInstance();
		for(String key : esc.getPathAndFileList().keySet())
		{
			pathAndMouseAdapter.put(
					key, 
					new ImageMouseAdapter(WidgetBuildController.getInstance().getFrame(), key, isSingleClick)
			);
		}
		mae.setPathAndMouseListenerAdapter(pathAndMouseAdapter);
	}

}
