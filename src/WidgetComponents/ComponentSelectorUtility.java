package WidgetComponents;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import ClassDefintions.ClassAndSetters;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ComponentSelectorUtility 
{
	public static ArrayList<String> generateComboSelectionOptions()
	{
		ArrayList<String> comboSel = new ArrayList<String>();
		List<WidgetCreatorProperty> props = WidgetBuildController.getInstance().getWidgetCreatorProperties();
		
		if(props == null || props.isEmpty())//TODO registered instanceof JFrame
		{
			comboSel.add(JFrame.class.getName());
			comboSel.add(ApplicationLayoutEditor.class.getName());
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
		List<WidgetCreatorProperty> props = WidgetBuildController.getInstance().getWidgetCreatorProperties();
		
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
	
	public static ArrayList<String> getComponentsFromParent(String parentRefId)//TODO performance
	{
		ArrayList<String> componentIncludeOptions = new ArrayList<String>();
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			if(parentRefId.equals(wcp.getParentRefWithID()))
			{
				componentIncludeOptions.add(wcp.getRefWithID());
			}
		}
		return componentIncludeOptions;
	}
	
	public static void setParentRefIdOnComponents(List<String> selectedRefIds, String parentRefId)//TODO performance
	{
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			for(String selId : selectedRefIds)
			{
				if(wcp.getRefWithID().equals(selId))
				{
					wcp.setParentRefWithID(parentRefId);
				}
			}
		}
	}
	
}
