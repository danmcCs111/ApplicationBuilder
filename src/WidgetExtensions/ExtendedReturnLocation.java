package WidgetExtensions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JFrame;

import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedReturnLocation implements ExtendedAttributeParam
{
	private Point 
		location;
	private JFrame
		frame;
	
	public void applyMethod(Point arg0, WidgetCreatorProperty widgetProperties) 
	{
		location = arg0;
		frame = WidgetBuildController.getInstance().getFrame();
		
		AbstractButton ab = (AbstractButton) widgetProperties.getInstance();
		ab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setLocation(location);
				frame.setSize(frame.getMinimumSize());
			}
		});
	}
	
}
