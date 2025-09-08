package WidgetComponents;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Graphics2D.GraphicsUtil;
import ShapeWidgetComponents.ShapeDrawingCollection;

public class JButtonArrayPicture 
{
	private static final String 
		IMAGES_RELATIVE_PATH = "/images/";

	private String 
		path,
		text;
	String fileLocation;
	private Image 
		img;
	private static Image
		defaultImg;
	private static ShapeDrawingCollection sdc = new ShapeDrawingCollection();

	public JButtonArrayPicture()
	{
		
	}
	
	public Image getImg()
	{
		if(img == null)
		{
			setupImage(new File(this.fileLocation), new File(JButtonArray.DEFAULT_IMG));
		}
		return img != null
			? img
			: defaultImg;
	}
	
	private Image getDefaultImage(File defaultImageLocation)
	{
		if(defaultImg == null)
		{
			defaultImg = GraphicsUtil.getImageFromXml(
					JButtonArray.DIM_DEFAULT_PIC.width, JButtonArray.DIM_DEFAULT_PIC.height, defaultImageLocation, Color.black);
		}
		return defaultImg;
	}
	
	private Image setupImage(File file, File fileDefault)
	{
		Image retImage = null;
		try {
			if(img == null)
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
	
}
