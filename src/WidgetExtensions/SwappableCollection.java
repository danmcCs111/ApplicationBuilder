package WidgetExtensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JPanel implements ExtendedStringCollection
{
	private static final long serialVersionUID = 1L;

	//conceptually holding a collection of components to be swapped/redrawn
	private HashMap<String, List<JComponent>> collectionNameAndList = new HashMap<String, List<JComponent>>();
	private HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
	
	public void addJComponent(String collectionName, JComponent component)
	{
		if(collectionNameAndList.containsKey(collectionName))
		{
			collectionNameAndList.get(collectionName).add(component);
		}
		else
		{
			collectionNameAndList.put(collectionName, Arrays.asList(component));
		}
	}
	
	@Override
	public HashMap<String, List<String>> getPathAndFileList() 
	{
		return this.pathAndFileList;
	}

	@Override
	public void setPathAndFileList(HashMap<String, List<String>> pathAndFileList) 
	{
		this.pathAndFileList = pathAndFileList;
	}
	
}
