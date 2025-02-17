package WidgetExtensions;

import WidgetComponents.CharacterLimited;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCharacterLimited implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		CharacterLimited cl = (CharacterLimited) widgetProperties.getInstance();
		cl.setCharacterLimit(Integer.parseInt(arg0));
	}

}
