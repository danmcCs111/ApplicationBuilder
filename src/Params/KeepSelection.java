package Params;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Properties.LoggingMessages;
import Properties.PathUtility;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeDrawingCollectionGraphics;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;
import ShapeWidgetComponents.ShapeStyling;
import WidgetComponents.JButtonArray;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class KeepSelection implements ShapeDrawingCollectionLoad
{
	private static final String 
		IMAGES_RELATIVE_PATH = "/images/";
	
	private String 
		path,
		text;
	String fileLocation;
	private JFrame frame;
	private Image 
		img;
	private static Image
		defaultImg;
	public static boolean 
		skip = true;
	private static ShapeDrawingCollection sdc = new ShapeDrawingCollection();;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
		this.fileLocation = PathUtility.getCurrentDirectory() + 
				PathUtility.removeCurrentWorkingDirectoryFromPath(path)  + 
				IMAGES_RELATIVE_PATH + toPngFilename();
		File file = new File(this.fileLocation);
		File fileDefault = new File(JButtonArray.DEFAULT_IMG);
		setupImage(skip, file, fileDefault);
	}
	
	private Image getDefaultImage(File defaultImageLocation)
	{
		if(defaultImg == null)
		{
			LoggingMessages.printOut(defaultImageLocation.getAbsolutePath());
			ShapeImportExport sie = new ShapeImportExport();
			@SuppressWarnings("unchecked")
			ArrayList<ShapeElement> shapeElements = (ArrayList<ShapeElement>) sie.openXml(defaultImageLocation);
			sdc.addShapeImports(shapeElements, this);
			sdc.getShapes();
	        BufferedImage bufferedImage = new BufferedImage(JButtonArray.DIM_DEFAULT_PIC.width, JButtonArray.DIM_DEFAULT_PIC.height, BufferedImage.TYPE_INT_RGB);
	        Graphics2D g2d = bufferedImage.createGraphics();
        	ShapeDrawingCollectionGraphics.drawShapes(g2d, sdc);
	        defaultImg = bufferedImage;
		}
		return defaultImg;
	}
	
	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	public JFrame getFrame()
	{
		return this.frame;
	}
	
	public Point getLocationPoint()
	{
		return this.frame.getLocation();
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public Image getImg()
	{
		if(img == null)
		{
			setupImage(false, new File(this.fileLocation), new File(JButtonArray.DEFAULT_IMG));
		}
		return img != null
			? img
			: defaultImg;
	}
	
	public Dimension getSize()
	{
		if(img == null)
		{
			setupImage(false, new File(this.fileLocation), new File(JButtonArray.DEFAULT_IMG));
		}
		return img != null
			? JButtonArray.DIM_PIC
			: JButtonArray.DIM_DEFAULT_PIC;
	}
	
	public String toPngFilename()
	{
		return this.text + ".png";
	}
	
	private Image setupImage(boolean skip, File file, File fileDefault)
	{
		Image retImage = null;
		try {
			if(!skip && img == null)
			{
				retImage = ImageIO.read(file);
				img = retImage.getScaledInstance(
						JButtonArray.SCALED_WIDTH_HEIGHT.width, 
						JButtonArray.SCALED_WIDTH_HEIGHT.height, 0);
				retImage = null;
				retImage = img;
			}
		} catch (IOException e) {
			getDefaultImage(fileDefault);
			retImage = defaultImg;
		}
		return retImage;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof KeepSelection)
		{
			if(((KeepSelection) obj).getPath().equals(this.getPath()) && 
					((KeepSelection) obj).getText().equals(this.getText()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return this.path + " " + this.text;
	}
	
	public static ArrayList<String> getTextOnlyConversion (List<KeepSelection> ks)
	{
		ArrayList<String> convList = new ArrayList<String>();
		for(KeepSelection k : ks)
		{
			convList.add(k.getText());
		}
		return convList;
	}

	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) 
	{
		if(sdc.getShapeStylings().size() <= shapeStyleIndex)
		{
			sdc.addShapeStyling(shapeStyling);
		}
	}

	@Override
	public void addShapeDrawingCollection(ShapeDrawingCollection sdc) 
	{
		//
	}
}
