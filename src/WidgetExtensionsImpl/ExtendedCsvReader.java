package WidgetExtensionsImpl;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ActionListenersImpl.CsvReaderListener;
import ObjectTypeConversion.CsvReader;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCsvReader implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		if(m instanceof AbstractButton)//TODO
		{
			AbstractButton mi = ((AbstractButton)m);
			if(arg0 != null && !arg0.strip().isEmpty())
			{
				for(ActionListener al : mi.getActionListeners())
				{
					if(al instanceof CsvReaderListener)
					{
						((CsvReaderListener) al).setCsvReader(new CsvReader(arg0));
					}
				}
			}
		}
	}
}
