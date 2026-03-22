package WidgetComponentInterfaces;

import java.util.List;

import javax.swing.AbstractButton;

public interface ButtonArray extends OpenAndSaveKeepsSubscriber, DefaultAndScaledImage
{
	public void addJButtons(String path, List<String> listOf, int index);
	public void refreshJButtons(String path, List<String> listOf, int index, int indexPl);
	
	public int getScaledWidthPreview();
	
	public void adjustVisibility(String searchPattern);
	public void buildLoadingFrame();
	public void setIsLoadingSpinGraphic(boolean loadGraphic);
	
	public boolean isHighlightButton(AbstractButton ab);
	
}
