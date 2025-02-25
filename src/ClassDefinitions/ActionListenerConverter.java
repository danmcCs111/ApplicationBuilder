package ClassDefinitions;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import Properties.LoggingMessages;
import WidgetComponents.DependentRedrawableFrame;
import WidgetComponents.DependentRedrawableFrameListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ActionListenerConverter implements StringToObjectConverter 
{
	public static ActionListener getActionListener(String arg0)
	{
		ActionListener al = null;
		try {
			Class<?> c = Class.forName(arg0);
			al = (ActionListener) c.getConstructor().newInstance();
			if(al instanceof DependentRedrawableFrameListener)//TODO loading for interface
			{
				List<WidgetCreatorProperty> wcps = WidgetBuildController.getInstance().getWidgetCreatorProperties();
				if(wcps != null && !wcps.isEmpty())
				{
					Object o = wcps.get(0).getInstance();
					if(o instanceof DependentRedrawableFrame)
					{
						DependentRedrawableFrameListener drFrame = (DependentRedrawableFrameListener) al;
						drFrame.setDependentRedrawableFrame((DependentRedrawableFrame) o);
					}
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		LoggingMessages.printOut("ActionListener name: " + al.getClass().toString());
		return al;
	}

	@Override
	public int numberOfArgs() 
	{
		return 1;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
			? getDefaultNullValue()
			: getActionListener(args[0]);
	}
	
	@Override
	public String toString()
	{
		return ActionListenerConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return ActionListener.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}
}
