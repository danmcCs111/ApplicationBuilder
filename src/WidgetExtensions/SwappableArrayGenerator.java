package WidgetExtensions;

import javax.swing.JButton;

public class SwappableArrayGenerator
{
	public void applyMethod(String path, String filter) 
	{
		FileListOptionGenerator.getComponents(path, filter, JButton.class);//TODO
	}
	
}
