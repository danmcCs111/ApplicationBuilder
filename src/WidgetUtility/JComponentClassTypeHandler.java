package WidgetUtility;

import java.awt.Container;

import javax.swing.JComponent;

public class JComponentClassTypeHandler implements ClassTypeHandler 
{
	private JComponent jComponent = null;
	
	public  JComponentClassTypeHandler(Object o)
	{
		this.jComponent = (JComponent) o;
	}
	
	public JComponent getJComponent()
	{
		return this.jComponent;
	}

	@Override
	public void applyAttribute(String method, String... params) 
	{
		// TODO Auto-generated method stub
	}
	
	public boolean isContainer()
	{
		return (this.jComponent instanceof Container);
	}

	@Override
	public Class<?> getClassType() 
	{
		return this.getJComponent().getClass();
	}
}
