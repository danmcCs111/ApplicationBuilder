package WidgetExtensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

/**
 * Holds a variable number of Components
 */
public class Collection extends JComponent
{
	private static final long serialVersionUID = 1L;
	
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
