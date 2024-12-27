package WidgetUtility;

import java.util.List;

import ApplicationBuilder.LoggingMessages;
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
				LoggingMessages.printOut(g.toString());
				g.generate(o);
			}
		}
	}
}
