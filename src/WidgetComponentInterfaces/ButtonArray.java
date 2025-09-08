package WidgetComponentInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import Graphics2D.GraphicsUtil;

public interface ButtonArray 
{
	public void addJButtons(String path, List<String> listOf, int index);
	
	public Dimension getScaledDefaultPic();
	public Dimension getDefaultPicSize();
	public Dimension getScaledWidthHeight();
	public String getDefaultImagePath();
	
	default Image getDefaultImage(File defaultImageLocation)
	{
		Image retImage = null;
		Image tmpImage = GraphicsUtil.getImageFromXml(
				getDefaultPicSize().width, getDefaultPicSize().height, defaultImageLocation, Color.black);
		retImage = tmpImage.getScaledInstance(
				getScaledDefaultPic().width, 
				getScaledDefaultPic().height, 0);
		return retImage;
	}
	
	default Image setupImage(File file, File fileDefault)
	{
		Image retImage = null;
		try {
			Image tmpImage = ImageIO.read(file);
			retImage = tmpImage.getScaledInstance(
					getScaledWidthHeight().width, 
					getScaledWidthHeight().height, 0);
		} catch (IOException e) {
			retImage = getDefaultImage(fileDefault);
		}
		return retImage;
	}
}
