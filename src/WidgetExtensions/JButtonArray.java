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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	public static Color 
		foregroundColor = new JButton().getForeground(),
		backgroundColor = new JButton().getBackground(),
		highlightForegroundColor = foregroundColor,
		highlightBackgroundColor = backgroundColor;
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
				highlightButton.setForeground(foregroundColor);
				highlightButton.setBackground(backgroundColor);
			}
			highlightButton = (JButton) e.getSource();
			highlightButton.setForeground(highlightForegroundColor);
			highlightButton.setBackground(highlightBackgroundColor);
		}
	};
	
	private ActionListener actionListener = null;
	
	public void addJButtons(String path, List<String> listOf, int index)
	{
		clearJButtons();
		
		ArrayList<Component> jbuts = new ArrayList<Component>();
		JButtonArray.indexPos = index;
		
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
					comp.setForeground(foregroundColor);
					comp.setBackground(backgroundColor);
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
		
		Container rootCont = getRootPane().getParent();//redraw window
		rootCont.paintComponents(rootCont.getGraphics());
		for(ComponentListener cl : rootCont.getComponentListeners())
		{
			cl.componentResized(new ComponentEvent(rootCont, ExtendedFrameResizer.INTERNAL_RESIZE_EVENT));
		}
	}
	
	public void setForegroundButtonArray(Color c)
	{
		JButtonArray.foregroundColor = c;
		ArrayList<Component> jButtons = collectionJButtons.get(indexPos);
		
		if(jButtons != null && jButtons.size() > 0)
		{
			for(Component comp : jButtons)
			{
				comp.setForeground(JButtonArray.foregroundColor);
			}
		}
	}
	
	public void setBackgroundButtonArray(Color c)
	{
		JButtonArray.backgroundColor = c;
		ArrayList<Component> jButtons = collectionJButtons.get(indexPos);
		
		if(jButtons != null && jButtons.size() > 0)
		{
			for(Component comp : jButtons)
			{
				comp.setBackground(JButtonArray.backgroundColor);
			}
		}
	}
	
	public static void setHighlight(boolean isHighlight)
	{
		JButtonArray.isHighlight = isHighlight;
	}
	
	public static void setHighlightForegroundColor(Color c)
	{
		JButtonArray.highlightForegroundColor = c;
	}
	
	public static void setHighlightBackgroundColor(Color c)
	{
		JButtonArray.highlightBackgroundColor = c;
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
		JButtonArray.foregroundColor = c;
		for(List<Component> buts : collectionJButtons)
		{
			for(Component but : buts)
			{
				if(!but.getForeground().equals(JButtonArray.foregroundColor))
				{
					but.setForeground(JButtonArray.foregroundColor);
				}
			}
		}
	}
	
	public void setArrayBackground(Color c)
	{
		JButtonArray.backgroundColor = c;
		for(List<Component> buts : collectionJButtons)
		{
			for(Component but : buts)
			{
				if(!but.getBackground().equals(JButtonArray.backgroundColor))
				{
					but.setBackground(JButtonArray.backgroundColor);
				}
			}
		}
	}
	
	public void setArrayForegroundAndBackground(Color cF, Color cB)
	{
		JButtonArray.foregroundColor = cF;
		JButtonArray.backgroundColor = cB;
		for(List<Component> buts : collectionJButtons)
		{
			for(Component but : buts)
			{
				if(!but.getBackground().equals(JButtonArray.backgroundColor))
				{
					but.setBackground(JButtonArray.backgroundColor);
				}
				if(!but.getForeground().equals(JButtonArray.foregroundColor))
				{
					but.setForeground(JButtonArray.foregroundColor);
				}
			}
		}
	}

	@Override
	public void unselect() 
	{
		highlightButton.setForeground(foregroundColor);
		highlightButton.setBackground(backgroundColor);
		highlightButton = null;
	}

	@Override
	public void addStripFilter(String filter) 
	{
		stripFilter.add(filter);
	}

}
