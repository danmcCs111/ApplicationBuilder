package ApplicationBuilder;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetExtensions.ExtendedTextStripper;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.WidgetReader;

public class WidgetBuildController 
{
	private static final Class<?> [] FIRST_ORDERED_GENERATORS = new Class<?> [] {
		ExtendedLayoutApplyParent.class, ExtendedTextStripper.class
	};
	
	private static ArrayList<WidgetCreatorProperty> widgetCreatorProperties;
	private static WidgetBuildController widgetBuildController = WidgetBuildController.getInstance();
	
	private WidgetBuildController()
	{
		
	}
	
	public static WidgetBuildController getInstance() 
	{
		if(widgetBuildController == null)
		{
			widgetBuildController = new WidgetBuildController();
		}
		return widgetBuildController;
	}
	
	/**
	 * read the properties of the source file passed during construction
	 */
	public static void readProperties(String sourceFile)
	{
		destroyGeneratedFrame();
		widgetCreatorProperties = (WidgetReader.collectWidgetCreatorProperties(sourceFile));
		
		if(widgetCreatorProperties == null || widgetCreatorProperties.isEmpty())
		{
			LoggingMessages.printOut("No widget creation file found while using path: " + sourceFile);
			return;
		}
		
		LoggingMessages.printOut("-->Widget Creator Properties<--");
		
		for(WidgetCreatorProperty wcProp : widgetCreatorProperties)//TODO 
		{
			LoggingMessages.printOut(wcProp.toString());
			LoggingMessages.printNewLine();
		}
		
		LoggingMessages.printNewLine();
		LoggingMessages.printOut("-->Widget Generation<--");
		generateGraphicalInterface(widgetCreatorProperties);
		
		JFrame frame = (JFrame) widgetCreatorProperties.get(0).getInstance();
		
		frame.setVisible(true);
		
		printComponents((JComponent) frame.getComponent(0));
		
	}
	
	public static List<WidgetCreatorProperty> getWidgetCreationProperties()
	{
		return widgetCreatorProperties;
	}
	
	public static void destroyGeneratedFrame()
	{
		if(widgetCreatorProperties != null && !widgetCreatorProperties.isEmpty())
		{
			JFrame frame = (JFrame) widgetCreatorProperties.get(0).getInstance();
			frame.setVisible(false);
			frame.dispose();
			widgetCreatorProperties.clear();
		}
	}
	
	public static WidgetCreatorProperty findRef(String ref)
	{
		for(WidgetCreatorProperty wcp : getWidgetCreationProperties())
		{
			if(wcp.isThisRef(ref))
			{
				return wcp;
			}
		}
		return null;
	}
	
	private static void printComponents(JComponent frame)
	{
		for(Component c : frame.getComponents())
		{
			LoggingMessages.printOut(c.toString());
			if(c instanceof JComponent)
			{
				Component [] cs = ((JComponent)c).getComponents();
				if(cs.length > 0)
				{
					printComponents(((JComponent)c));
				}
			}
		}
	}
	
	private static ArrayList<XmlToWidgetGenerator> orderGenerators(ArrayList<XmlToWidgetGenerator> generators)
	{
		ArrayList<XmlToWidgetGenerator> tmp = new ArrayList<XmlToWidgetGenerator>();
		ArrayList<XmlToWidgetGenerator> orderedGenerators = new ArrayList<XmlToWidgetGenerator>();
		for(XmlToWidgetGenerator xwg : generators)
		{
			String methodName = xwg.getMethodName();
			for(Class<?> clazz : FIRST_ORDERED_GENERATORS)
			{
				if(clazz.getName().toLowerCase().contains(methodName.toLowerCase()))
				{
					orderedGenerators.add(xwg);
				}
			}
			if(!orderedGenerators.contains(xwg))
			{
				tmp.add(xwg);
			}
		}
		orderedGenerators.addAll(tmp);
		
		return orderedGenerators;
	}
	
	private static void generateGraphicalInterface (List<WidgetCreatorProperty> widgets)
	{
		for(WidgetCreatorProperty w : widgets)
		{
			Object o = w.getInstance();
			ArrayList<XmlToWidgetGenerator> generators = w.getXmlToWidgetGenerators();
			List<XmlToWidgetGenerator> orderedGenerators = orderGenerators(generators);
			for(XmlToWidgetGenerator g : orderedGenerators)
			{
				Class<? extends ExtendedAttributeStringParam> c = getExtendedAttribute(g.getMethodName());
				if(c != null)
				{
					String parent = w.getParentRef();
					WidgetCreatorProperty wc = findRef(parent);
					Object parentObj = null;
					if(wc != null)
					{
						parentObj = wc.getInstance();
					}
					LoggingMessages.printOut("Extended Class: " + c.toString() + " **PARENT CLASS**: " + parentObj);
					g.generateExtended(c, WidgetBuildController.getInstance(), w);
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
}
