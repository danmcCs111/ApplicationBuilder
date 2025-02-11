package WidgetUtility;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ApplicationBuilder.WidgetBuildController;
import ClassDefintions.ClassAndSetters;

public class ComponentSelector 
{
	public static ArrayList<String> generateComboSelectionOptions()
	{
		ArrayList<String> comboSel = new ArrayList<String>();
		List<WidgetCreatorProperty> props = WidgetBuildController.getWidgetCreationProperties();
		
		if(props == null || props.isEmpty())
		{
			comboSel.add(JFrame.class.getName());
		}
		else
		{
			ArrayList<ClassAndSetters> css = WidgetAttributes.getClassAndSetters();
			for(ClassAndSetters cs : css)
			{
				String name = cs.getClazz().getName();
				comboSel.add(name);
			}
		}
		return comboSel;
	}
	
	public static ArrayList<String> getParentContainerOptions()
	{
		ArrayList<String> containerOptions = new ArrayList<String>();
		List<WidgetCreatorProperty> props = WidgetBuildController.getWidgetCreationProperties();
		
		if(props == null || props.isEmpty())
		{
			containerOptions.add("");
		}
		else
		{
			for(WidgetCreatorProperty wcp : props)
			{
				String ref = wcp.getRefWithID();
				if(ref == null || ref.equals("")) 
					continue;
				if(!containerOptions.contains(ref) && wcp.getClassType().isContainer())
				{
					containerOptions.add(ref);
				}
			}
		}
		return containerOptions;
	}
	
}
