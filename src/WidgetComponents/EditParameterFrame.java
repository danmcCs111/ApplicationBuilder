package WidgetComponents;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;

public class EditParameterFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final Dimension WINDOW_LOCATION = new Dimension(350, 150);
	private static final Dimension WINDOW_SIZE = new Dimension(650, 550);
	private static final String TITLE_NAME_PREFIX = "Add method: ";
	
	public EditParameterFrame(String methodName)
	{
		setTitle(TITLE_NAME_PREFIX + methodName);
		setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		setVisible(true);
	}
	
	public void addComponent(Component comp)
	{
		this.add(comp);
	}
}
