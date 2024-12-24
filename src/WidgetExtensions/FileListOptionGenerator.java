package WidgetExtensions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import Properties.PropertiesFileLoader;

public class FileListOptionGenerator 
{
	List<JComponent> components = new ArrayList<JComponent>();
	
	public FileListOptionGenerator(String path, String filter, Class<?> componentType)
	{
		if(componentType.getName().equals(JButton.class.getName()))
		{
			ArrayList<String> fileNames = PropertiesFileLoader.getOSFileList(path, filter);
			for(String fileName: fileNames)
			{
				JButton button = new JButton();
				button.setText(fileName);
				button.setName(UrlToValueReader.parse(fileName, path));
				components.add(button);
			}
		}
	}
}
