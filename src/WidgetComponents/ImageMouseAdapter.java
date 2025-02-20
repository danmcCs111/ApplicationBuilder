package WidgetComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class ImageMouseAdapter extends MouseAdapter
{
		private JFrame f;
		private Component component;
		private BufferedImage img;
		private String text;
		private static final Dimension DIM_PAD = new Dimension(150,0);
		private static final Dimension DIM = new Dimension(350,450);
		
		public ImageMouseAdapter(Component component, String path, String text)
		{
			this.component = component;
			this.text = text;
			String fileLocation = path + toPngFilename();
			File file = new File(fileLocation);
			LoggingMessages.printOut("file location: " + fileLocation);
			try {
				img = ImageIO.read(file);
				
			} catch (IOException e) {
				LoggingMessages.printOut("file not found: " + fileLocation);
			}
		}
		
		private String toPngFilename()
		{
			return this.text + ".png";
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			f.setVisible(false);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			f = new JFrame();
			Rectangle bounds = component.getBounds();
			f.setUndecorated(true);
			JPanel p = new JPanel();
			JLabel l = new JLabel();
			l.setText(text);
			p.add(l);
			if(img != null)
			{
				JLabel picLabel = new JLabel(new ImageIcon(img));
				p.add(picLabel);
			}
			
			f.add(p);
			f.setLocation(bounds.width + DIM_PAD.width, bounds.height + DIM_PAD.height);
			f.setMinimumSize(DIM);
			f.setVisible(true);
		}
}
