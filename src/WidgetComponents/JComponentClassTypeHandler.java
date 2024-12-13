package WidgetComponents;

import javax.swing.JComponent;

public class JComponentClassTypeHandler implements ClassTypeHandler {

	private JComponent jComponent = null;
	
	public  JComponentClassTypeHandler(Object o)
	{
		this.jComponent = (JComponent) o;
	}
	
	public JComponent getJComponent()
	{
		return this.jComponent;
	}
}
