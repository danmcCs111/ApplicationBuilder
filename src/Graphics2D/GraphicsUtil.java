package Graphics2D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;

public interface GraphicsUtil 
{
	public static void centerWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			w = comp.getWidth(),
			h = comp.getHeight();
		int 
			x = loc.x + (int)((rw/2.0) - (w / 2.0)),
			y = loc.y + (int)((rh/2.0) - (h/2.0));
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
	}
	public static void centerHeightOnlyWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			h = comp.getHeight();
		int 
			x = (int)(loc.x + (rw / 2.0)),
			y = (int)(loc.y + (rh/2.0 - h/2.0));
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
	}
	public static void centerReferenceOnlyWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight();
		int
			x = (int)(loc.x + (rw / 2.0)),
			y = (int)(loc.y + (rh/2.0));
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
	}
	public static void centerOnScreen(Component comp)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point loc = new Point(0, 0);
		
		double 
			rw = screenSize.getWidth(),
			rh = screenSize.getHeight(),
			w = comp.getWidth(),
			h = comp.getHeight();
		int
			x = loc.x + (int)((rw/2.0) - (w / 2.0)),
			y = loc.y + (int)((rh/2.0) - (h/2.0));
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp)
	{
		rightEdgeTopWindow(referenceComponent, comp, 0);
	}
	
	public static void rightEdgeTopWindow(Container referenceComponent, Container comp, int numberOfMatchedVisible)
	{
		Point loc = referenceComponent.getLocation();
		int 
			rw = referenceComponent.getWidth(),
			h = comp.getHeight(),
			x = loc.x + (rw), 
			y = loc.y + (h * (numberOfMatchedVisible));
		
		Point locPoint = new Point( 
				x,(y<0)?0:y
		);
		
		comp.setLocation(locPoint);
	}
	
	public static void rightEdgeCenterWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocation();
		
		int 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			h = comp.getHeight(),
			x = loc.x + rw,
			y = loc.y + (rh/2 - h/2);
		y=(y<0)?0:y;
		
		comp.setLocation(x, y);
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
	
	public static Image getImageFromFile(File defaultImageLocation)
	{
        BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(defaultImageLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
	
}
