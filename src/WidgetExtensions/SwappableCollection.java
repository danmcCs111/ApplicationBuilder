package WidgetExtensions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JComponent
{
	private static final long serialVersionUID = 1L;

	//conceptually holding a collection of components to be swapped/redrawn
	public HashMap<String, List<JComponent>> collectionNameAndList = new HashMap<String, List<JComponent>>();
	
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
	
}
