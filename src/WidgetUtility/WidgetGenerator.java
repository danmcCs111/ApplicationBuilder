package WidgetUtility;

import java.util.List;

import Params.XmlToWidgetGenerator;

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
				g.generate(o);
			}
		}
	}
}
