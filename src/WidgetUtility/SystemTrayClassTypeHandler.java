package WidgetUtility;

public class SystemTrayClassTypeHandler implements ClassTypeHandler 
{
	private java.awt.SystemTray systemTray = null;
	
	public SystemTrayClassTypeHandler (Object o)
	{
		this.systemTray = (java.awt.SystemTray) o;
	}
	
	public java.awt.SystemTray getSystemTray()
	{
		return this.systemTray;
	}

	@Override
	public void applyAttribute(String method, String... params) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getClassType() 
	{
		return this.getSystemTray().getClass();
	}

	@Override
	public boolean isContainer() 
	{
		return false;
	}
}
