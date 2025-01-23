package ActionListeners;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import WidgetExtensions.ExtendedFrameResizer;

public class FrameResizeListener extends ComponentAdapter
{
	
	private ArrayList<Component> allComponents = new ArrayList<Component>();
	private HashMap<Component, String> componentAndText = new HashMap<Component, String>();
	
	private JFrame frame;
	private ExtendedFrameResizer resizerListener;
	private boolean heightLimited = false;
	
	public FrameResizeListener(JFrame frame, ExtendedFrameResizer resizerListener)
	{
		this.frame = frame;
		this.resizerListener = resizerListener;
		heightLimited = isHeightLimited();
		
	}
	
	@Override
	public void componentResized(ComponentEvent e) 
	{
		clearComponentsList();
		
		collectAllComponents(frame.getContentPane());
		collectTextWidgets();
		
		boolean tmpH = isHeightLimited();
		
		if(tmpH != heightLimited)
		{
			heightLimited = tmpH;
			resizerListener.getResizeListener().heightLimitEvent(heightLimited);
		}
	}
	
	public void clearComponentsList()
	{
		allComponents.clear();
		componentAndText.clear();
	}

	private void collectAllComponents(Container parent)
	{
		for (Component c : parent.getComponents())
		{
			if(c instanceof Container)
			{
				if(!allComponents.contains(c))
				{
					allComponents.add(c);
				}
				collectAllComponents((Container) c);
			}
			else
			{
				allComponents.add(c);
			}
		}
		return;
	}
	
	private boolean isHeightLimited()
	{
		int 
			heightF = 0,
			heightP = 0;
		
		HashMap<Container, ArrayList<Component>> lastParentAndComponents = new HashMap<Container, ArrayList<Component>>();
				
		for(Component c : componentAndText.keySet())
		{
			Container lastParent = null;
			ArrayList<Component> comps = null;
			
			if(c.getParent() != null)
			{
				lastParent = getLastParent(c, c.getParent(), frame.getContentPane());
				if(lastParentAndComponents.containsKey(lastParent))
				{
					comps = lastParentAndComponents.get(lastParent);
					comps.add(c);
					lastParentAndComponents.replace(lastParent, comps);
				}
				else
				{
					comps = new ArrayList<Component>();
					comps.add(c);
					lastParentAndComponents.put(lastParent, comps);
				}
			}
			for(Container c2 : lastParentAndComponents.keySet())
			{
				if(!c2.equals(frame))
				{
					for(Component comp : lastParentAndComponents.get(c2))
					{
						if(heightP < comp.getBounds().height)
						{
							heightP = comp.getBounds().height;
						}
					}
				}
				else
				{
					for(Component comp : lastParentAndComponents.get(c2))
					{
						if(heightF < comp.getBounds().height)
						{
							heightF = comp.getBounds().height;
						}
					}
				}
			}
		}
		return (heightP > heightF);
	}
	
	private Container getLastParent(Component c, Container parent, Container root)
	{
		if(parent.getParent() != null && !parent.getParent().equals(root))
		{
			return getLastParent(c, parent.getParent(), root);
		}
		else
		{
			return parent;
		}
	}
	
	private void collectTextWidgets()
	{
		for(Component c : allComponents)
		{
			try {
				Method m = c.getClass().getMethod("getText", null);
				if(m != null)
				{
					String text = (String) m.invoke(c, null);
					Container parentKey = c.getParent();
					if(componentAndText.containsKey(parentKey))
					{
						String compareT = componentAndText.get(parentKey);
						if(compareT.length() < text.length())
						{
							componentAndText.replace(parentKey, text);
						}
					}
					else
					{
						componentAndText.put(parentKey, text);
					}
				}
			} catch (NoSuchMethodException e) {
			} catch (SecurityException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}
}
