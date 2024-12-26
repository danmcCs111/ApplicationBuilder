package WidgetExtensions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	private List<JButton> jButtons = new ArrayList<JButton>();
	private String 
		path,
		filter;
	
	
	public void setPathAndFilter(String path, String filter)
	{
		this.path = path;
		this.filter = filter;
	}
	
	public void addJButtons()
	{
		for(JComponent comp : FileListOptionGenerator.getComponents(path, filter, JButton.class))
		{
			if(comp instanceof JButton)
			{
				jButtons.add((JButton) comp);
			}
		}
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
