package WidgetComponentInterfaces;

import javax.swing.AbstractButton;

public interface HighlightListener 
{
	public void highlight();
	public AbstractButton getMatchingButton(String name);
}
