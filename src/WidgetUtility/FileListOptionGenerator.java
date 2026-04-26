package WidgetUtility;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import Properties.PathUtility;
import Properties.UrlToValueReader;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.JToggleButtonLengthLimited;

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
		if(componentType.equals(JButton.class))
		{
			ArrayList<String> fileNames = PathUtility.getOSFileList(path, filter);
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
		if(componentType.equals(JButtonLengthLimited.class))//TODO impl
		{
			for(String fileName: fileNames)
			{
				components.add(buildComponent(path, fileName, componentType));
			}
		}
		return components;
	}
	public static JComponent buildComponent(String path, String fileName, Class<?> componentType)
	{
		return buildComponent(path, fileName, null, componentType);
	}
	public static JComponent buildComponent(String path, String fileName, String url, Class<?> componentType)
	{
		JComponent comp = null;
		if(componentType.equals(JButtonLengthLimited.class))
		{
			JButtonLengthLimited button = new JButtonLengthLimited();
			button.setPath(path);
			button.setText(fileName);
			button.setFullText(PathUtility.getFilenameNoExtension(fileName));
			button.setName(url == null
					?UrlToValueReader.parse(fileName, path)
							:url);
			comp = button;
		}
		else if(componentType.equals(JToggleButtonLengthLimited.class))
		{
			JToggleButtonLengthLimited button = new JToggleButtonLengthLimited();
			button.setPath(path);
			button.setText(fileName);
			button.setFullText(PathUtility.getFilenameNoExtension(fileName));
			button.setName(url == null
					?UrlToValueReader.parse(fileName, path)
							:url);
			comp = button;
		}
		return comp;
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
