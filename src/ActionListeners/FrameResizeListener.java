package ActionListeners;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import WidgetExtensions.ExtendedFrameResizer;

public class FrameResizeListener extends ComponentAdapter
{
	private ArrayList<Component> allComponents = new ArrayList<Component>();
	private HashMap<Component, String> componentAndText = new HashMap<Component, String>();
	private HashMap<Container, ArrayList<Component>> 
		lastParentAndComponents = new HashMap<Container, ArrayList<Component>>();
	private String 
		compCombineCurrent = "",
		compCombineLast = "";
	
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
		clearComponentsList();//TODO attach to a Frame contents change listner?
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
	
	private boolean isHeightLimited()//TODO mark explicit?
	{
		int heightPandF [] = new int [] {0,0};
		
		if (lastParentAndComponents != null && !lastParentAndComponents.isEmpty())
		{
			compCombineCurrent = FrameResizeListener.combineForComponentsID(lastParentAndComponents.values());
			if(!compCombineCurrent.equals(compCombineLast))
			{
				lastParentAndComponents.clear();
				collectLastParentAndComponents();
			}
			compCombineLast = FrameResizeListener.combineForComponentsID(lastParentAndComponents.values());
		}
		else
		{
			collectLastParentAndComponents();
			if (lastParentAndComponents != null && !lastParentAndComponents.isEmpty())
			{
				compCombineCurrent = FrameResizeListener.combineForComponentsID(lastParentAndComponents.values());
				compCombineLast = compCombineCurrent;
			}
		}
		
		for(Container c2 : lastParentAndComponents.keySet())
		{
			int ind = c2.equals(frame) ? 1 : 0;
			
			for(Component comp : lastParentAndComponents.get(c2))
			{
				if(heightPandF[ind] < comp.getBounds().height)
				{
					heightPandF[ind] = comp.getBounds().height;
				}
			}
		}
		
		return (heightPandF[0] > heightPandF[1]);
	}
	
	private void collectLastParentAndComponents()
	{
		for(Component c : componentAndText.keySet())
		{
			Container lastParent = null;
			ArrayList<Component> comps = null;
			
			if(c.getParent() != null)
			{
				//ignore scroll pane and scroll bar
				if(!(c instanceof JScrollPane) && !(c instanceof JScrollBar))
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
			}
		}
	}
	
	private static String combineForComponentsID(Collection<ArrayList<Component>> out)
	{
		String combine_delimiter = ", ";
		StringBuffer sb = new StringBuffer();
		for(ArrayList<Component> al : out)
		{
			for(Component s : al)
			{
				sb.append(((Component)s).getName() + combine_delimiter);
			}
		}
		return (String) sb.subSequence(0, sb.length() - combine_delimiter.length());
	}
	
	private Container getLastParent(Component c, Container parent, Container root)
	{
		return (parent.getParent() != null && !parent.getParent().equals(root)) 
				? getLastParent(c, parent.getParent(), root)
				: parent;
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
