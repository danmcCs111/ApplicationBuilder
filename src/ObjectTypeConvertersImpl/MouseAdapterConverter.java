package ObjectTypeConvertersImpl;

import java.awt.event.MouseAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import ObjectTypeConversion.StringToObjectConverter;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class MouseAdapterConverter implements StringToObjectConverter
{
	public static MouseAdapter getMouseListener(String arg0)
	{
		MouseAdapter al = null;
		try {
			Class<?> c = Class.forName(arg0);
			al = (MouseAdapter) c.getConstructor().newInstance();
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
			: getMouseListener(args[0]);
	}
	
	@Override
	public String toString()
	{
		return MouseAdapterConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return MouseAdapter.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[1];
	}
}
