package Graphics2D;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import WidgetComponents.JCheckBoxLimited;

public class ColorTemplate 
{
	private static Color 
		panelBackgroundColor = null,
		deleteBackgroundColor = null,
		deleteForegroundColor = Color.red,
		buttonBackgroundColor = null,
		buttonForegroundColor = null;
	
	public static void setButtonForegroundColor(Color c)
	{
		buttonForegroundColor = c;
	}
	public static void setButtonBackgroundColor(Color c)
	{
		buttonBackgroundColor = c;
	}
	public static void setDeleteForegroundColor(Color c)
	{
		deleteForegroundColor = c;
	}
	public static void setDeleteBackgroundColor(Color c)
	{
		deleteBackgroundColor = c;
	}
	public static void setPanelBackgroundColor(Color c)
	{
		panelBackgroundColor = c;
	}
	
	public static Color getButtonForegroundColor()
	{
		return buttonForegroundColor;
	}
	public static Color getButtonBackgroundColor()
	{
		return buttonBackgroundColor;
	}
	public static Color getDeleteForegroundColor()
	{
		return deleteForegroundColor;
	}
	public static Color getDeleteBackgroundColor()
	{
		return deleteBackgroundColor;
	}
	public static Color getPanelBackgroundColor()
	{
		return panelBackgroundColor;
	}
	
	public static void setBackgroundColorPanel(Container container, Color c) 
	{
		if(c == null)
		{
			return;
		}
		
		if (container instanceof JPanel) 
        {
        	JPanel pan = (JPanel) container;
        	pan.setBackground(c);
        } 
		else if (container instanceof JTextField) 
        {
			JTextField tf = (JTextField) container;
         	tf.setBackground(c);
        } 
		else if (container instanceof JTextArea) 
        {
			JTextArea ta = (JTextArea) container;
			ta.setBackground(c);
        } 
		else if (container instanceof JCheckBox) 
        {
        	JCheckBox cb = (JCheckBox) container;
        	cb.setBackground(c);
        } 
		else if (container instanceof JLabel) 
        {
			JLabel jl = (JLabel) container;
         	jl.setBackground(c);
        } 
		else if (container instanceof JList) 
        {
			JList<?> jl = (JList<?>) container;
        	jl.setBackground(c);
        }
		else if (container instanceof JSlider) 
        {
			JSlider js = (JSlider) container;
        	js.setBackground(c);
        } 
		else if(container instanceof JCheckBoxLimited)
		{
			JCheckBoxLimited jc = (JCheckBoxLimited) container;
        	jc.setBackground(c);
		}
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JPanel) 
            {
            	JPanel pan = (JPanel) component;
            	pan.setBackground(c);
            } 
            else if (container instanceof JTextField) 
            {
    			JTextField tf = (JTextField) container;
             	tf.setBackground(c);
            } 
    		else if (container instanceof JCheckBox) 
            {
            	JCheckBox cb = (JCheckBox) container;
            	cb.setBackground(c);
            } 
    		else if (container instanceof JList) 
            {
    			JList<?> jl = (JList<?>) container;
            	jl.setBackground(c);
            }
    		else if (container instanceof JSlider) 
            {
    			JSlider js = (JSlider) container;
            	js.setBackground(c);
            } 
    		else if(container instanceof JCheckBoxLimited)
    		{
    			JCheckBoxLimited jc = (JCheckBoxLimited) container;
            	jc.setBackground(c);
    		}
            if (component instanceof Container) 
            {
            	setBackgroundColorPanel((Container) component, c);
            }
        }
    }
	public static void setBackgroundColorButtons(Container container, Color c) 
	{
		if(c == null)
		{
			return;
		}
		
		if (container instanceof JButton ) 
        {
			JButton ab = (JButton) container;
        	ab.setBackground(c);
        } 
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JButton) 
            {
            	AbstractButton ab = (JButton) component;
            	ab.setBackground(c);
            } 
            else if (component instanceof Container) 
            {
            	setBackgroundColorButtons((Container) component, c);
            }
        }
    }
	public static void setBackgroundColorButtonsForced(Container container, Color c)
	{
		
	}
	public static void setForegroundColorButtons(Container container, Color c) 
	{
		if(c == null)
		{
			return;
		}
		
		if (container instanceof JButton) 
        {
			JButton ab = (JButton) container;
        	ab.setForeground(c);
        } 
		else if (container instanceof JTextField) 
        {
			JTextField tf = (JTextField) container;
			tf.setCaretColor(c);
         	tf.setForeground(c);
        } 
		else if (container instanceof JTextArea) 
        {
			JTextArea ta = (JTextArea) container;
			ta.setCaretColor(c);
			ta.setForeground(c);
        } 
		else if (container instanceof JLabel) 
        {
			JLabel jl = (JLabel) container;
         	jl.setForeground(c);
        } 
		else if (container instanceof JCheckBox) 
        {
        	JCheckBox cb = (JCheckBox) container;
        	cb.setForeground(c);
        } 
		else if (container instanceof JList) 
        {
			JList<?> jl = (JList<?>) container;
        	jl.setForeground(c);
        }
		else if (container instanceof JSlider) 
        {
			JSlider js = (JSlider) container;
        	js.setForeground(c);
        } 
		else if(container instanceof JCheckBoxLimited)
		{
			JCheckBoxLimited jc = (JCheckBoxLimited) container;
        	jc.setForeground(c);
		}
        for (Component component : container.getComponents()) 
        {
            if (component instanceof JButton) 
            {
            	JButton ab = (JButton) component;
            	ab.setForeground(c);
            } 
            else if (component instanceof JTextField) 
            {
            	JTextField tf = (JTextField) component;
            	tf.setCaretColor(c);
            	tf.setForeground(c);
            } 
            else if (container instanceof JTextArea) 
            {
    			JTextArea ta = (JTextArea) container;
    			ta.setCaretColor(c);
    			ta.setForeground(c);
            } 
            else if (component instanceof JCheckBox) 
            {
            	JCheckBox cb = (JCheckBox) component;
            	cb.setForeground(c);
            } 
            else if (container instanceof JList) 
            {
    			JList<?> jl = (JList<?>) container;
            	jl.setForeground(c);
            }
    		else if (container instanceof JSlider) 
            {
    			JSlider js = (JSlider) container;
            	js.setForeground(c);
            } 
    		else if(container instanceof JCheckBoxLimited)
    		{
    			JCheckBoxLimited jc = (JCheckBoxLimited) container;
            	jc.setForeground(c);
    		}
            else if (component instanceof Container) 
            {
            	setForegroundColorButtons((Container) component, c); 
            }
        }
    }
}
