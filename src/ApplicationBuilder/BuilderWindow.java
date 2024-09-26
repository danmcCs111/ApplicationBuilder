package ApplicationBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
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
		
		setTitle(WidgetTextProperties.APPLICATION_TITLE.getPropertiesValue());
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
		
		this.setSize(480, 640);
		
		HashMap<String, ArrayList<String>> settersAndClass = generateMethodApiList("set");
		
		for(String methodStr : settersAndClass.keySet())
		{
			LoggingMessages.printOut(methodStr + ": " + settersAndClass.get(methodStr).toString());
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public HashMap<String, ArrayList<String>> generateMethodApiList(String methodPrefixFilter)
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
