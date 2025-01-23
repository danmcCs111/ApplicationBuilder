package ActionListeners;

import java.awt.Component;

import ApplicationBuilder.WidgetBuildController;

public interface ActionListenerSubTypeExtension 
{
	public abstract void setActionListenerSubTypeExtension(Class<?> clazz, String type);
	public abstract void setWidgetBuildController(WidgetBuildController widgetBuildController);
	public abstract void setConnectedComp(Component comp);
	
}
