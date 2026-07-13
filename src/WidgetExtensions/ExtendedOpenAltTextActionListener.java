package WidgetExtensions;

import ObjectTypeConversion.NameId;
import Params.XmlToWidgetGenerator;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedOpenAltTextActionListener extends ExtendedOpenActionListener implements ExtendedAttributeParam
{
	private OpenActionExtension 
		oae = null;
	private WidgetCreatorProperty
		widgetProperties;
	private ExtendedTouchOptionLookup
		extendedTouchOptionLookup;
	private NameId
		nameId;
	
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		super.applyMethod(arg0, widgetProperties);
		nameId = arg0;
		this.widgetProperties = widgetProperties;
	}
	
	private void getOae()
	{
		Object o = WidgetBuildController.getInstance().findRefByName(nameId.getNameId()).getInstance();
		if(o instanceof OpenActionExtension)
		{
			oae = (OpenActionExtension) o;
		}
	}
	
	@Override
	public void performOpen()
	{
		if(oae == null)
		{
			getOae();
		}
		if(oae != null)
		{
			for(XmlToWidgetGenerator xtwg : widgetProperties.getXmlToWidgetGenerators())
			{
				Object o = xtwg.getExtendedAttributeObject(ExtendedTouchOptionLookup.class);
				if(o != null)
				{
					extendedTouchOptionLookup = (ExtendedTouchOptionLookup) o;
					break;
				}
			}
			if(extendedTouchOptionLookup != null)
			{
				if(extendedTouchOptionLookup.isTouch())
				{
					getOpenActionExtension().performOpenAltFont();
				}
				else
				{
					getOpenActionExtension().performOpen();
				}
			}
		}
		else
		{
			getOpenActionExtension().performOpen();
		}
	}
}

