package Params;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;
import Properties.PathUtility;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeStyling;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponents.JButtonArray;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class KeepSelection implements ShapeDrawingCollectionLoad, Comparator<KeepSelection>
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
	private static ShapeDrawingCollection sdc = new ShapeDrawingCollection();
	private ButtonArray ba;
	
	public KeepSelection()
	{
		
	}
	
	public KeepSelection(String path, String text, ButtonArray ba)
	{
		this.path = path;
		this.text = text;
		this.ba = ba;
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
			Image retImage;
			retImage = GraphicsUtil.getImageFromXml(
					ba.getDefaultPicSize().width, ba.getDefaultPicSize().height, defaultImageLocation, Color.black);
			defaultImg = retImage.getScaledInstance(
					ba.getScaledDefaultPic().width, 
					ba.getScaledDefaultPic().height, 0);
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
			setupImage(false, new File(this.fileLocation), new File(ba.getDefaultImagePath()));
		}
		return img != null
			? img
			: defaultImg;
	}
	
	public static boolean isDefaultImg(Image img)
	{
		return img.equals(defaultImg);
	}
	
	public Dimension getSize()
	{
		if(img == null)
		{
			setupImage(false, new File(this.fileLocation), new File(ba.getDefaultImagePath()));
		}
		return img != null
			? ba.getScaledWidthHeight()
			: ba.getScaledDefaultPic();
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
						ba.getScaledWidthHeight().width, 
						ba.getScaledWidthHeight().height, 0);
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
			LoggingMessages.printOut(k.getText()+"");
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

	@Override
	public int compare(KeepSelection o1, KeepSelection o2) 
	{
		Point p1 = o1.getFrame().getLocation();
		Point p2 = o2.getFrame().getLocation();
		int 
			tmpHeight = o1.getFrame().getSize().height,
			tmpHeight2 = o2.getFrame().getSize().height;
		double
			height = tmpHeight > tmpHeight2 ? tmpHeight : tmpHeight2,
			heightThresh = (int) (height * (1.0/3.0)),
			heightThreshNeg = (int) (height * (1.5/3.0));
			
		int 
			xDiff = p1.x - p2.x,
			yDiff = p1.y - p2.y;
		if(xDiff < 0)
		{
			if(yDiff <= 0)
			{
				return -1;
			}
			else if(yDiff >= 0)
			{
				if(yDiff >= heightThresh)
				{
					return 1;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}
		//xDiff is > 0
		else if(yDiff <= 0)
		{
			if(Math.abs(yDiff) >= heightThreshNeg)
			{
				return -1;
			}
			else
			{
				return 1;
			}
		}
		else
		{
			return 1;
		}
	}
}
