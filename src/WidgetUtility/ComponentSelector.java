package WidgetUtility;

import java.util.ArrayList;

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
}
