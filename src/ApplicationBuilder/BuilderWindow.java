package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ActionListeners.ComponentComboBoxActionListener;
import ActionListeners.OpenDetailsActionListener;
import ClassDefintions.ClassAndSetters;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.XmlToEditor;

public class BuilderWindow extends RedrawableFrame 
{
	private static final long serialVersionUID = 1886L;
	private static final String TITLE = "Application Parameter Editor";
	private static final Dimension 
		WINDOW_LOCATION = new Dimension(100, 50),
		WINDOW_SIZE = new Dimension(480, 640);
	
	private HashMap<String, JList<?>> listOfComponentMethods = new HashMap<String, JList<?>>();
	private JScrollPane scrPane = null;
	private JPanel innerPanel2 = new JPanel();
	private JComboBox<String> classSelection;
	private JList<?> componentMethods = null;
	private JButton openDetails;
	private ArrayList<String> detailsOpenList = new ArrayList<String>();
	private OpenDetailsActionListener openDetailsActionListener;
	
	public BuilderWindow()
	{
		setTitle(TITLE);
		setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		
		ArrayList<ClassAndSetters> classAndSetters = WidgetAttributes.getClassAndSetters();
		List<String> comboKeySetClasses = new ArrayList<String>();
		HashMap<String, ArrayList<String>> comboKeyAndSetClasses = new HashMap<String, ArrayList<String>>();
		
		for(ClassAndSetters cs : classAndSetters)
		{
			String comboClassStr = cs.getClazz().toString();
			comboKeySetClasses.add(comboClassStr);
			LoggingMessages.printOut("class string: " + comboClassStr);
			comboKeyAndSetClasses.put(comboClassStr, cs.getSupportedSetters());
		}
		
		Collections.sort(comboKeySetClasses);
		
		//setup combo box of component classes to build
		classSelection = new JComboBox<String>(comboKeySetClasses.toArray(new String[]{}));
		classSelection.addActionListener(new ComponentComboBoxActionListener(this));
		
		classSelection.setVisible(true);
		
		//setup methods list from selected drop down component
		for (String c : comboKeySetClasses)
		{
			ArrayList<String> methods =	comboKeyAndSetClasses.get(c);
			
			Collections.sort(methods);//Sort list...
			JList<String> jl = new JList<String>(methods.toArray(new String[] {}));
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
		openDetailsActionListener = new OpenDetailsActionListener(openDetails);
		openDetails.addActionListener(openDetailsActionListener);
		openDetailsActionListener.setComponentMethods(componentMethods);
		openDetails.setEnabled(false);

		
		this.add(classSelection, BorderLayout.NORTH);
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
	}
	
	public void setComboSelection(String compSelect)
	{
		for (int i = 0; i < classSelection.getItemCount(); i++)
		{
			String sel = classSelection.getItemAt(i);
			String compStr = compSelect.replaceFirst(XmlToEditor.COMPONENT_REGEX, "");
			LoggingMessages.printOut(compStr + " " + sel);
			if(sel.endsWith(compStr))
			{
				classSelection.setSelectedIndex(i);
				return;
			}
		}
	}
	
	@Override
	public void clearInnerPanels()
	{
		innerPanel2.removeAll();
		scrPane.removeAll();
		this.remove(innerPanel2);
	}
	
	@Override
	public void rebuildInnerPanels() 
	{
		componentMethods = listOfComponentMethods.get(classSelection.getSelectedItem());
		openDetailsActionListener.setComponentMethods(componentMethods);
		scrPane = new JScrollPane(componentMethods);
		innerPanel2.add(scrPane, BorderLayout.CENTER);innerPanel2.setOpaque(rootPaneCheckingEnabled);
		BuilderWindow.this.add(innerPanel2, BorderLayout.CENTER);
		
		LoggingMessages.printOut("Method output for class: " + classSelection.getSelectedItem().toString());
		BuilderWindow.this.paintComponents(BuilderWindow.this.getGraphics());
	}
	
}
