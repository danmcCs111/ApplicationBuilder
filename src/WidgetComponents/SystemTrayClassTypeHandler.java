package WidgetComponents;

public class SystemTrayClassTypeHandler implements ClassTypeHandler {

	private java.awt.SystemTray systemTray = null;
	
	public SystemTrayClassTypeHandler (Object o)
	{
		this.systemTray = (java.awt.SystemTray) o;
	}
	
	public java.awt.SystemTray getSystemTray()
	{
		return this.systemTray;
	}
}
