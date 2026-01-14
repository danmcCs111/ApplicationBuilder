package ObjectTypeConvertersImpl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import Properties.LoggingMessages;
import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class MouseMotionListenerConverter extends MouseAdapterConverter 
{
	public static MouseAdapter getMouseListener(String arg0)
	{
		MouseMotionListener al = null;
		try {
			Class<?> c = Class.forName(arg0);
			al = (MouseMotionListener) c.getConstructor().newInstance();
			if(al instanceof RedrawableFrameListener)//TODO loading for interface
			{
				List<WidgetCreatorProperty> wcps = WidgetBuildController.getInstance().getWidgetCreatorProperties();
				if(wcps != null && !wcps.isEmpty())
				{
					Object o = wcps.get(0).getInstance();
					if(o instanceof DependentRedrawableFrame)
					{
						RedrawableFrameListener drFrame = (RedrawableFrameListener) al;
						drFrame.setRedrawableFrame((RedrawableFrame) o);
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
		LoggingMessages.printOut("MouseListener name: " + al.getClass().toString());
		return (MouseAdapter) al;
	}
	
	@Override
	public String toString()
	{
		return MouseMotionListenerConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return MouseMotionListener.class;
	}
}
