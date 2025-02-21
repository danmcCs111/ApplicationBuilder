package ActionListeners;

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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class ImageMouseAdapter extends MouseAdapter
{
	private static final Dimension 
		DIM_PAD = new Dimension(150,0),
		DIM_NO_PIC = new Dimension(300,50),
		DIM_PIC = new Dimension(300,470);
	private static final String 
		PROPERTIES_FILE_LOCATION = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/data/",
		PROPERTIES_FILE_SAVE_TITLE = "Save Properties",
		PROPERTIES_FILE_SAVE_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = "\\.txt",
		KEEP_TITLE = "[Click Image]";
	
	private JFrame f;
	private Component component;
	private JFrame parentFrame;
	private BufferedImage img;
	private String text;
	private boolean keepFrame = false;
	private static final ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();//The whole app
	private KeepSelection keep;
		
	public ImageMouseAdapter(Component component, JFrame parentFrame, String path, String text)
	{
		this.keep = new KeepSelection(path, text);
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
	
	protected boolean keepFrame()
	{
		return this.keepFrame;
	}
	
	public void setupKeepFrame()
	{
		LoggingMessages.printOut("setup keep frame");
		performFrameBuild();
		keepFrame = true;
		createKeepFrame();
	}
	
	private String toPngFilename()
	{
		return this.text + ".png";
	}

	private void createKeepFrame()
	{
		if(!keeps.contains(keep))
		{
			keeps.add(keep);
			for(KeepSelection k : keeps) LoggingMessages.printOut(k.toString());
		}
		
		f.dispose();
		f.setUndecorated(false);
		f.removeMouseListener(ImageMouseAdapter.this);
		keepFrame = false;
		f.setTitle(KEEP_TITLE);
		
		f.setVisible(true);
		f.pack();
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
			createKeepFrame();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		performFrameBuild();
	}
	
	
	private void performFrameBuild()
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
			ImageIcon ii = new ImageIcon(img);
			JLabel picLabel = new JLabel(ii);
			picLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					LoggingMessages.printOut(e.getX() + " " + e.getY() + " " + 
					ii.getIconWidth() + " " + ii.getIconHeight() + 
					" " + picLabel.getX() + " " + picLabel.getY());
					
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
	
	public void writeSave()
	{
		if(keeps.isEmpty())
			return;
		
		String [][] properties = new String [keeps.size()][2];
		for(int i = 0; i < keeps.size(); i++)
		{
			KeepSelection ks = keeps.get(i);
			String [] props = new String [] {
					ks.getText(),
					ks.getPath()
			};
			properties[i] = props;
		}
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f = new File(PROPERTIES_FILE_LOCATION);
		jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_SAVE_TITLE, PROPERTIES_FILE_SAVE_FILTER));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showSaveDialog(parentFrame);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			String path = chosenFile.getAbsolutePath();
			if(!path.endsWith(PROPERTIES_FILE_EXTENSION))
			{
				path += PROPERTIES_FILE_EXTENSION;
			}
			PathUtility.writeProperties(chosenFile.getAbsolutePath(), properties);
		}
	}
	
	
}
