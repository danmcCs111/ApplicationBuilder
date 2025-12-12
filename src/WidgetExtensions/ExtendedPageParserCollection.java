package WidgetExtensions;

import ObjectTypeConversion.PageParserCollection;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.PageParserCollectionLoad;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedPageParserCollection implements ExtendedAttributeParam 
{
	public void applyMethod(PageParserCollection arg0, WidgetCreatorProperty widgetProperties) 
	{
		PageParserCollectionLoad ppl =  (PageParserCollectionLoad) widgetProperties.getInstance();
		ppl.addPageParserCollection(arg0);
	}

}
