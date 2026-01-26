package WidgetExtensions;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetScrollBackgroundForegroundColor implements ExtendedAttributeParam 
{
	public void applyMethod(Color background, Color foreground, WidgetCreatorProperty widgetProperties) 
	{
		JScrollPane scrollPane = (JScrollPane) widgetProperties.getInstance();
		BasicScrollBarUI bsb = new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		    	this.trackColor = background;
		        this.thumbColor = foreground;
		    }
		};
		scrollPane.getVerticalScrollBar().setUI(bsb);

		BasicScrollBarUI bsb2 = new BasicScrollBarUI() 
		{
		    @Override
		    protected void configureScrollBarColors() {
		        this.trackColor = background;
		        this.thumbColor = foreground;
		    }
		};
		scrollPane.getHorizontalScrollBar().setUI(bsb2);
	}
}
