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

import ObjectTypeConvertersImpl.ClassAndSetters;
import ObjectTypeConvertersImpl.ClassTextAdapter;
import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;
import WidgetComponents.ApplicationLayoutEditor;
import WidgetComponents.DatabaseResponseNodeTextArea;
import WidgetComponents.JButtonArray;
import WidgetComponents.MenuOption;
import WidgetComponents.OutputWeatherResultsTextArea;
import WidgetComponents.SendHttpRequestPanel;
import WidgetComponents.SwappableCollection;
import WidgetComponents.WeatherButtonPanel;
import WidgetComponents.XmlToEditor;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ExtendedMethodArgDef;
import WidgetExtensionsImpl.ExtendedActionListenerArray;
import WidgetExtensionsImpl.ExtendedActionListenerConnectedComponent;
import WidgetExtensionsImpl.ExtendedActionListenerSubType;
import WidgetExtensionsImpl.ExtendedArrayProcessingPath;
import WidgetExtensionsImpl.ExtendedCalculationPad;
import WidgetExtensionsImpl.ExtendedCloseActionListener;
import WidgetExtensionsImpl.ExtendedCloseAllActionListener;
import WidgetExtensionsImpl.ExtendedDatabaseResponseNodeListener;
import WidgetExtensionsImpl.ExtendedLayoutApplyParent;
import WidgetExtensionsImpl.ExtendedImageMouseAdapterArray;
import WidgetExtensionsImpl.ExtendedOpenActionListener;
import WidgetExtensionsImpl.ExtendedSaveActionListener;
import WidgetExtensionsImpl.ExtendedScrollBarSetUnit;
import WidgetExtensionsImpl.ExtendedSetJMenuBarParent;
import WidgetExtensionsImpl.ExtendedSetViewportView;
import WidgetExtensionsImpl.ExtendedSetWeatherButtonListener;
import WidgetExtensionsImpl.ExtendedSetupTaskbar;
import WidgetExtensionsImpl.ExtendedSwappableHolder;
import WidgetExtensionsImpl.ExtendedTextStripper;
import WidgetExtensionsImpl.ExtendedTitleSwitcher;

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
		COMPONENT_CLASSES.add(JComboBox.class);
		COMPONENT_CLASSES.add(JComponent.class);
		COMPONENT_CLASSES.add(JMenuBar.class);
		COMPONENT_CLASSES.add(JMenu.class);
		COMPONENT_CLASSES.add(JMenuItem.class);
		
		COMPONENT_CLASSES.add(WeatherButtonPanel.class);
		COMPONENT_CLASSES.add(DatabaseResponseNodeTextArea.class);
		COMPONENT_CLASSES.add(SendHttpRequestPanel.class);
		COMPONENT_CLASSES.add(OutputWeatherResultsTextArea.class);
		COMPONENT_CLASSES.add(ApplicationLayoutEditor.class);
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
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetupTaskbar.class, ExtendedMethodArgDef.ExtendedFileSelection.getMethodArgDef()),
		});
		EXTENDED_METHODS.put(JPanel.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetViewportView.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JButton.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedActionListenerSubType.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedActionListenerConnectedComponent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JTextField.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JLabel.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedCalculationPad.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedTitleSwitcher.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JScrollPane.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedScrollBarSetUnit.class, ExtendedMethodArgDef.ExtendedScrollBarSetUnit.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JComboBox.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JComponent.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JMenuBar.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetJMenuBarParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JMenu.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JMenuItem.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSaveActionListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedOpenActionListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedCloseActionListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedCloseAllActionListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		
		//EXTENDED WIDGETS
		EXTENDED_METHODS.put(WeatherButtonPanel.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(DatabaseResponseNodeTextArea.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedDatabaseResponseNodeListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(SendHttpRequestPanel.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(OutputWeatherResultsTextArea.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetWeatherButtonListener.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(ApplicationLayoutEditor.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetupTaskbar.class, ExtendedMethodArgDef.ExtendedFileSelection.getMethodArgDef()),
		});
		EXTENDED_METHODS.put(SwappableCollection.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedArrayProcessingPath.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSetViewportView.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(JButtonArray.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedArrayProcessingPath.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedActionListenerArray.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedSwappableHolder.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedTextStripper.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedImageMouseAdapterArray.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
		});
		EXTENDED_METHODS.put(MenuOption.class, new String [] {
				ExtendedAttributeParam.getMethodDefinition(ExtendedLayoutApplyParent.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()),
				ExtendedAttributeParam.getMethodDefinition(ExtendedArrayProcessingPath.class, ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef())
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
