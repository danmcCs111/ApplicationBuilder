package MouseListenersImpl;

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.DialogParentReferenceContainer;
import WidgetComponents.ComboSelectionDialog;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetExtensions.ComboListDialogSelectedListener;

public class ImageMouseAdapter extends MouseAdapter implements ComboListDialogSelectedListener, DialogParentReferenceContainer
{
	private static final Dimension //TODO
		DIM_PAD = new Dimension(150,0);
	private static final String 
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Save Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Which to Save: ",
		PROPERTIES_FILE_LOCATION = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/data/",
		PROPERTIES_FILE_SAVE_TITLE = "Save Properties",
		PROPERTIES_FILE_SAVE_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = ".txt",
		KEEP_MENU_OPTION_TEXT = "keep",
		KEEP_TITLE = "[Click Image]",
		FILE_ARG_DELIMITER="@";
	
	private static final ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();//The whole app
	private ArrayList<KeepSelection> keepsCurrentCollection = new ArrayList<KeepSelection>();//instance
	private JFrame f;
	private ArrayList<JFrame> frames = new ArrayList<JFrame>();
	private JFrame parentFrame;
	private List<String> saveChosenSelection = null;
	private String saveFilePathChosen = null;
	private String path;
	private boolean keepFrame = false;
		
	public ImageMouseAdapter(JFrame parentFrame, String path)
	{
		this.parentFrame = parentFrame;
		this.path = path;
	}
	
	//required.
	public void setupKeepsSelection(List<Component> components)
	{
		for(Component c : components)
		{
			String text = ((JButtonLengthLimited)c).getFullLengthText();
			keepsCurrentCollection.add(new KeepSelection(this.path, text));
		}
	}
	
	public KeepSelection getAllStoredKeepSelection(Component component)
	{
		String fullText = ((JButtonLengthLimited)component).getFullLengthText();
		return getAllStoredKeepSelection(fullText);
	}
	public KeepSelection getAllStoredKeepSelection(String fullText)
	{
		for(KeepSelection ks : keepsCurrentCollection)//TODO
		{
			if(fullText.equals(ks.getText()))
			{
				return ks;
			}
		}
		return null;
	}
	
	public KeepSelection getKeepSelectionVisible(Component component)
	{
		String fullText = ((JButtonLengthLimited)component).getFullLengthText();
		return getKeepSelectionVisible(fullText);
	}
	public KeepSelection getKeepSelectionVisible(String fullText)
	{
		for(KeepSelection ks : keeps)//TODO
		{
			if(fullText.equals(ks.getText()))
			{
				return ks;
			}
		}
		return null;
	}
	
	public ArrayList<KeepSelection> getKeeps()
	{
		return keeps;
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
	
	public void setupKeepFrame(Component c, int x, int y)
	{
		KeepSelection keep = getKeepSelectionVisible(c);
		if(!keepFrame && !keeps.contains(keep))
		{
			performFrameBuild(c);
			keepFrame = true;
			f.setLocation(x, y);
			createKeepFrame(c);
		}
	}
	
	private void destroyFrame()
	{
		f.setVisible(false);
		f.removeAll();
		f.dispose();
	}

	private void createKeepFrame(Component c)
	{
		KeepSelection keep = getAllStoredKeepSelection(c);
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
						removeSel(keep);
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
		keepFrame = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		Component component = (Component)e.getSource();
		
		LoggingMessages.printOut(e.toString());
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			PopupMenu pm = new PopupMenu();
			MenuItem mi = new MenuItem();
			mi.setLabel(KEEP_MENU_OPTION_TEXT);
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					keepFrame = true;
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
			createKeepFrame((Component)e.getSource());
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		performFrameBuild((Component)e.getSource());
	}
	
	private void performFrameBuild(Component c )
	{
		performFrameBuild(c, "");
	}
	
	private void performFrameBuild(Component component, String title)
	{
		String fullText = ((JButtonLengthLimited)component).getFullLengthText();
		KeepSelection ks = getAllStoredKeepSelection(fullText);
		
		f = new JFrame();
		frames.add(f);
		
		f.setTitle(title);
		f.setUndecorated(true);
		
		Rectangle bounds = component.getBounds();
		Point loc = parentFrame.getLocation();
		
		JLabel l = new JLabel();
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		
		p.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		l.setText(ks.getText());
		p2.add(l, BorderLayout.CENTER);
		p.add(p2, BorderLayout.NORTH);
		
		BufferedImage useImage = null;
		f.setMinimumSize(ks.getSize());
		useImage = ks.getImg();
		ImageIcon ii = new ImageIcon(useImage);
		JLabel picLabel = new JLabel(ii);
		
		if(component instanceof JButtonLengthLimited)//TODO
		{
			JButtonLengthLimited ab = (JButtonLengthLimited) component;
			FrameMouseDragListener mouseDragListener = new FrameMouseDragListener(f, ab, picLabel);
			picLabel.addMouseMotionListener(mouseDragListener);
			picLabel.addMouseListener(mouseDragListener);
			picLabel.setName(ks.getText());
			picLabel.addMouseListener(new PicLabelMouseListener(ab, picLabel));
			if(JButtonArray.isHighlightButton(ab))//TODO add interface.?
			{
				PicLabelMouseListener.highLightLabel(ab, true);
			}
		}
		
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
			csd.buildAndShow(KeepSelection.getTextOnlyConversion(keeps), 
					DIALOG_SELECT_CHILD_COMPONENTS_TITLE,
					DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE,
					ImageMouseAdapter.this, ImageMouseAdapter.this);
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
							ks.getText() + FILE_ARG_DELIMITER+ks.getLocationPoint().x + 
								FILE_ARG_DELIMITER + ks.getLocationPoint().y,
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
		saveChosenSelection.clear();;//reset.
		saveFilePathChosen = "";
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
