package WidgetUtility;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetExtensions.ExtendedTextStripper;

public class WidgetBuildController 
{
	private static final Class<?> [] FIRST_ORDERED_GENERATORS = new Class<?> [] {
		ExtendedLayoutApplyParent.class, 
		ExtendedTextStripper.class
	};
	
	private static ArrayList<WidgetBuildController> widgetBuildController = new ArrayList<WidgetBuildController> ();
	static {
		widgetBuildController.add(new WidgetBuildController());
	}
	private static ArrayList<WidgetReader> widgetReaders = new ArrayList<WidgetReader>();
	
	private static int selInstance = 0;
	
	private WidgetBuildController() {
		//adjusted to controlled instances.
	}
	
	public static WidgetBuildController getInstance() 
	{
		return getInstance(selInstance);
	}
	
	public static WidgetReader getWidgetReader()
	{
		return getWidgetReader(selInstance);
	}
	public static WidgetReader getWidgetReader(int sel)
	{
		return (widgetReaders.isEmpty())
			? null
			: widgetReaders.get(sel);
	}
	
	public static WidgetBuildController getInstance(int sel) 
	{
		return widgetBuildController.get(sel);
	}
	
	
	public void readProperties(File sourceFile)
	{
		readProperties(sourceFile.getAbsolutePath());
	}
	/**
	 * read the properties of the source file passed during construction
	 */
	public void readProperties(String sourceFile)
	{
		destroyGeneratedFrame();
		clearWidgetCreatorProperties();
		
		if(!widgetReaders.isEmpty())
			widgetReaders.set(selInstance, new WidgetReader(sourceFile));
		else
			widgetReaders.add(new WidgetReader(sourceFile));
		
		if(getWidgetCreatorProperties() == null || getWidgetCreatorProperties().isEmpty())
		{
			LoggingMessages.printOut("No widget creation file found while using path: " + sourceFile);
			return;
		}
		
		LoggingMessages.printOut("-->Widget Creator Properties<--");
		
		for(WidgetCreatorProperty wcProp : getWidgetCreatorProperties())//TODO print output
		{
			LoggingMessages.printOut(wcProp.toString());
			LoggingMessages.printNewLine();
		}
		
	}
	
	public void generateGraphicalInterface()
	{
		LoggingMessages.printNewLine();
		LoggingMessages.printOut("-->Widget Generation<--");
		
		for(WidgetCreatorProperty w : getWidgetCreatorProperties())
		{
			Object o = w.getInstance();
			ArrayList<XmlToWidgetGenerator> generators = w.getXmlToWidgetGenerators();
			List<XmlToWidgetGenerator> orderedGenerators = orderGenerators(generators);
			for(XmlToWidgetGenerator g : orderedGenerators)
			{
				Class<? extends ExtendedAttributeStringParam> c = getExtendedAttribute(g.getMethodName());
				if(c != null)
				{
					String parent = w.getParentRefWithID();
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
		
		if(getWidgetCreatorProperties() != null && !getWidgetCreatorProperties().isEmpty())
		{
			JFrame frame = (JFrame) getWidgetCreatorProperties().get(0).getInstance();
			frame.setVisible(true);
			printComponents((JComponent) frame.getComponent(0));
		}
	}
	
	public List<WidgetCreatorProperty> getWidgetCreatorProperties()
	{
		return getWidgetReader()==null
				? null
				:getWidgetReader().getWidgetCreatorProperties();
	}
	
	public void addWidgetCreatorProperty(WidgetCreatorProperty wcp)
	{
		addWidgetCreatorProperty(wcp, false);
	}
	
	public void addWidgetCreatorProperty(WidgetCreatorProperty wcp, boolean inPlace)//TODO append/order insert?
	{
		if(inPlace == false)
		{
			getWidgetCreatorProperties().add(wcp);
			return;
		}
		
		if(wcp.getParentRef() != null && !wcp.getParentRef().isBlank())
		{
			WidgetCreatorProperty wcpParent = findRef(wcp.getParentRefWithID());
			LoggingMessages.printOut(wcpParent.toString());
			
			//TODO
			int indexAfter = getWidgetCreatorProperties().indexOf(wcpParent)+1;
			WidgetCreatorProperty wcpReplace;
			if(getWidgetCreatorProperties().size()-1 >= indexAfter)
			{
				wcpReplace = getWidgetCreatorProperties().get(indexAfter);
				getWidgetCreatorProperties().set(indexAfter, wcp);
				
				if(getWidgetCreatorProperties().size() > indexAfter)//has additional in list
				{
					List<WidgetCreatorProperty> sublist = getWidgetCreatorProperties().subList(
						indexAfter+1, getWidgetCreatorProperties().size());
					getWidgetReader().setWidgetCreatorProperties(new ArrayList<WidgetCreatorProperty>(getWidgetCreatorProperties().subList(0, indexAfter+1)));
					getWidgetCreatorProperties().add(wcpReplace);
					getWidgetCreatorProperties().addAll(sublist);
					LoggingMessages.printOut("index after. " + indexAfter);
					LoggingMessages.printOut("list count: " + getWidgetCreatorProperties().size());
				}
				else //was replacing on last in list
				{
					getWidgetCreatorProperties().add(wcpReplace);
				}
			}
			else //it is the last in the list
			{
				getWidgetCreatorProperties().add(wcp);
			}
		}
		else //no parent entered so root.
		{
			getWidgetCreatorProperties().add(wcp);
		}
	}
	
	public void destroyGeneratedFrame()
	{
		if(getWidgetCreatorProperties() != null && !getWidgetCreatorProperties().isEmpty())
		{
			JFrame frame = (JFrame) getWidgetCreatorProperties().get(0).getInstance();
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	public void clearWidgetCreatorProperties()
	{
		if(getWidgetCreatorProperties() != null && !getWidgetCreatorProperties().isEmpty())
		{
			getWidgetReader().clearWidgetCreatorProperties();
			WidgetCreatorProperty.resetIDCounter();
			WidgetComponent.resetIDCounter();
		}
	}
	
	//TODO use more refined param, id?
	public WidgetCreatorProperty findRef(String ref)
	{
		for(WidgetCreatorProperty wcp : getWidgetCreatorProperties())
		{
			if(wcp.isThisRef(ref))
			{
				return wcp;
			}
		}
		return null;
	}
	
	private void printComponents(JComponent frame)
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
	
	private ArrayList<XmlToWidgetGenerator> orderGenerators(ArrayList<XmlToWidgetGenerator> generators)
	{
		ArrayList<XmlToWidgetGenerator> 
			tmp = new ArrayList<XmlToWidgetGenerator>(),
			orderedGenerators = new ArrayList<XmlToWidgetGenerator>();
		
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
	
	@SuppressWarnings("unchecked")
	private Class<? extends ExtendedAttributeStringParam> getExtendedAttribute(String methodName)
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
