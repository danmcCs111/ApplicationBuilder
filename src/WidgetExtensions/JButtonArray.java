package WidgetExtensions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	public static Color []
		backgroundAndForegroundColor = new Color [] {new JButton().getBackground(), new JButton().getForeground()},
		highlightBackgroundAndForegroundColor = new Color [] {backgroundAndForegroundColor[0], backgroundAndForegroundColor[1]};
	private static int indexPos=0;
	private static boolean isHighlight = true;
	private static JButton highlightButton = null;
	private static ArrayList<ArrayList<Component>> collectionJButtons = new ArrayList<ArrayList<Component>>();
	private static ArrayList<String> stripFilter = new ArrayList<String>();
	
	public static final ActionListener highlightActionListener = new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(highlightButton != null)//return and set new selected 
			{
				setHighlightForegroundAndBackground(false);
			}
			highlightButton = (JButton) e.getSource();
			setHighlightForegroundAndBackground(true);
		}
	};
	
	private ActionListener actionListener = null;
	
	public void addJButtons(String path, List<String> listOf, int index)
	{
		ArrayList<Component> jbuts = new ArrayList<Component>();
		JButtonArray.indexPos = index;
		
		clearJButtons();
		
		if(collectionJButtons.size()-1 < index)
		{
			for(Component comp : FileListOptionGenerator.buildComponents(path, listOf, JButton.class))
			{
				if(comp instanceof Component)
				{
					if(comp instanceof AbstractButton)
					{
						String tmpTxt = ((AbstractButton) comp).getText();
						for(String s : stripFilter)
						{
							tmpTxt = tmpTxt.replace(s, "");
						}
						((AbstractButton) comp).setText(tmpTxt);
					}
					comp.setForeground(backgroundAndForegroundColor[1]);
					comp.setBackground(backgroundAndForegroundColor[0]);
					addHighlightButtonActionListener((JButton)comp);
					jbuts.add((JButton) comp);
					this.add(comp);
				}
			}
			addActionListeners(jbuts);
			collectionJButtons.add(jbuts);
		}
		else
		{
			jbuts = collectionJButtons.get(JButtonArray.indexPos);
			for(Component but : jbuts)
			{
				this.add(but);
			}
		}
		
		Container rootCont = getRootPane();
		rootCont.paintComponents(rootCont.getGraphics());
		
		for(ComponentListener cl : rootCont.getParent().getComponentListeners())
		{
			cl.componentResized(new ComponentEvent(rootCont.getParent(), ExtendedFrameResizer.INTERNAL_RESIZE_EVENT));
		}
		rootCont.paintComponents(rootCont.getGraphics());
	}
	
	public static void setHighlight(boolean isHighlight)
	{
		JButtonArray.isHighlight = isHighlight;
	}
	
	public static void setHighlightForegroundColor(Color c)
	{
		JButtonArray.highlightBackgroundAndForegroundColor[1] = c;
	}
	
	public static void setHighlightBackgroundColor(Color c)
	{
		JButtonArray.highlightBackgroundAndForegroundColor[0] = c;
	}
	
	public void addHighlightButtonActionListener(JButton but)
	{
		if(isHighlight)
		{
			but.addActionListener(highlightActionListener);
		}
	}
	
	public void clearJButtons()
	{
		this.removeAll();
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		this.actionListener = actionListener;
		if(collectionJButtons.size()-1 >= indexPos)
		{
			addActionListeners(collectionJButtons.get(indexPos));
		}
	}
	
	private void addActionListeners(ArrayList<Component> jButtons)
	{
		if(this.actionListener != null && !jButtons.isEmpty())
		{
			for(Component button : jButtons)
			{
				if(button instanceof AbstractButton)
				{
					((AbstractButton)button).addActionListener(actionListener);
				}
			}
		}
	}
	
	public void setArrayForeground(Color c)
	{
		setArrayColor(c, 1);
	}
	
	public void setArrayBackground(Color c)
	{
		setArrayColor(c, 0);
	}
	
	private void setArrayColor(Color c, int backgroundOrForeground)
	{
		JButtonArray.backgroundAndForegroundColor[backgroundOrForeground] = c;
		for(List<Component> buts : collectionJButtons)
		{
			for(Component but : buts)
			{
				if(!but.getForeground().equals(JButtonArray.backgroundAndForegroundColor[backgroundOrForeground]))
				{
					but.setForeground(JButtonArray.backgroundAndForegroundColor[backgroundOrForeground]);
				}
			}
		}
	}
	
	public void setArrayForegroundAndBackground(Color cF, Color cB)
	{
		JButtonArray.backgroundAndForegroundColor[1] = cF;
		JButtonArray.backgroundAndForegroundColor[0] = cB;
		for(List<Component> buts : collectionJButtons)
		{
			for(Component but : buts)
			{
				if(!but.getBackground().equals(JButtonArray.backgroundAndForegroundColor[0]))
				{
					but.setBackground(JButtonArray.backgroundAndForegroundColor[0]);
				}
				if(!but.getForeground().equals(JButtonArray.backgroundAndForegroundColor[1]))
				{
					but.setForeground(JButtonArray.backgroundAndForegroundColor[1]);
				}
			}
		}
	}
	
	private static void setHighlightForegroundAndBackground(boolean highlight)
	{
		Color [] color = highlight ? highlightBackgroundAndForegroundColor : backgroundAndForegroundColor;
		highlightButton.setForeground(color[1]);
		highlightButton.setBackground(color[0]);
	}

	@Override
	public void unselect() 
	{
		setHighlightForegroundAndBackground(false);
		highlightButton = null;
	}

	@Override
	public void addStripFilter(String filter) 
	{
		stripFilter.add(filter);
	}

}
