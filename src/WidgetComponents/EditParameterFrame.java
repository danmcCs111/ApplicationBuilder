package WidgetComponents;

import java.awt.Component;

import javax.swing.JFrame;

public class EditParameterFrame extends JFrame{

	private static final String TITLE_NAME_PREFIX = "Add method: ";
	
	public EditParameterFrame(String methodName)
	{
		setTitle(TITLE_NAME_PREFIX + methodName);
		setLocation(350, 150);
		
		this.setSize(450, 250);
		setVisible(true);
	}
	
	public void addComponent(Component comp)
	{
		this.add(comp);
	}
}
