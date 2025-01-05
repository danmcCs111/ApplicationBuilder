package ApplicationBuilder;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;
import WidgetUtility.WidgetReader;

public class WidgetBuildController 
{
	private ArrayList<WidgetCreatorProperty> widgetCreatorProperties;
	private String sourceFile;
	
	public WidgetBuildController(String sourceFile)
	{
		this.sourceFile = sourceFile;
		readProperties();
	}
	
	/**
	 * read the properties of the source file passed during construction
	 */
	private void readProperties()
	{
		widgetCreatorProperties = WidgetReader.getWidgetCreatorProperties(sourceFile);
		
		LoggingMessages.printOut("-->Widget Creator Properties<--");
		
		for(WidgetCreatorProperty wcProp : widgetCreatorProperties)//TODO 
		{
			LoggingMessages.printOut(wcProp.toString());
			LoggingMessages.printNewLine();
		}
		
		LoggingMessages.printNewLine();
		LoggingMessages.printOut("-->Widget Generation<--");
		generate(widgetCreatorProperties);
		
		JFrame frame = (JFrame) widgetCreatorProperties.get(0).getInstance();
		frame.setVisible(true);
		
		printComponents((JComponent) frame.getComponent(0));
		
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
	
	public List<WidgetCreatorProperty> getWidgetCreationProperties()
	{
		return this.widgetCreatorProperties;
	}
	
	private void generate (List<WidgetCreatorProperty> widgets)
	{
		for(WidgetCreatorProperty w : widgets)
		{
			Object o = w.getInstance();
			List<XmlToWidgetGenerator> generators = w.getXmlToWidgetGenerators();
			for(XmlToWidgetGenerator g : generators)
			{
				Class<? extends ExtendedAttributeStringParam> c = getExtendedAttribute(g.getMethodName());
				if(c != null)
				{
					String parent = w.getParentRef();
					WidgetCreatorProperty wc = findRef(parent);
					Object parentObj = wc.getInstance();
					LoggingMessages.printOut("Extended Class: " + c.toString() + " **PARENT CLASS**: " + parentObj);
					g.generateExtended(c, this, w);
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
	
	public WidgetCreatorProperty findRef(String ref)
	{
		for(WidgetCreatorProperty wcp : getWidgetCreationProperties())
		{
			if(wcp.isThisParentRef(ref))
			{
				return wcp;
			}
		}
		return null;
	}
}
