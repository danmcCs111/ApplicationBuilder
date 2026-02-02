package WidgetComponentInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;

public class ImageReader
{
	private ButtonArray 
		ba;
	private JButtonLengthLimited
		jbll;
	private Image 
		defaultImage;
	private ImageIcon
		defaultImageIcon;
	private boolean
		buttonArrayImage = false;
	
	public ImageReader(ButtonArray ba)
	{
		this(ba, false);
	}
	
	public ImageReader(ButtonArray ba, boolean buttonArrayImage)
	{
		this.ba = ba;
		this.buttonArrayImage = buttonArrayImage;
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
		if(defaultImage == null && ba != null && !buttonArrayImage)
		{
			Image tmpImage = GraphicsUtil.getImageFromXml(
					ba.getDefaultPicSize().width, ba.getDefaultPicSize().height, new File(ba.getDefaultImagePath()), Color.black);
			defaultImage = tmpImage.getScaledInstance(
					ba.getScaledDefaultPic().width, 
					ba.getScaledDefaultPic().height, 0);
		}
		else if(defaultImage == null && buttonArrayImage)
		{
			LoggingMessages.printOut("default image creation!");
			Image tmpImage = GraphicsUtil.getImageFromXml(
					ba.getDefaultPicSize().width, ba.getDefaultPicSize().height, new File(ba.getDefaultImagePath()), Color.black);
			Dimension d = getScaledDimensionFromHeight(tmpImage, JButtonArray.getButtonIconHeight());//TODO.
			defaultImage = tmpImage.getScaledInstance(
					d.width, 
					d.height, 0);
		}
		return defaultImage;
	}
	
	public ImageIcon getImageIcon(File file)
	{
		Image tmpImage = getImage(file);
		if(tmpImage.equals(defaultImage))
		{
			return defaultImageIcon;
		}
		return new ImageIcon(tmpImage);
	}
	
	public Image getImage(File file)
	{
		Image retImage = null;
		try {
			Image tmpImage = ImageIO.read(file);
			if(tmpImage == null)
			{
				retImage = getDefaultImage();
			}
			else
			{
				Dimension scaled;
				if(buttonArrayImage)
				{
					scaled = getScaledDimensionFromHeight(tmpImage, JButtonArray.getButtonIconHeight());//TODO.
				}
				else
				{
					scaled = getScaledDimension(tmpImage, ba.getScaledWidth());
				}
				
				retImage = tmpImage.getScaledInstance(
						scaled.width, 
						scaled.height, 0);
			}
		} catch (IOException e) {
			retImage = getDefaultImage();
		}
		return retImage;
	}
	
	public static Dimension getScaledDimension(Image tmpImage, int scaledWidth)
	{
		ImageReaderDimensionObserver imgO = new ImageReaderDimensionObserver();
		double 
			width = (double)tmpImage.getWidth(imgO),
			height = (double)tmpImage.getHeight(imgO);
		
		if(width == -1 || height == -1)
		{
			return imgO.getScaledFromWidth((double)scaledWidth);
		}
		else
		{
			double factor = scaledWidth / width;
			
			return new Dimension((int)(factor * width), (int)(factor * height));
		}
	}
	
	public static Dimension getScaledDimensionFromHeight(Image tmpImage, int scaledHeight)
	{
		ImageReaderDimensionObserver imgO = new ImageReaderDimensionObserver();
		double 
			width = (double)tmpImage.getWidth(imgO),
			height = (double)tmpImage.getHeight(imgO);
		
		if(width == -1 || height == -1)
		{
			return imgO.getScaledFromHeight((double)scaledHeight);
		}
		else
		{
			double factor = scaledHeight / height;
			
			return new Dimension((int)(factor * width), (int)(factor * height));
		}
	}
}
