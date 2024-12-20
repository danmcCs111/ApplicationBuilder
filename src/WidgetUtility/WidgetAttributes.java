package WidgetUtility;

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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ClassDefintions.ClassAndSetters;
import ClassDefintions.ClassTextAdapter;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ClassTypeHandler;
import WidgetExtensions.ExtendedActionListenerSubType;
import WidgetExtensions.ExtendedLayoutApplyParent;

public class WidgetAttributes {
	
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
		COMPONENT_CLASSES.add(JMenuItem.class);
	}
	//TODO replace :(
	private static final HashMap<Class<?>, String []> EXTENDED_METHODS = new HashMap<Class<?>, String []>();
	static {
		EXTENDED_METHODS.put(JFrame.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JPanel.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JButton.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition(),
				ExtendedActionListenerSubType.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JTextField.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JScrollPane.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JComboBox.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JComponent.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
		EXTENDED_METHODS.put(JMenuItem.class, new String [] {
				ExtendedLayoutApplyParent.getMethodDefinition()
		});
	}
	static {
		initialLoad();
	}
	

	public static XmlToWidgetGenerator setAttribute(ClassTypeHandler classTypeHandler, String method, String ... params)
	{
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
	
	public static void addClassAndSetters(ClassAndSetters classAndSetters)
	{
		classesAndSetters.add(classAndSetters);
	}
	
	public static ArrayList<ClassAndSetters> getClassAndSetters()
	{
		return classesAndSetters;
	}
	
	private static void initialLoad()
	{
		//discover a list of methods available to adjust for our available list of components
		HashMap<String, ArrayList<String>> tmpClassesAndSetters = new HashMap<String, ArrayList<String>>();
		
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
