package WidgetComponentInterfaces;

import java.awt.Dimension;

import ObjectTypeConversion.FileSelection;

public interface DefaultAndScaledImage 
{
	public String getDefaultImagePath();
	public Dimension getScaledDefaultPic();
	public Dimension getDefaultPicSize();
	public int getScaledWidth();
	
	public void setDefaultImageXmlPath(FileSelection fs);
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension);
	public void setDefaultPicSize(Dimension defaultPicDimension);
	public void setScaledWidth(int scaledWidth);
}
