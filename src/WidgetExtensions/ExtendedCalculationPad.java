package WidgetExtensions;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

import ApplicationBuilder.LoggingMessages;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCalculationPad implements ExtendedAttributeStringParam
{
	private Component 
		thisComp,
		targetComp;
	private Container
		frame,
		panel;
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		LoggingMessages.printOut(this.getClass().toString());
		this.thisComp = (JComponent) widgetProperties.getInstance();
		this.frame = (Container) WidgetBuildController.getInstance().getWidgetCreatorProperties().get(0).getInstance();
		this.panel = this.thisComp.getParent();
		this.targetComp = getTargetComponent(panel, arg0);
		
		addActionListener();
		resize();
	}
	
	private Component getTargetComponent(Container panel, String clazzName)
	{
		for(Component comp : panel.getComponents())
		{
			Class<?> clazz = comp.getClass();
			if(clazz.toString().contains(clazzName))
			{
				return comp;
			}
		}
		return null;
	}
	
	private void addActionListener()
	{
		this.frame.addComponentListener(new ComponentAdapter() 
		{
			@Override
			public void componentResized(ComponentEvent e) 
			{
				resize();
			}
		});
	}
	
	public void resize()
	{
		int 
			width = frame.getWidth(),
			calcWidth = (width / 2) - targetComp.getPreferredSize().width,
			calcHeight = targetComp.getPreferredSize().height;
		
		this.thisComp.setPreferredSize(new Dimension(calcWidth, calcHeight));
	}

}
