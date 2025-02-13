package WidgetUtility;

import java.awt.SystemTray;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.ClassAndSetters;
import ClassDefintions.ClassTextAdapter;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ClassTypeHandler;
import WidgetExtensions.SwappableCollection;
import WidgetExtensions.XmlToEditor;
import WidgetExtensions.ApplicationLayoutEditor2;
import WidgetExtensions.ExtendedActionListenerArray;
import WidgetExtensions.ExtendedActionListenerConnectedComponent;
import WidgetExtensions.ExtendedActionListenerSubType;
import WidgetExtensions.ExtendedArrayProcessingPath;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedCalculationPad;
import WidgetExtensions.ExtendedClipboard;
import WidgetExtensions.ExtendedFrameResizer;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetExtensions.ExtendedSetJMenuBarParent;
import WidgetExtensions.ExtendedSetViewportView;
import WidgetExtensions.ExtendedSetupTaskbar;
import WidgetExtensions.ExtendedSwappableHolder;
import WidgetExtensions.ExtendedTextStripper;
import WidgetExtensions.ExtendedTitleSwitcher;
import WidgetExtensions.JButtonArray;
import WidgetExtensions.JScrollPaneResizer;
import WidgetExtensions.MenuOption;

public class WidgetAttributes 
{
	private static ArrayList<ClassAndSetters> classesAndSetters = new ArrayList<ClassAndSetters>();
	private static final String [] METHODS_PREFIX = new String [] {"set", "add"};
	private static final ArrayList<Class<?>> COMPONENT_CLASSES = new ArrayList<Class<?>>();
	static {
		COMPONENT_CLASSES.add(JFrame.class);
		COMPONENT_CLASSES.add(JPanel.class);
		COMPONENT_CLASSES.add(JButton.class);
		COMPONENT_CLASSES.add(JTextField.class);
		COMPONENT_CLASSES.add(JLabel.class);
		COMPONENT_CLASSES.add(JScrollPane.class);
		COMPONENT_CLASSES.add(JScrollPaneResizer.class);
		COMPONENT_CLASSES.add(JComboBox.class);
		COMPONENT_CLASSES.add(JComponent.class);
		COMPONENT_CLASSES.add(JMenuBar.class);
		COMPONENT_CLASSES.add(JMenu.class);
		COMPONENT_CLASSES.add(JMenuItem.class);
		
		COMPONENT_CLASSES.add(ApplicationLayoutEditor2.class);
		COMPONENT_CLASSES.add(XmlToEditor.class);
		COMPONENT_CLASSES.add(SystemTray.class);
		COMPONENT_CLASSES.add(SwappableCollection.class);
		COMPONENT_CLASSES.add(JButtonArray.class);
		COMPONENT_CLASSES.add(MenuOption.class);
	}
	//TODO replace :(
	private static final HashMap<Class<?>, String []> EXTENDED_METHODS = new HashMap<Class<?>, String []>();
	static {
		EXTENDED_METHODS.put(JFrame.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedFrameResizer.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedSetupTaskbar.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedClipboard.class)
		});
		EXTENDED_METHODS.put(JPanel.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedSetViewportView.class)
		});
		EXTENDED_METHODS.put(JButton.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedActionListenerSubType.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedActionListenerConnectedComponent.class)
		});
		EXTENDED_METHODS.put(JTextField.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JLabel.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedCalculationPad.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedTitleSwitcher.class)
				
		});
		EXTENDED_METHODS.put(JScrollPane.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JScrollPaneResizer.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JComboBox.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JComponent.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JMenuBar.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedSetJMenuBarParent.class)
		});
		EXTENDED_METHODS.put(JMenu.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		EXTENDED_METHODS.put(JMenuItem.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class)
		});
		
		//EXTENDED WIDGETS
		EXTENDED_METHODS.put(SwappableCollection.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedArrayProcessingPath.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedSetViewportView.class)
		});
		EXTENDED_METHODS.put(JButtonArray.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedArrayProcessingPath.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedActionListenerArray.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedSwappableHolder.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedTextStripper.class)
				
		});
		EXTENDED_METHODS.put(MenuOption.class, new String [] {
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class),
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedArrayProcessingPath.class)
		});
		
	}
	static {
		initialLoad();
	}
	

	public static XmlToWidgetGenerator setAttribute(ClassTypeHandler classTypeHandler, String method, String ... params)
	{
		JScrollPane pane = new JScrollPane();
		pane.setVisible(false);
		XmlToWidgetGenerator xmlToWidgetGenerator = null;
		ClassAndSetters tmp = ClassTypeHandler.getClassAndSetters(classTypeHandler);
		String setter = null; 
		if(tmp != null)
		{
			setter = tmp.getSetter(method);
			if(setter != null)
			{
				xmlToWidgetGenerator = ClassTextAdapter.functionCall(tmp.getClazz(), setter, method, params);
			}
		}
		return xmlToWidgetGenerator;
	}
	
	public static XmlToWidgetGenerator setAttribute(ClassTypeHandler classTypeHandler, String method, String params)
	{
		List<String> paramList = new ArrayList<String>();
		
		String [] args = params.split(",");
		
		for(String arg : args)
		{
			arg = arg.trim();
			arg = arg.replace("\"", "");
			
			//TODO setAttribute
			paramList.add(arg);
		}
		return setAttribute(classTypeHandler, method,  paramList.toArray(new String []{}));
	}
	
	public static XmlToWidgetGenerator setAttribute(ClassTypeHandler classTypeHandler, String methodDef)
	{
		methodDef = methodDef.replaceAll("\\[.*", "");
		methodDef = methodDef.trim();
		LoggingMessages.printOut(methodDef);
		return setAttribute(classTypeHandler, methodDef,  new String []{""});
	}
	
	public static void addClassAndSetters(ClassAndSetters classAndSetters)
	{
		classesAndSetters.add(classAndSetters);
	}
	
	public static ArrayList<ClassAndSetters> getClassAndSetters()
	{
		return classesAndSetters;
	}
	
	public static String getClassNameString(String classNameSuffix)
	{
		String packageName = "";
		for(Class<?> c : COMPONENT_CLASSES)
		{
			if(c.getName().endsWith(classNameSuffix))
			{
				if(!classNameSuffix.startsWith(c.getName()))
				{
					packageName = c.getPackageName()+".";//include package name 
				}
				break;
			}
		}
		return packageName + classNameSuffix;
	}
	
	private static void initialLoad()
	{
		//discover a list of methods available to adjust for our available list of components
		HashMap<String, ArrayList<String>> tmpClassesAndSetters = new HashMap<String, ArrayList<String>>();
		
		//add standard library methods
		for(Class<?> componentClass : COMPONENT_CLASSES)
		{
			String classKey = componentClass.getName();
			for(String prefix : METHODS_PREFIX)
			{
				if(tmpClassesAndSetters.containsKey(classKey))
				{
					tmpClassesAndSetters.get(classKey).addAll(generateClassesMethodApiList(componentClass, prefix));
				}
				else
				{
					tmpClassesAndSetters.put(classKey, generateClassesMethodApiList(componentClass, prefix));
				}
			}
		}
		//add extended methods
		for(String classStr : tmpClassesAndSetters.keySet())
		{
			ClassAndSetters cs = new ClassAndSetters(classStr, tmpClassesAndSetters.get(classStr));
			Set<Class<?>> exList = EXTENDED_METHODS.keySet();
			if(exList.contains(cs.getClazz()))
			{
				cs.addExtendedSetters(EXTENDED_METHODS.get(cs.getClazz()));
			}
			WidgetAttributes.addClassAndSetters(cs);
		}
	}
	
	private static ArrayList<String> generateClassesMethodApiList(Class<?> componentClass, String methodPrefixFilter)
	{
		ArrayList<String> classMethods = new ArrayList<String>();
		
		for (Method m : componentClass.getMethods())
		{
			String methodName = m.getName();
			String paramName = " [";
			for (int i =0; i < m.getParameterCount(); i++)
			{
				Parameter p = m.getParameters()[i];
				paramName += p.toString();
				if(m.getParameterCount() > i+1)
				{
					paramName += ", ";
				}
			}
			methodName += paramName + "]";
			if(methodName.startsWith(methodPrefixFilter))
			{
				classMethods.add(methodName);
			}
		}
		return classMethods;
	}
}
