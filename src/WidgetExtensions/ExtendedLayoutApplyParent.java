package WidgetExtensions;

import ClassDefintions.ClassAndSetters;
import WidgetComponents.ClassTypeHandler;
import WidgetUtility.WidgetComponent;

public class ExtendedLayoutApplyParent implements ExtendedAttributeStringParam
{
	private WidgetComponent parentComponent;
	private WidgetComponent component;
	
	public ExtendedLayoutApplyParent(WidgetComponent parent, WidgetComponent component)
	{
		this.parentComponent = parent;
		this.component = component;
	}
	
	@Override
	public void applyMethod(String layoutApplyParent)
	{
		ClassTypeHandler cthP = parentComponent.getComponentClassType();
		ClassAndSetters csP = ClassTypeHandler.getClassAndSetters(cthP);
		
		ClassTypeHandler cthC = component.getComponentClassType();
		ClassAndSetters csC = ClassTypeHandler.getClassAndSetters(cthC);
		
	}
}
