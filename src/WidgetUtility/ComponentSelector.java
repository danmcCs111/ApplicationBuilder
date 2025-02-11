package WidgetUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.ClassAndSetters;

public class ComponentSelector 
{
	public static ArrayList<String> generateComboSelectionOptions()
	{
		ArrayList<String> comboSel;
		comboSel = new ArrayList<String>();
		ArrayList<ClassAndSetters> css = WidgetAttributes.getClassAndSetters();
		for(ClassAndSetters cs : css)
		{
			String name = cs.getClazz().getName();
			comboSel.add(name);
		}
		return comboSel;
	}
	
	public static List<String> getParentContainerOptions()
	{
		List<WidgetCreatorProperty> props = WidgetBuildController.getWidgetCreationProperties();
		if(props == null || props.isEmpty()) return Arrays.asList(new String [] {"JFrame"});
		
		ArrayList<String> containerOptions = new ArrayList<String>();
		for(WidgetCreatorProperty wcp : props)
		{
			String parent = wcp.getParentRefWithID();
			if(parent == null || parent.equals("")) 
				continue;
			if(!containerOptions.contains(parent))
			{
				containerOptions.add(wcp.getParentRefWithID());
			}
		}
		
		return containerOptions;
	}
	
}
