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
import javax.swing.JPanel;

import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;
import Properties.PathUtility;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeStyling;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetExtensionInterfaces.ShapeDrawingCollectionLoad;

public class KeepSelection implements ShapeDrawingCollectionLoad, Comparator<KeepSelection>
{
	private static final String 
		IMAGES_RELATIVE_PATH = "/images/";
	
	private String 
		path,
		text,
		fileLocation;
	private JFrame frame;
	private Image 
		img,
		previewImage;
	private static Image
		defaultImg;
	public static boolean 
		skip = true;
	private static ShapeDrawingCollection sdc = new ShapeDrawingCollection();
	private ButtonArray ba;
	private JButtonLengthLimited ab;
	
	private JPanel connectedPanel;
	
	public KeepSelection()
	{
		
	}
	
	public KeepSelection(String path, String text, ButtonArray ba, JButtonLengthLimited ab)
	{
		this(path, text, ba);
		this.ab = ab;
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
	
	public void destroyImages()
	{
		//TODO.
	}
	
	public JButtonLengthLimited getJButtonLengthLimited()
	{
		return ab;
	}
	
	public JPanel getConnectedPanel()
	{
		return connectedPanel;
	}
	
	public void setConnectedPanel(JPanel panel)
	{
		connectedPanel = panel;
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
	
	public Image getPreviewImage()
	{
		if(previewImage == null)
		{
			setupImage(false, new File(this.fileLocation), new File(ba.getDefaultImagePath()));
		}
		return previewImage != null
			? previewImage
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
			? ImageReader.getScaledDimension(img, ba.getScaledWidth())
			: ba.getScaledDefaultPic();
	}
	
	public Dimension getSizePreview()
	{
		if(img == null)
		{
			getSize();
		}
		return img != null
			? ImageReader.getScaledDimension(img, ba.getScaledWidthPreview())
			: ba.getScaledDefaultPic();
	}
	
	public String toPngFilename()
	{
		return this.text + ".png";
	}
	
	private void setupImage(boolean skip, File file, File fileDefault)
	{
		try {
			if(!skip && img == null)
			{
				Image retImage = null;
				retImage = ImageIO.read(file);
				LoggingMessages.printOut(file.getAbsolutePath());
				Dimension scaled = ImageReader.getScaledDimension(retImage, ba.getScaledWidth());
				img = retImage.getScaledInstance(
						scaled.width, 
						scaled.height, 0);
				if(ba.getScaledWidthPreview() != ba.getScaledWidth())
				{
					Dimension scaledPreview = ImageReader.getScaledDimension(retImage, ba.getScaledWidthPreview());
					previewImage = retImage.getScaledInstance(
							scaledPreview.width,
							scaledPreview.height, 0);
				}
				else
				{
					previewImage = img;
				}
			}
		} catch (IOException e) {
			getDefaultImage(fileDefault);
		}
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
			heightThresh = (int) (height * (1.0/3.0));
			
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
			if(Math.abs(yDiff) >= heightThresh)
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
