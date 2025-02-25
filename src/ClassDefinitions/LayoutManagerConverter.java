package ClassDefinitions;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;

public class LayoutManagerConverter implements StringToObjectConverter 
{
	private static final Class<?> [] layoutManagers = new Class<?> [] {
			BorderLayout.class,
			GridLayout.class,
			FlowLayout.class
	};
	public static LayoutManager getLayoutManager(String arg0)
	{
		for(Class<?> c : layoutManagers)
		{
			if(c.getName().contains(arg0))
			{
				try {
					if(c.getName().contains("GridLayout"))//TODO
					{
						return (LayoutManager) c.getDeclaredConstructor(int.class, int.class).newInstance(0,1);
					}
					else
					{
						return (LayoutManager) c.getDeclaredConstructor().newInstance();
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		return null;
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
				: getLayoutManager(args[0]);
	}
	
	@Override
	public String toString()
	{
		return LayoutManagerConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return LayoutManager.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return args[0];
	}
}
