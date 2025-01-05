package WidgetUtility;

import java.util.List;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;

public class WidgetGenerator 
{
	public WidgetGenerator(List<WidgetCreatorProperty> widgets)
	{
		for(WidgetCreatorProperty w : widgets)
		{
			Object o = w.getInstance();
			List<XmlToWidgetGenerator> generators = w.getXmlToWidgetGenerators();
			for(XmlToWidgetGenerator g : generators)
			{
				Class<?> c = getExtendedAttribute(g.getMethodName());
				if(c != null)
				{
					String parent = w.getParentRef();
					WidgetCreatorProperty wc = findRef(parent, widgets);
					LoggingMessages.printOut("Extended Class: " + c.toString() + " **PARENT CLASS**: " + wc.getInstance());
				}
				else
				{
					g.generate(o);
				}
				
				LoggingMessages.printOut(g.toString());
			}
			LoggingMessages.printNewLine();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Class<? extends ExtendedAttributeStringParam> getExtendedAttribute(String methodName)
	{
		String me = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Class<? extends ExtendedAttributeStringParam> c = null;
		try {
			c = (Class<? extends ExtendedAttributeStringParam>) 
					Class.forName("WidgetExtensions" + "." + me);
		} catch (ClassNotFoundException e) {
			//return null
		}
		return c;
	}
	
	private static WidgetCreatorProperty findRef(String ref, List<WidgetCreatorProperty> widgets)
	{
		for(WidgetCreatorProperty wcp : widgets)
		{
			if(wcp.isThisParentRef(ref))
			{
				return wcp;
			}
		}
		return null;
	}
}
