package ApplicationBuilder;

import java.awt.BorderLayout;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Properties.WidgetTextProperties;

public class BuilderWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String SOURCE_FILE = "src\\ApplicationBuilder\\data\\WidgetBuild.xml";
	
	public BuilderWindow()
	{
		new WidgetBuildController(SOURCE_FILE);
		
//		addMenuButtons();
		
		setTitle("ApplicationBuilder");
		setLocation(150, 150);
		
//		BorderLayout bl = new BorderLayout();
//		scrPane = new JScrollPane(innerPanel);
//		BorderLayout bl2 = new BorderLayout();
//		innerPanel2.setLayout(bl2);
//		innerPanel2.add(scrPane, BorderLayout.NORTH);
//		this.setLayout(bl);
//		this.add(innerPanel2, BorderLayout.CENTER);
//		
//		createNavigationButtons();
//		addChannelButtons();
//		
//		this.setSize(getWindowWidth(), winHeight);
//		this.addWindowListener(new RokuLauncherWindowListener(this));
//		this.addComponentListener(new RokuLauncherComponentListener(this));

		HashMap<String, ArrayList<String>> classesAndSetters = generateClassesMethodApiList("set");
		ArrayList<String> tmp = new ArrayList<String>(classesAndSetters.keySet());
		String [] selections = tmp.toArray(new String [tmp.size()]);
		for(String classStr : classesAndSetters.keySet())
		{
			for(String s : classesAndSetters.get(classStr))
				LoggingMessages.printOut(classStr + ": " + s.toString());
		}
		JComboBox<String> classSelection = new JComboBox<String>(selections);
		classSelection.setSize(100, 50);
		classSelection.setVisible(true);

		
		this.add(classSelection, BorderLayout.NORTH);
		
		this.setSize(480, 640);
		
//		HashMap<String, ArrayList<String>> settersAndClass = generateUniqueMethodApiList("set");
//		for(String methodStr : settersAndClass.keySet())
//		{
//			LoggingMessages.printOut(settersAndClass.get(methodStr).toString() + ": " + methodStr);
//		}
//		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public HashMap<String, ArrayList<String>> generateClassesMethodApiList(String methodPrefixFilter)
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(JFrame.class);
		classes.add(JPanel.class);
		classes.add(JButton.class);
		classes.add(JTextField.class);
		classes.add(JLabel.class);
		
		HashMap<String, ArrayList<String>> classMethods = new HashMap<String, ArrayList<String>>();
		
		for(Class<?> c : classes)
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
	
	public HashMap<String, ArrayList<String>> generateUniqueMethodApiList(String methodPrefixFilter)
	{
		HashMap<String, ArrayList<String>> settersAndClass = new HashMap<String, ArrayList<String>>();
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(JFrame.class);
		classes.add(JPanel.class);
		classes.add(JButton.class);
		classes.add(JTextField.class);
		classes.add(JLabel.class);
		
		for(Class<?> c : classes)
		{
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
					if(settersAndClass.containsKey(methodName))
					{
						settersAndClass.get(methodName).add(c.getName());
					}
					else
					{
						ArrayList<String> tmp = new ArrayList<String>();
						tmp.add(c.getName());
						settersAndClass.put(methodName, tmp);
					}
				}
			}
		}
		return settersAndClass;
	}
}
