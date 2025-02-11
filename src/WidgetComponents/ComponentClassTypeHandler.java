package WidgetComponents;

import java.awt.Component;
import java.awt.Container;

public class ComponentClassTypeHandler implements ClassTypeHandler
{
	private Component component = null;
	
	public ComponentClassTypeHandler (Object o)
	{
		this.component = (Component) o;
	}
	
	public Component getComponent()
	{
		return this.component;
	}

	@Override
	public void applyAttribute(String method, String... params) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getClassType() 
	{
		return this.getComponent().getClass();
	}

	@Override
	public boolean isContainer() 
	{
		return (this.component instanceof Container);
	}
}
