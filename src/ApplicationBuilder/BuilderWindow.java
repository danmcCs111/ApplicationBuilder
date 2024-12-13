package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ActionListeners.ComponentComboBoxActionListener;
import ActionListeners.OpenDetailsActionListener;

public class BuilderWindow extends RedrawableFrame {

	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Application Parameter Editor";
	private static final Dimension WINDOW_LOCATION = new Dimension(250, 250);
	private static final Dimension WINDOW_SIZE = new Dimension(480, 640);
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
	}
	
	private HashMap<String, JList<?>> listOfComponentMethods = new HashMap<String, JList<?>>();
	private JScrollPane scrPane = null;
	private JPanel innerPanel2 = new JPanel();
	private JComboBox<String> classSelection;
	private JList<?> componentMethods = null;
	private JButton openDetails;
	private ArrayList<String> detailsOpenList = new ArrayList<String>();
	
	public BuilderWindow()
	{
		
		setTitle(TITLE);
		setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		//discover a list of methods available to adjust for our available list of components
		HashMap<String, ArrayList<String>> classesAndSetters = generateClassesMethodApiList("set");
		ArrayList<String> tmp = new ArrayList<String>(classesAndSetters.keySet());
		Collections.sort(tmp);
		String [] selections = tmp.toArray(new String [tmp.size()]);
		for(String classStr : classesAndSetters.keySet())
		{
			for(String s : classesAndSetters.get(classStr))
				LoggingMessages.printOut(classStr + ": " + s.toString());
		}
		
		//setup combo box of component classes to build
		classSelection = new JComboBox<String>(selections);
		classSelection.addActionListener(new ComponentComboBoxActionListener(this));
		classSelection.setVisible(true);
		
		//setup methods list from selected drop down component
		for (String c : classesAndSetters.keySet())
		{
			ArrayList<String> methods =	classesAndSetters.get(c);
			
			Collections.sort(methods);//Sort list...
			JList jl = new JList(methods.toArray());
			listOfComponentMethods.put(c, jl);
			jl.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if(!e.getValueIsAdjusting())
					{
						ListSelectionModel lsm = jl.getSelectionModel();
						openDetails.setEnabled(!lsm.isSelectionEmpty() && 
								!detailsOpenList.contains(jl.getSelectedValue()));
					}
				}
			});
		}
		componentMethods = listOfComponentMethods.get(classSelection.getSelectedItem());
		LoggingMessages.printOut("Method output for class: " + classSelection.getSelectedItem().toString());
		BorderLayout bl = new BorderLayout();
		scrPane = new JScrollPane(componentMethods);
		BorderLayout bl2 = new BorderLayout();
		innerPanel2.setLayout(bl2);
		innerPanel2.add(scrPane, BorderLayout.CENTER);
		this.setLayout(bl);
		this.add(innerPanel2, BorderLayout.CENTER);
		
		
		//setup add Property button
		openDetails = new JButton("Add Property");
		this.add(openDetails, BorderLayout.SOUTH);
		openDetails.addActionListener(new OpenDetailsActionListener(openDetails, componentMethods));
		openDetails.setEnabled(false);

		
		this.add(classSelection, BorderLayout.NORTH);
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
	}
	
	@Override
	public void clearInnerPanels()
	{
		innerPanel2.removeAll();
		scrPane.removeAll();
		this.remove(innerPanel2);
	}
	
	@Override
	public void rebuildInnerPanels() {
		componentMethods = listOfComponentMethods.get(classSelection.getSelectedItem());
		scrPane = new JScrollPane(componentMethods);
		innerPanel2.add(scrPane, BorderLayout.CENTER);
		BuilderWindow.this.add(innerPanel2, BorderLayout.CENTER);
		
		LoggingMessages.printOut("Method output for class: " + classSelection.getSelectedItem().toString());
		BuilderWindow.this.paintComponents(BuilderWindow.this.getGraphics());
	}
	
//	public HashMap<String, JList<?>> filterMethods(HashMap<String, JList<?>> classesAndMethods, 
//			ArrayList<String> capableMethodParams)
//	{
//		for(String sc : classesAndMethods.keySet())
//		{
//			ArrayList<String> methods = new ArrayList<String>();
//			JList jl = classesAndMethods.get(sc);
//			LoggingMessages.printOut(jl.getComponentCount() + " is component count");
//		}
//		return classesAndMethods;
//	}
	
//	/*
//	 * TODO replace
//	*/
//	public ArrayList<String> getCapableMethodParams()
//	{
//		ArrayList<String> capableMethodParams = new ArrayList<String>();
//		capableMethodParams.add("boolean");
//		capableMethodParams.add("java.lang.string");
//		capableMethodParams.add("int");
//		capableMethodParams.add("float");
//		capableMethodParams.add("double");
//		capableMethodParams.add("java.awt.color");
//		return capableMethodParams;
//	}
	
	public HashMap<String, ArrayList<String>> generateClassesMethodApiList(String methodPrefixFilter)
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
