package WidgetExtensions;

import WidgetUtility.WidgetCreatorProperty;

public class ExtendedActionListenerSubTypeAttribute implements ExtendedAttributeStringParam
{
	private String subTypeAttribute = null;
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties)
	{
		this.subTypeAttribute = arg0;
	}
	
	public String getSubTypeAttribute()
	{
		return this.subTypeAttribute;
	}
}
