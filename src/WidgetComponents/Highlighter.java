package WidgetComponents;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class Highlighter
{
	private JComponent 
		comp;
	private Color [] 
			foregroundAndBackgroundColor = new Color [] {new JButton().getForeground(), new JButton().getBackground()},
			highlightForegroundAndBackgroundColor = new Color [] {foregroundAndBackgroundColor[0], foregroundAndBackgroundColor[1]};
	private static Color 
		borderColor = Color.ORANGE;
	private static Border 
		EMPTY_BORDER = BorderFactory.createEmptyBorder(),
		BORDER_HIGHLIGHT;
	
	public Highlighter(
			JComponent comp, Color [] highlightForegroundAndBackgroundColor, Color [] foregroundAndBackgroundColor)
	{
		this.comp = comp;
		this.highlightForegroundAndBackgroundColor = highlightForegroundAndBackgroundColor;
		this.foregroundAndBackgroundColor = foregroundAndBackgroundColor;
	}
	
	public Highlighter(
			JComponent comp, Color [] highlightForegroundAndBackgroundColor)
	{
		this.comp = comp;
		this.highlightForegroundAndBackgroundColor = highlightForegroundAndBackgroundColor;
	}
	
	public Highlighter(
			JComponent comp, Color borderColor)
	{
		this.comp = comp;
		Highlighter.borderColor = borderColor;
	}
	
	public static void setBorderColor(Color color)
	{
		Highlighter.borderColor = color;
		BORDER_HIGHLIGHT = new BevelBorder(BevelBorder.RAISED, borderColor, borderColor);
	}
	
	public static Border getBorderHighlight()
	{
		if(BORDER_HIGHLIGHT == null)
		{
			BORDER_HIGHLIGHT = new BevelBorder(BevelBorder.RAISED, borderColor, borderColor);
		}
		return BORDER_HIGHLIGHT;
	}
	
	public void setHighlightForegroundAndBackground(boolean highlight)
	{
		Color [] color = highlight 
				? highlightForegroundAndBackgroundColor 
				: foregroundAndBackgroundColor;
		if(comp instanceof AbstractButton)
		{
			comp.setForeground(color[0]);
			comp.setBackground(color[1]);
		}
		else if(comp instanceof JPanel)
		{
			JPanel panel = (JPanel) comp;
			panel.setBorder(
					(highlight)
					? getBorderHighlight()
					: EMPTY_BORDER
			);
		}
	}
}
