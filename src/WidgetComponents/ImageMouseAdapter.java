package WidgetComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class ImageMouseAdapter extends MouseAdapter
{
		private JFrame f;
		private Component component;
		private String text;
		private static final Dimension DIM = new Dimension(350,50);
		
		public ImageMouseAdapter(Component component, String text)
		{
			this.component = component;
			this.text = text;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			f.setVisible(false);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			Point loc = component.getLocation();
			f = new JFrame();
			Rectangle bounds = component.getBounds();
			f.setUndecorated(true);
			Label l = new Label();
			l.setText(text);
			f.setLocation(loc.x+bounds.width, loc.y);
			f.setMinimumSize(DIM);
			f.add(l);
			f.setVisible(true);
		}
}
