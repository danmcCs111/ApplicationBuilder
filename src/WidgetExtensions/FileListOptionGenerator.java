package WidgetExtensions;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import Properties.PropertiesFileLoader;

public class FileListOptionGenerator 
{
	/**
	 * @param path (the directory to collect)
	 * @param filter (the filename extension filter)
	 * @param componentType (which widget type to load list of files into)
	 */
	public static List<JComponent> buildComponents(String path, String filter, Class<?> componentType)
	{
		List<JComponent> components = new ArrayList<JComponent>();
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
		return components;
	}
	public static List<JComponent> buildComponents(String path, List<String> fileNames, Class<?> componentType)
	{
		List<JComponent> components = new ArrayList<JComponent>();
		if(componentType.getName().equals(JButton.class.getName()))
		{
			for(String fileName: fileNames)
			{
				JButton button = new JButton();
				button.setText(fileName);
//				button.setName(UrlToValueReader.parse(fileName, path));
				components.add(button);
			}
		}
		return components;
	}
	
	public static void addActionListenerToAll(ActionListener actionListener, List<JComponent> components)
	{
		for(JComponent component : components)
		{
			if(component instanceof JButton)
			{
				((JButton)component).addActionListener(actionListener);
			}
		}
	}
}
