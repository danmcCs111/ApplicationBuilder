package WidgetComponents;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class ShapeCreatorEditPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel titleLabel = new JLabel("Edit");
	
	public ShapeCreatorEditPanel()
	{
		this.add(titleLabel);
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		this.setBorder(b);
	}
}
