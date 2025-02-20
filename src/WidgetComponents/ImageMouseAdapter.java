package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Properties.LoggingMessages;

public class ImageMouseAdapter extends MouseAdapter
{
	private static final Dimension 
		DIM_PAD = new Dimension(150,0),
		DIM_NO_PIC = new Dimension(350,50),
		DIM_PIC = new Dimension(350,450);
	
	private JFrame f;
	private Component component;
	private JFrame parentFrame;
	private BufferedImage img;
	private String text;
	private boolean keepFrame = false;
		
	public ImageMouseAdapter(Component component, JFrame parentFrame, String path, String text)
	{
		this.component = component;
		this.parentFrame = parentFrame;
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
	public void mouseClicked(MouseEvent e) 
	{
		LoggingMessages.printOut(e.toString());
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			PopupMenu pm = new PopupMenu();
			MenuItem mi = new MenuItem();
			mi.setLabel("keep");
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					keepFrame = true;
					LoggingMessages.printOut("Keep.");
				}
			});
			pm.add(mi);
			component.add(pm);
			pm.show(component, component.getBounds().width/2, 0);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) 
	{
		if(!keepFrame)
		{
			f.setVisible(false);
			f.removeAll();
			f.dispose();
		}
		else
		{
			f.dispose();
			f.setUndecorated(false);
			f.removeMouseListener(this);
			keepFrame = false;
			
			f.setVisible(true);
			f.pack();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		f = new JFrame();
		f.setUndecorated(true);
		
		Rectangle bounds = component.getBounds();
		Point loc = parentFrame.getLocation();
		
		JLabel l = new JLabel();
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		
		p.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		l.setText(text);
		p2.add(l, BorderLayout.CENTER);
		p.add(p2, BorderLayout.NORTH);
		
		if(img != null)
		{
			JLabel picLabel = new JLabel(new ImageIcon(img));
			picLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for(ActionListener al : ((JButton)component).getActionListeners())
					{
						al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
					}
					LoggingMessages.printOut("button pressed.");
				}
			});
			p.add(picLabel, BorderLayout.CENTER);
			f.setMinimumSize(DIM_PIC);
		}
		else
		{
			f.setMinimumSize(DIM_NO_PIC);
		}
		
		f.add(p);
		f.setLocation((int)loc.getX() + (bounds.width + DIM_PAD.width), 
				(int)loc.getY() + (bounds.height + DIM_PAD.height));
		f.setVisible(true);
	}
		
}
