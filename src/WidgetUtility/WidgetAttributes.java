package WidgetUtility;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.ClassAndSetters;
import WidgetComponents.ClassTypeHandler;

public class WidgetAttributes {
	
	private static final ArrayList<Class<?>> COMPONENT_CLASSES = new ArrayList<Class<?>>();
	private static ArrayList<ClassAndSetters> classesAndSetters = new ArrayList<ClassAndSetters>();
	static {
		COMPONENT_CLASSES.add(JFrame.class);
		COMPONENT_CLASSES.add(JPanel.class);
		COMPONENT_CLASSES.add(JButton.class);
		COMPONENT_CLASSES.add(JTextField.class);
		COMPONENT_CLASSES.add(JLabel.class);
		COMPONENT_CLASSES.add(JScrollPane.class);
		COMPONENT_CLASSES.add(JComboBox.class);
		COMPONENT_CLASSES.add(JComponent.class);
		initialLoad();
	}
	

	public static void setAttribute(ClassTypeHandler classTypeHandler, String method, String ... params)
	{
		ClassAndSetters tmp = ClassTypeHandler.getClassAndSetters(classTypeHandler);
		for(String p : params)
		{
			LoggingMessages.printOut("method :"  + method + ": " + p);
		}
	}
	
	public static void setAttribute(ClassTypeHandler classTypeHandler, String method, String params)
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
		setAttribute(classTypeHandler, method,  paramList.toArray(new String []{}));
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
		HashMap<String, ArrayList<String>> tmpClassesAndSetters = generateClassesMethodApiList("set");
		for(String classStr : tmpClassesAndSetters.keySet())
		{
			WidgetAttributes.addClassAndSetters(new ClassAndSetters(classStr, tmpClassesAndSetters.get(classStr)));
		}
	}
	
	private static HashMap<String, ArrayList<String>> generateClassesMethodApiList(String methodPrefixFilter)
	{
		HashMap<String, ArrayList<String>> classMethods = new HashMap<String, ArrayList<String>>();
		
		for(Class<?> c : COMPONENT_CLASSES)
		{
			String classNameKey = c.getName();
			ArrayList<String> tmp = new ArrayList<String>();
			classMethods.put(classNameKey, tmp);
			
			for (Method m : c.getMethods())
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
					if(classMethods.containsKey(classNameKey))
					{
						classMethods.get(classNameKey).add(methodName);
					}
				}
			}
		}
		return classMethods;
	}
}
