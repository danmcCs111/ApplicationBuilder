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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import WidgetComponents.ComboListDialogSelectedListener;
import WidgetComponents.ComboSelectionDialog;
import WidgetComponents.DialogParentReferenceContainer;

public class ImageMouseAdapter extends MouseAdapter implements ComboListDialogSelectedListener, DialogParentReferenceContainer
{
	private static final Dimension //TODO
		DIM_PAD = new Dimension(150,0),
		DIM_DEFAULT_PIC = new Dimension(300,80),
//		DIM_NO_PIC = new Dimension(300,30),
		DIM_PIC = new Dimension(300,470);
	private static final String 
		PROPERTIES_FILE_LOCATION = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/data/",
		PROPERTIES_FILE_SAVE_TITLE = "Save Properties",
		PROPERTIES_FILE_SAVE_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = ".txt",
		KEEP_TITLE = "[Double-Click Image]",
		DEFAULT_IMG = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/launch_xsm.png";//TODO
	
	private JFrame f;
	private Component component;
	private JFrame parentFrame;
	private BufferedImage img,
		defaultImg;
	private String text;
	private boolean keepFrame = false;
	private static final ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();//The whole app
	private KeepSelection keep;
	private List<String> saveChosenSelection = null;
	private String saveFilePathChosen = null;
		
	public ImageMouseAdapter(Component component, JFrame parentFrame, String path, String text) throws IOException
	{
		this.keep = new KeepSelection(path, text);
		this.component = component;
		this.parentFrame = parentFrame;
		this.text = text;
		String fileLocation = path + toPngFilename();
		File file = new File(fileLocation);
		File fileDefault = new File(DEFAULT_IMG);
		try {
			img = ImageIO.read(file);
			
		} catch (IOException e) {
			//TODO ignore. not required.
			defaultImg = ImageIO.read(fileDefault);
		}
	}
	
	protected boolean keepFrame()
	{
		return this.keepFrame;
	}
	
	public void removeSel(KeepSelection ks)
	{
		if(keepFrame == true) keepFrame = false;
		keeps.remove(ks);
	}
	
	public void setupKeepFrame(int x, int y)
	{
		if(!keepFrame && !keeps.contains(keep))
		{
			performFrameBuild();
			keepFrame = true;
			f.setLocation(x, y);
			createKeepFrame();
		}
	}
	
	private String toPngFilename()
	{
		return this.text + ".png";
	}
	
	private void destroyFrame()
	{
		f.setVisible(false);
		f.removeAll();
		f.dispose();
	}

	private void createKeepFrame()
	{
		if(keepFrame)
		{
			if(!keeps.contains(keep))
			{
				keeps.add(keep);
				keep.setFrame(f);
				for(KeepSelection k : keeps) LoggingMessages.printOut(k.toString());
				
				f.dispose();
				f.setUndecorated(false);
				f.addWindowListener(new WindowAdapter() {
					
					@Override
					public void windowClosing(WindowEvent e) {
						if(keepFrame)
						{
							LoggingMessages.printOut("remove. " + keep);
							keeps.remove(keep);
							keepFrame = false;
						}
					}
					
				});
				f.removeMouseListener(ImageMouseAdapter.this);
				f.setTitle(KEEP_TITLE);
				
				f.setVisible(true);
				f.pack();
			}
			else
			{
				destroyFrame();
			}
		}
		else
		{
			destroyFrame();
		}
		
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
			destroyFrame();
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
		performFrameBuild("");
	}
	
	private void performFrameBuild(String title)
	{
		f = new JFrame();
		f.setTitle(title);
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
		
		BufferedImage useImage = null;
		if(img != null)
		{
			f.setMinimumSize(DIM_PIC);
			useImage = img;
		}
		else
		{
			f.setMinimumSize(DIM_DEFAULT_PIC);
			useImage = defaultImg;
		}
		ImageIcon ii = new ImageIcon(useImage);
		JLabel picLabel = new JLabel(ii);
		picLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2)//require double click
				{
					LoggingMessages.printOut(e.getX() + " " + e.getY() + " " + 
					ii.getIconWidth() + " " + ii.getIconHeight() + 
					" " + picLabel.getX() + " " + picLabel.getY());
					
					for(ActionListener al : ((JButton)component).getActionListeners())
					{
						al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
					}
					LoggingMessages.printOut("Image pressed.");
				}
			}
		});
		p.add(picLabel, BorderLayout.CENTER);
		
		f.add(p);
		f.setResizable(false);
		f.setLocation((int)loc.getX() + (bounds.width + DIM_PAD.width), 
				(int)loc.getY() + (bounds.height + DIM_PAD.height));
		f.setVisible(true);
	}
	
	public void writeSave()
	{
		if(keeps.isEmpty())
			return;
		
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f = new File(PROPERTIES_FILE_LOCATION);
		jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_SAVE_TITLE, PROPERTIES_FILE_SAVE_FILTER));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showSaveDialog(parentFrame);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			saveFilePathChosen = chosenFile.getAbsolutePath();
			if(!saveFilePathChosen.endsWith(PROPERTIES_FILE_EXTENSION))
			{
				saveFilePathChosen += PROPERTIES_FILE_EXTENSION;
			}
			ComboSelectionDialog csd = new ComboSelectionDialog();
			csd.buildAndShow(KeepSelection.getTextOnlyConversion(keeps), ImageMouseAdapter.this, ImageMouseAdapter.this);
		}
		
	}
	
	private void performAfterSelectionEventSave()
	{
		if(saveChosenSelection != null)
		{
			int minusCount = 0;
			String [][] properties = new String [saveChosenSelection.size()][2];
			for(int i = 0; i < keeps.size(); i++)
			{
				KeepSelection ks = keeps.get(i);
				if(saveChosenSelection.contains(ks.getText()))//TODO better ID / key system?
				{
					String [] props = new String [] {
							ks.getText()+"@"+ks.getLocationPoint().x+"@"+ks.getLocationPoint().y,
							ks.getPath()
					};
					properties[i+minusCount] = props;
				}
				else
				{
					minusCount--;
				}
			}
			PathUtility.writeProperties(saveFilePathChosen, properties);
		}
		saveChosenSelection = null;//reset.
		saveFilePathChosen = null;
	}
	
	@Override
	public void selectionChosen(List<String> chosenSelection) 
	{
		if(chosenSelection == null) return;
		LoggingMessages.printOut(LoggingMessages.combine(chosenSelection));//print
		saveChosenSelection = (List<String>) chosenSelection;
		performAfterSelectionEventSave();
	}
	
	@Override
	public Point getContainerCenterLocationPoint() 
	{
		return new Point((int)parentFrame.getBounds().getCenterX(), 
				(int)parentFrame.getBounds().getCenterY());
	}
	
	
}
