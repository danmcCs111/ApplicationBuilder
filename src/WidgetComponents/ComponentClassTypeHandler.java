package WidgetComponents;

import java.awt.Component;

public class ComponentClassTypeHandler implements ClassTypeHandler{
	
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
	public void applyAttribute(String method, String... params) {
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getClassType() {
		return this.getComponent().getClass();
	}
}
