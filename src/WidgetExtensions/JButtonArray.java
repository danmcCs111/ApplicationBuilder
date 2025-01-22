package WidgetExtensions;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 */
public class JButtonArray extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static Color 
		foregroundColor = new JButton().getForeground(),
		backgroundColor = new JButton().getBackground(),
		highlightForegroundColor = foregroundColor,
		highlightBackgroundColor = backgroundColor;
	private static int indexPos=0;
	private static boolean isHighlight = true;
	private static JButton highlightButton = null;
	private static final ActionListener highlightActionListener = new ActionListener() 
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
	
	private ArrayList<ArrayList<JButton>> collectionJButtons = new ArrayList<ArrayList<JButton>>();
	private ActionListener actionListener = null;
	
	public void addJButtons(String path, List<String> listOf, int index)
	{
		clearJButtons();
		
		ArrayList<JButton> jbuts = new ArrayList<JButton>();
		JButtonArray.indexPos = index;
		
		if(collectionJButtons.size()-1 < index)
		{
			for(JComponent comp : FileListOptionGenerator.buildComponents(path, listOf, JButton.class))
			{
				if(comp instanceof JButton)
				{
					comp.setForeground(foregroundColor);
					comp.setBackground(backgroundColor);
					addHighlightButtonActionListener((JButton)comp);
					jbuts.add((JButton) comp);
					this.add(comp);
				}
			}
			collectionJButtons.add(jbuts);
		}
		else
		{
			jbuts = collectionJButtons.get(JButtonArray.indexPos);
			for(JButton but : jbuts)
			{
				this.add(but);
			}
		}
		
		addActionListeners(jbuts);
		
		Container rootCont = getRootPane().getParent();//redraw window
		rootCont.paintComponents(rootCont.getGraphics());
	}
	
	public void setForegroundButtonArray(Color c)
	{
		JButtonArray.foregroundColor = c;
		ArrayList<JButton> jButtons = collectionJButtons.get(indexPos);
		
		if(jButtons != null && jButtons.size() > 0)
		{
			for(JButton comp : jButtons)
			{
				comp.setForeground(JButtonArray.foregroundColor);
			}
		}
	}
	
	public void setBackgroundButtonArray(Color c)
	{
		JButtonArray.backgroundColor = c;
		ArrayList<JButton> jButtons = collectionJButtons.get(indexPos);
		
		if(jButtons != null && jButtons.size() > 0)
		{
			for(JButton comp : jButtons)
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
	
	public void applyToParentComponent(JComponent parentComponent)
	{
		applyToParentComponent(parentComponent, null);
	}
	public void applyToParentComponent(JComponent parentComponent, Object constraints)
	{
		for(JButton button : collectionJButtons.get(indexPos))
		{
			if(constraints != null)
			{
				parentComponent.add(button, constraints);
			}
			else
			{
				parentComponent.add(button);
			}
		}
	}

	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		this.actionListener = actionListener;
		addActionListeners(collectionJButtons.get(indexPos));
	}
	
	private void addActionListeners(ArrayList<JButton> jButtons)
	{
		if(this.actionListener != null)
		{
			for(JButton button : jButtons)
			{
				button.addActionListener(actionListener);
			}
		}
	}
	
}
