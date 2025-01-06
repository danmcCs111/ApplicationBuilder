package WidgetExtensions;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	private List<JButton> jButtons = new ArrayList<JButton>();
	private ActionListener actionListener = null;
	
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
		addActionListeners();
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

	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		this.actionListener = actionListener;
		addActionListeners();
	}
	
	private void addActionListeners()
	{
		if(this.actionListener != null)
		{
			for(JButton button : this.jButtons)
			{
				button.addActionListener(actionListener);
			}
		}
	}
	
}
