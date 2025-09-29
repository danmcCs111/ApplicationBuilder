package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationSouthBar extends MouseAdapter
{
	private JLabel pixelMouse;
	private Container parent;
	
	public LocationSouthBar(Container parent)
	{
		this.parent = parent;
		buildWidgets();
	}
	
	@Override
	public void mouseMoved(MouseEvent me)
	{
		pixelMouse.setText(me.getPoint().x + ", " + me.getPoint().y);
	}
	
	public void buildWidgets()
	{
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		
		pixelMouse = new JLabel();
		
		innerPanel.add(pixelMouse, BorderLayout.WEST);
		parent.add(innerPanel, BorderLayout.SOUTH);
	}
}
