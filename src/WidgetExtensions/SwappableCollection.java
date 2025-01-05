package WidgetExtensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JPanel 
{
	private static final long serialVersionUID = 1L;

	//conceptually holding a collection of components to be swapped/redrawn
	private HashMap<String, List<JComponent>> collectionNameAndList = new HashMap<String, List<JComponent>>();
	private ExtendedArrayProcessingPath extendedArrayProcessingPath;
	String [] collectionName;
	
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
	
	public HashMap<String, List<String>> getCollectionNameAndList()
	{
		return this.extendedArrayProcessingPath.getPathAndFileList();
	}
	
}
