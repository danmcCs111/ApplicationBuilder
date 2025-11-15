package Graphics2D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeDrawingCollectionGraphics;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;

public interface GraphicsUtil 
{
	public static void centerWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		int rw = referenceComponent.getWidth();
		int rh = referenceComponent.getHeight();
		int w = comp.getWidth();
		int h = comp.getHeight();
		
		comp.setLocation(new Point(loc.x + (rw/2) - (w/2), loc.y + (rh/2) - (h/2)));
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp)
	{
		rightEdgeTopWindow(referenceComponent, comp, 0);
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp, int numberOfMatchedVisible)
	{
		Point loc = referenceComponent.getLocationOnScreen();
		int rw = referenceComponent.getWidth();
		int h = comp.getHeight();
		
		comp.setLocation(new Point(loc.x + (rw), loc.y + (h * (numberOfMatchedVisible) )));
	}
	
	public static Image getImageFromXml(int width, int height, File defaultImageLocation, Color backgroundColor)
	{
		Image defaultImg;
		LoggingMessages.printOut(defaultImageLocation.getAbsolutePath());
		ShapeImportExport sie = new ShapeImportExport();
		ShapeDrawingCollection sdc = new ShapeDrawingCollection();
		@SuppressWarnings("unchecked")
		ArrayList<ShapeElement> shapeElements = (ArrayList<ShapeElement>) sie.openXml(defaultImageLocation);
		sdc.addShapeImports(shapeElements, null);
		sdc.getShapes();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setBackground(backgroundColor);
    	ShapeDrawingCollectionGraphics.drawShapes(g2d, sdc);
        defaultImg = bufferedImage;
		return defaultImg;
	}
	
	public static void setBackgroundColorPanel(Container container, Color c) 
	{
		if (container instanceof JPanel) 
        {
        	JPanel pan = (JPanel) container;
        	pan.setBackground(c);
        } 
		else if (container instanceof JTextField) 
        {
			JTextField tf = (JTextField) container;
         	tf.setBackground(c);
        } 
		else if (container instanceof JCheckBox) 
        {
        	JCheckBox cb = (JCheckBox) container;
        	cb.setBackground(c);
        } 
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JPanel) 
            {
            	JPanel pan = (JPanel) component;
            	pan.setBackground(c);
            } 
            else if (container instanceof JTextField) 
            {
    			JTextField tf = (JTextField) container;
             	tf.setBackground(c);
            } 
    		else if (container instanceof JCheckBox) 
            {
            	JCheckBox cb = (JCheckBox) container;
            	cb.setBackground(c);
            } 
            if (component instanceof Container) 
            {
            	setBackgroundColorPanel((Container) component, c);
            }
        }
    }
	public static void setBackgroundColorButtons(Container container, Color c) 
	{
		if (container instanceof JButton ) 
        {
			JButton ab = (JButton) container;
        	ab.setBackground(c);
        } 
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JButton) 
            {
            	AbstractButton ab = (JButton) component;
            	ab.setBackground(c);
            } 
            else if (component instanceof Container) 
            {
            	setBackgroundColorButtons((Container) component, c);
            }
        }
    }
	public static void setForegroundColorButtons(Container container, Color c) 
	{
		if (container instanceof JButton) 
        {
			JButton ab = (JButton) container;
        	ab.setForeground(c);
        } 
		else if (container instanceof JTextField) 
        {
			JTextField tf = (JTextField) container;
			tf.setCaretColor(c);
         	tf.setForeground(c);
        } 
		else if (container instanceof JCheckBox) 
        {
        	JCheckBox cb = (JCheckBox) container;
        	cb.setBackground(c);
        } 
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JButton) 
            {
            	JButton ab = (JButton) component;
            	ab.setForeground(c);
            } 
            else if (component instanceof JTextField) 
            {
            	JTextField tf = (JTextField) component;
            	tf.setCaretColor(c);
            	tf.setForeground(c);
            } 
            else if (component instanceof JCheckBox) 
            {
            	JCheckBox cb = (JCheckBox) component;
            	cb.setBackground(c);
            } 
            else if (component instanceof Container) 
            {
            	setForegroundColorButtons((Container) component, c); 
            }
        }
    }
	
}
