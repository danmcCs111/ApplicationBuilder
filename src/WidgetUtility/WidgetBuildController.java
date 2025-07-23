package WidgetUtility;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import Params.ParameterEditor;
import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensionsImpl.ExtendedLayoutApplyParent;
import WidgetExtensionsImpl.ExtendedSwappableHolder;
import WidgetExtensionsImpl.ExtendedTextStripper;

public class WidgetBuildController 
{
	private static final String EXTENSIONS_PACKAGE_NAME = "WidgetExtensionsImpl";
	
	private static final Class<?> [] FIRST_ORDERED_GENERATORS = new Class<?> [] {
		ExtendedLayoutApplyParent.class, 
		ExtendedTextStripper.class
	};
	
	private static final Class<?> [] LAST_ORDERED_GENERATORS = new Class<?> [] {
		ExtendedSwappableHolder.class
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
	
	public void newWidgetBuild()
	{
		getInstance(++selInstance);
		widgetReaders.add(new WidgetReader(null));
		WidgetComponent.resetIDCounter();	
	}
	
	public String getFilename()
	{
		return getWidgetReader(selInstance).getSourceFileAbsolutePath();
	}
	
	public void clearFilename()
	{
		getWidgetReader(selInstance).clearSourceFile();
	}
	
	public static int getGeneratedNum()
	{
		return widgetBuildController.size();
	}
	public static void setSelectionIndex(int sel)
	{
		selInstance = sel;
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
		return (widgetReaders.isEmpty() || widgetReaders.size() -1 < sel)
			? null
			: widgetReaders.get(sel);
	}
	
	public static WidgetBuildController getInstance(int sel) 
	{
		if(widgetBuildController.size()-1 < sel)
		{
			widgetBuildController.add(new WidgetBuildController());
		}
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
		
		if(!widgetReaders.isEmpty() && widgetReaders.size()-1 >= selInstance)
			widgetReaders.set(selInstance, new WidgetReader(sourceFile));
		else
		{
			widgetReaders.add(new WidgetReader(sourceFile));
			WidgetComponent.resetIDCounter();			
		}
		
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
	
	public void generateGraphicalInterface(boolean replaceWithEditorValues)
	{
		LoggingMessages.printNewLine();
		LoggingMessages.printOut("-->Widget Generation<--");
		
		if(getWidgetReader().getSourceFileAbsolutePath() != null && getWidgetCreatorProperties().isEmpty())
		{
			readProperties(getWidgetReader().getSourceFileAbsolutePath());
		}
		
		ArrayList<PostWidgetBuildProcessing> postGenerationComponents = new ArrayList<PostWidgetBuildProcessing>();
		for(WidgetCreatorProperty w : getWidgetCreatorProperties())
		{
			Object o = w.getInstance();
			if(o instanceof PostWidgetBuildProcessing)
			{
				postGenerationComponents.add((PostWidgetBuildProcessing) o);
			}
			ArrayList<XmlToWidgetGenerator> generators = w.getXmlToWidgetGenerators();
			List<XmlToWidgetGenerator> orderedGenerators = orderGenerators(generators);
			for(XmlToWidgetGenerator g : orderedGenerators)
			{
				if(replaceWithEditorValues) g.replaceParamsListWithParamEditors();
				Class<? extends ExtendedAttributeParam> c = getExtendedAttribute(g.getMethodName());
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
		
		for(PostWidgetBuildProcessing pp : postGenerationComponents)//Post build processing.
		{
			pp.postExecute();
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
					getWidgetReader().setWidgetCreatorProperties(
							new ArrayList<WidgetCreatorProperty>(getWidgetCreatorProperties().subList(0, indexAfter+1))
					);
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
			JFrame frame = (JFrame) getWidgetCreatorProperties().get(0).getInstance();//TODO
//			frame.setVisible(false);
//			for(WidgetCreatorProperty wcp : getWidgetCreatorProperties()) wcp.destroy();
			frame.dispose();
		}
	}
	
	public void clearWidgetCreatorProperties()
	{
		if(getWidgetCreatorProperties() != null && !getWidgetCreatorProperties().isEmpty())
		{
			getWidgetReader().clearWidgetCreatorProperties();
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
	
	public WidgetCreatorProperty findRefByName(String name)
	{
		for(WidgetCreatorProperty wcp : getWidgetCreatorProperties())
		{
			Object o = wcp.getInstance();
			if(o instanceof Component)
			{
				if(((Component) o).getName() != null && ((Component) o).getName().equals(name))
				{
					return wcp;
				}
			}
		}
		return null;
	}
	
	public JFrame getFrame()
	{
		return (JFrame) getWidgetCreatorProperties().get(0).getInstance();
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
			tmpL = new ArrayList<XmlToWidgetGenerator>(),
			orderedGenerators = new ArrayList<XmlToWidgetGenerator>();
			
		outer:
		for(XmlToWidgetGenerator xwg : generators)
		{
			String methodName = xwg.getMethodName();
			for(Class<?> clazz : FIRST_ORDERED_GENERATORS)
			{
				if(clazz.getName().toLowerCase().contains(methodName.toLowerCase()))
				{
					orderedGenerators.add(xwg);
					continue outer;
				}
			}
			for(Class<?> clazz : LAST_ORDERED_GENERATORS)
			{
				if(clazz.getName().toLowerCase().contains(methodName.toLowerCase()))
				{
					tmpL.add(xwg);
					continue outer;
				}
			}
			tmp.add(xwg);
		}
		orderedGenerators.addAll(tmp);
		orderedGenerators.addAll(tmpL);
		
		return orderedGenerators;
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends ExtendedAttributeParam> getExtendedAttribute(String methodName)
	{
		String me = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Class<? extends ExtendedAttributeParam> c = null;
		try {
			c = (Class<? extends ExtendedAttributeParam>) 
					Class.forName(EXTENSIONS_PACKAGE_NAME + "." + me);
		} catch (ClassNotFoundException e) {
			//return null
		}
		return c;
	}
	
	public void destroyFrameAndCreatorProperties()
	{
		destroyGeneratedFrame();
		clearWidgetCreatorProperties();
	}
	
	public void destroyEditors()
	{
		for(WidgetCreatorProperty wcp : getWidgetCreatorProperties())
		{
			ArrayList<XmlToWidgetGenerator> xmlToWidgetGenerators = wcp.getXmlToWidgetGenerators();
			if(xmlToWidgetGenerators != null)
			{
				for(XmlToWidgetGenerator xmlGen : xmlToWidgetGenerators)
				{
					for(ParameterEditor pe : xmlGen.getParameterEditors())
					{
						pe.destroy();
					}
				}
			}
		}
	}
}
