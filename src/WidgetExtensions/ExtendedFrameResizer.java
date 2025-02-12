package WidgetExtensions;

import java.awt.Component;

import javax.swing.JFrame;

import ActionListeners.FrameResizeListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

/**
 * WARN consumes a lot of memory adding component listener
 */
public class ExtendedFrameResizer implements ExtendedAttributeStringParam
{
	public static final int INTERNAL_RESIZE_EVENT = 999;
	private String compName;
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		this.compName = arg0;
		JFrame frame = (JFrame) widgetProperties.getInstance();
		FrameResizeListener frListener = new FrameResizeListener(frame, this);
		frame.addComponentListener(frListener);
	}
	
	public ResizerListener getResizeListener()
	{
		for(WidgetCreatorProperty wcp : WidgetBuildController.getWidgetCreatorProperties())
		{
			Object o = wcp.getInstance();
			if(o instanceof Component)
			{
				if(o instanceof ResizerListener && compName.equals(((Component) o).getName()))
				{
					return (ResizerListener) o;
				}
			}
		}
		return null;
	}

}
