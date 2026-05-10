package WidgetExtensions;

import WidgetExtensionDefs.ExtendedAttributeStringParam;

public class ExtendedOpenAltTextActionListener extends ExtendedOpenActionListener implements ExtendedAttributeStringParam
{
	@Override
	public void performOpen()
	{
		getOpenActionExtension().performOpenAltFont();
	}
}

