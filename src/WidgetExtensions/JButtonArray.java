package WidgetExtensions;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private List<JButton> jButtons = new ArrayList<JButton>();
	private HashMap<String, List<String>> collectionNameAndList = new HashMap<String, List<String>>();
	
	/**
	 * SwappableCollection is parent so built before JButtonArray
	 */
	public JButtonArray()
	{
		Container parent = this.getParent();
		if(parent instanceof SwappableCollection)
		{
			SwappableCollection sc = (SwappableCollection) parent;
			this.collectionNameAndList = sc.getCollectionNameAndList();
		}
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
