package WidgetComponentInterfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;

import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.FileSelection;

public interface ButtonArray 
{
	public void addJButtons(String path, List<String> listOf, int index);
	
	public Dimension getScaledDefaultPic();
	public Dimension getDefaultPicSize();
	public Dimension getScaledWidthHeight();
	public String getDefaultImagePath();
	
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension);
	public void setDefaultPicSize(Dimension defaultPicDimension);
	public void setScaledWidthHeight(Dimension scaledDimension);
	public void setDefaultImageXmlPath(FileSelection fs);
	
	public boolean isHighlightButton(AbstractButton ab);
	
	default Image getDefaultImage(File defaultImageLocation)
	{
		return getDefaultImage(defaultImageLocation, getScaledDefaultPic(), getDefaultPicSize());
	}
	public static Image getDefaultImage(File defaultImageLocation, Dimension scaledDefaultPicSize, Dimension defaultPicSize)
	{
		Image retImage = null;
		Image tmpImage = GraphicsUtil.getImageFromXml(
				defaultPicSize.width, defaultPicSize.height, defaultImageLocation, Color.black);
		retImage = tmpImage.getScaledInstance(
				scaledDefaultPicSize.width, 
				scaledDefaultPicSize.height, 0);
		return retImage;
	}
	
	default Image setupImage(File file, File fileDefault)
	{
		return setupImage(file, fileDefault, getScaledWidthHeight(), getScaledDefaultPic(), getDefaultPicSize());
	}
	
	public static Image setupImage(File file, File fileDefault, Dimension scaledWidthHeight, Dimension scaledDefaultPicSize, Dimension defaultPicSize)
	{
		Image retImage = null;
		try {
			Image tmpImage = ImageIO.read(file);
			retImage = tmpImage.getScaledInstance(
					scaledWidthHeight.width, 
					scaledWidthHeight.height, 0);
		} catch (IOException e) {
			retImage = getDefaultImage(fileDefault, scaledDefaultPicSize, defaultPicSize);
		}
		return retImage;
	}
}
