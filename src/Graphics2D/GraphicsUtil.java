package Graphics2D;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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
		Point loc = referenceComponent.getLocationOnScreen();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			w = comp.getWidth(),
			h = comp.getHeight();
		
		comp.setLocation(loc.x + (int)((rw/2.0) - (w / 2.0)), loc.y + (int)((rh/2.0) - (h/2.0)) );
	}
	public static void centerHeightOnlyWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocationOnScreen();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			h = comp.getHeight();
		
		comp.setLocation((int)(loc.x + (rw / 2.0)), (int)(loc.y + (rh/2.0 - h/2.0)));
	}
	public static void centerReferenceOnlyWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocationOnScreen();
		
		double 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight();
		
		comp.setLocation((int)(loc.x + (rw / 2.0)), (int)(loc.y + (rh/2.0)));
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
	
	public static void rightEdgeCenterWindow(Container referenceComponent, Container comp)
	{
		Point loc = referenceComponent.getLocationOnScreen();
		
		int 
			rw = referenceComponent.getWidth(),
			rh = referenceComponent.getHeight(),
			h = comp.getHeight();
		
		comp.setLocation(loc.x + rw, loc.y + (rh/2 - h/2));
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
