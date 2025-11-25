package WidgetComponentInterfaces;

import java.awt.Dimension;
import java.util.List;

import javax.swing.AbstractButton;

import ObjectTypeConversion.FileSelection;

public interface ButtonArray extends OpenAndSaveKeepsSubscriber
{
	public void addJButtons(String path, List<String> listOf, int index);
	
	public Dimension getScaledDefaultPic();
	public Dimension getDefaultPicSize();
	public Dimension getScaledWidthHeight();
	public Dimension getScaledWidthHeightPreview();
	public String getDefaultImagePath();
	
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension);
	public void setDefaultPicSize(Dimension defaultPicDimension);
	public void setScaledWidthHeight(Dimension scaledDimension);
	public void setDefaultImageXmlPath(FileSelection fs);
	public void adjustVisibility(String searchPattern);
	public void buildLoadingFrame();
	public void setIsLoadingSpinGraphic(boolean loadGraphic);
	
	public boolean isHighlightButton(AbstractButton ab);
	
}
