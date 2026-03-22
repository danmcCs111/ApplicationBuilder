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

public class ImageReader
{
	private DefaultAndScaledImage 
		imageScalingOptions;
	private Image 
		defaultImage;
	private ImageIcon
		defaultImageIcon;
	private boolean
		buttonArrayImage = false;
	
	public ImageReader(DefaultAndScaledImage imageScalingOptions)
	{
		this(imageScalingOptions, false);
	}
	
	public ImageReader(DefaultAndScaledImage imageScalingOptions, boolean buttonArrayImage)
	{
		this.imageScalingOptions = imageScalingOptions;
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
		if(defaultImage == null && imageScalingOptions != null && !buttonArrayImage)
		{
			Image tmpImage = GraphicsUtil.getImageFromXml(
					imageScalingOptions.getDefaultPicSize().width, 
					imageScalingOptions.getDefaultPicSize().height, 
					new File(imageScalingOptions.getDefaultImagePath()), Color.black);
			defaultImage = tmpImage.getScaledInstance(
					imageScalingOptions.getScaledDefaultPic().width, 
					imageScalingOptions.getScaledDefaultPic().height, Image.SCALE_SMOOTH);
		}
		else if(defaultImage == null && buttonArrayImage)
		{
			LoggingMessages.printOut("default image creation!");
			Image tmpImage = GraphicsUtil.getImageFromXml(
					imageScalingOptions.getDefaultPicSize().width, 
					imageScalingOptions.getDefaultPicSize().height, 
					new File(imageScalingOptions.getDefaultImagePath()), Color.black);
			Dimension d = getScaledDimensionFromHeight(tmpImage, JButtonArray.getButtonIconHeight());//TODO.
			defaultImage = tmpImage.getScaledInstance(
					d.width, 
					d.height, Image.SCALE_SMOOTH);
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
					scaled = getScaledDimension(tmpImage, imageScalingOptions.getScaledWidth());
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
