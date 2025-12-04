package WidgetComponentInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Graphics2D.GraphicsUtil;

public class ImageReader
{
	private ButtonArray ba;
	private Image 
		defaultImage;
	private ImageIcon
		defaultImageIcon;
	
	public ImageReader(ButtonArray ba)
	{
		this.ba = ba;
		getDefaultImageIcon();
	}
	
	public ImageIcon getDefaultImageIcon()
	{
		if(defaultImageIcon == null)
		{
			defaultImageIcon = new ImageIcon(getDefaultImage());
		}
		return defaultImageIcon;
	}
	
	public Image getDefaultImage()
	{
		if(defaultImage == null)
		{
			Image tmpImage = GraphicsUtil.getImageFromXml(
					ba.getDefaultPicSize().width, ba.getDefaultPicSize().height, new File(ba.getDefaultImagePath()), Color.black);
			defaultImage = tmpImage.getScaledInstance(
					ba.getScaledDefaultPic().width, 
					ba.getScaledDefaultPic().height, 0);
		}
		return defaultImage;
	}
	
	public ImageIcon setupImageIcon(File file)
	{
		Image tmpImage = setupImage(file);
		if(tmpImage.equals(defaultImage))
		{
			return defaultImageIcon;
		}
		return new ImageIcon(tmpImage);
	}
	
	public Image setupImage(File file)
	{
		Image retImage = null;
		try {
			Image tmpImage = ImageIO.read(file);
			Dimension scaled = getScaledDimension(tmpImage, ba.getScaledWidth());
			retImage = tmpImage.getScaledInstance(
					scaled.width, 
					scaled.height, 0);
		} catch (IOException e) {
			retImage = getDefaultImage();
		}
		return retImage;
	}
	
	public static Dimension getScaledDimension(Image tmpImage, int scaledWidth)
	{
		ImageReaderDimensionObserver imgO = new ImageReaderDimensionObserver();
		double width = (double)tmpImage.getWidth(imgO);
		double height = (double)tmpImage.getHeight(imgO);
		
		if(width == -1 || height == -1)
		{
			return imgO.getWidthHeightScaled((double)scaledWidth);
		}
		else
		{
			double factor = scaledWidth / width;
			
			return new Dimension((int)(factor * width), (int)(factor * height));
		}
		
	}
}
