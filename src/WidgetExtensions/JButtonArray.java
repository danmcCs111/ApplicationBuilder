package WidgetExtensions;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private List<JButton> jButtons = new ArrayList<JButton>();
	private HashMap<String, List<String>> collectionNameAndList = new HashMap<String, List<String>>();
	
	
	public JButtonArray()
	{
		this.addHierarchyListener(new HierarchyListener() {
			
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				LoggingMessages.printOut("JBUTTON ARRAY ADDED");
				
			}
		});
		
	}
	
	public void addJButtons(String path, List<String> listOf)
	{
		for(JComponent comp : FileListOptionGenerator.buildComponents(path, listOf, JButton.class))
		{
			if(comp instanceof JButton)
			{
				jButtons.add((JButton) comp);
				this.add(comp);
			}
		}
	}
	
	public void clearJButtons()
	{
		this.removeAll();
	}
	
	public void applyToParentComponent(JComponent parentComponent)
	{
		applyToParentComponent(parentComponent, null);
	}
	public void applyToParentComponent(JComponent parentComponent, Object constraints)
	{
		for(JButton button : jButtons)
		{
			if(constraints != null)
			{
				parentComponent.add(button, constraints);
			}
			else
			{
				parentComponent.add(button);
			}
		}
	}
	
}
