package WidgetUtility;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import Properties.PathUtility;
import Properties.UrlToValueReader;
import WidgetComponents.JButtonLengthLimited;

public class FileListOptionGenerator 
{
	private static final String IMAGES_RELATIVE_FILE_LOCATION= "/images/";
	
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
				JButtonLengthLimited button = new JButtonLengthLimited();
				button.setText(fileName);
				button.setName(UrlToValueReader.parse(fileName, path));
				components.add(button);
			}
		}
//		else if(componentType.equals(JCheckBoxLimited.class))
//		{
//			for(String fileName: fileNames)
//			{
//				JCheckBoxLimited button = new JCheckBoxLimited();
//				String fileImage = PathUtility.getCurrentDirectory() +
//						PathUtility.removeCurrentWorkingDirectoryFromPath(path) +
//						IMAGES_RELATIVE_FILE_LOCATION +
//						fileName.replaceAll(".url", ".png");
//				LoggingMessages.printOut(fileImage);
//				Image img = setupImage(new File(fileImage), new File(DEFAULT_IMG));
//				button.setIcon(new ImageIcon(img));
//				button.setText(fileName);
////				button.setName(fileName);
//				button.setToolTipText(fileName);
//				button.setPathKey(path);
//				button.setBorderPainted(true);
//				button.setName(UrlToValueReader.parse(fileName, path));
//				components.add(button);
//			}
//		}
		return components;
	}
	
//	public static List<AbstractButton> addIconToComponents(List<AbstractButton> components)
//	{
//		for(AbstractButton com : components)
//		{
//			com.setIcon(null);
//		}
//	}
//	
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
