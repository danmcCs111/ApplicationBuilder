package WidgetExtensions;

import ObjectTypeConversion.PageParser;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.PageParserLoad;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedPageParser implements ExtendedAttributeParam 
{
	public void applyMethod(PageParser arg0, WidgetCreatorProperty widgetProperties) 
	{
		PageParserLoad ppl =  (PageParserLoad) widgetProperties.getInstance();
		ppl.addPageParser(arg0);
	}

}
