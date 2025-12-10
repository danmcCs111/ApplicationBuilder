package MouseListenersImpl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentDialogs.ComboSelectionDialog;
import WidgetComponentDialogs.VideoBookMarksDialog;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.OpenAndSaveKeepsSubscriber;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.ComboListDialogSelectedListener;
import WidgetUtility.ComponentUtility;
import WidgetUtility.WidgetBuildController;

public class ImageMouseAdapter extends MouseAdapter implements ComboListDialogSelectedListener
{
	private static final String 
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Save Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Which to Save: ",
		BOOKMARKS_FILE_RELATIVE_LOCATION = "./Properties/VideoLaunchBookmarks/ ",
		PROPERTIES_FILE_SAVE_TITLE = "Save Properties",
		PROPERTIES_FILE_SAVE_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = ".txt",
		KEEP_MENU_OPTION_TEXT = "keep",
//		KEEP_TITLE = "[Click Image]",
		FILE_ARG_DELIMITER="@";
	private static boolean
		SHOW_JAVA_SWING_FILE_CHOOSER = false,
		SHOW_TITLE_ON_POSTER = true,
		SHOW_PREVIEW = true;
	
	private static final ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();//The whole app
	private ArrayList<KeepSelection> keepsCurrentCollection = new ArrayList<KeepSelection>();//instance
	private KeepSelection previewKeep;
	private JFrame f;
	private ArrayList<JFrame> frames = new ArrayList<JFrame>();
	private JFrame parentFrame;
	private List<String> saveChosenSelection = null;
	private static String saveFilePathChosen = null;
	private String path;
	private boolean 
		keepFrame = false,
		singleClick = false;
	private ButtonArray ba;
		
	public ImageMouseAdapter(JFrame parentFrame, String path, boolean singleClick)
	{
		this.parentFrame = parentFrame;
		this.path = path;
		this.singleClick = singleClick;
	}
	
	private ButtonArray getButtonArray()
	{
		if(ba == null)
		{
			ba = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);
		}
		return ba;
	}
	
	public static void setShowPreview(boolean showPreview)
	{
		SHOW_PREVIEW = showPreview;
	}
	
	public static void setJavaSwingFileChooser(boolean isSwingFileChooser)
	{
		SHOW_JAVA_SWING_FILE_CHOOSER = isSwingFileChooser;
	}
	
	public static void setShowTitleOnPoster(boolean showTitle)
	{
		SHOW_TITLE_ON_POSTER = showTitle;
	}
	
	
	public void setSingleClick(boolean singleClick)
	{
		this.singleClick = singleClick;
	}
	
	public void setupKeepsSelection(List<JButtonLengthLimited> components)
	{
		for(JButtonLengthLimited c : components)
		{
			String text = c.getFullLengthText();
			keepsCurrentCollection.add(new KeepSelection(this.path, text, c.getText(), getButtonArray(), c));
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
				f.setMinimumSize(keep.getSize());
				JPanel p = keep.getConnectedPanel();
				Component comp = ComponentUtility.findComponentByName(p, keep.getText());
				p.remove(comp);
				p.add(buildPicLabel(new ImageIcon(keep.getImg()), keep), BorderLayout.CENTER);
				f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						removeSel(keep);
					}
					
				});
				f.setIconImage(GraphicsUtil.getImageFromFile(JButtonArray.MOVIE_IMAGE_FILE_LOCATION));//TODO
				f.removeMouseListener(ImageMouseAdapter.this);
//				f.setTitle(KEEP_TITLE);
				
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
		JComponent component = (JComponent)e.getSource();
		LoggingMessages.printOut(e.toString());
		
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			JPopupMenu pm = new JPopupMenu();
			pm.setLocation(e.getLocationOnScreen());
			JMenuItem mi = new JMenuItem();
			mi.setText(KEEP_MENU_OPTION_TEXT);
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
	
	private void performFrameBuild(Component c)
	{
		performFrameBuild(c, "");
	}
	
	private void performFrameBuild(Component component, String title)
	{
		String fullText = ((JButtonLengthLimited)component).getFullLengthText();
		LoggingMessages.printOut(fullText);
		KeepSelection ks = getAllStoredKeepSelection(fullText);
		if(f != null && !keeps.contains(previewKeep))
		{
			f.dispose();
			previewKeep.destroyImages();//nop. unknown.
		}
		previewKeep = ks;
		f = new JFrame();
		frames.add(f);
		
		f.setTitle(title);
		f.setUndecorated(true);
		
		JLabel l = new JLabel();
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		
		p.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		if(SHOW_TITLE_ON_POSTER || KeepSelection.isDefaultImg(ks.getImg()))
		{
			l.setText(ks.getDisplayText());
		}
		p2.add(l, BorderLayout.CENTER);
		p.add(p2, BorderLayout.NORTH);
		
		Image useImage = null;
		f.setMinimumSize(ks.getSizePreview());
		
		if(SHOW_PREVIEW)
		{
			useImage = ks.getPreviewImage();
		}
		JLabel picLabel = buildPicLabel(useImage==null ?null :new ImageIcon(useImage), ks);
		ks.setConnectedPanel(p);
		p.add(picLabel, BorderLayout.CENTER);
		
		f.add(p);
		f.setResizable(false);
		if(SHOW_PREVIEW &&!KeepSelection.isDefaultImg(useImage))//hide default image. only during keeping.
		{
			f.setVisible(true);
		}
		
		GraphicsUtil.rightEdgeCenterWindow(WidgetBuildController.getInstance().getFrame(), f);
	}
	
	private JLabel buildPicLabel(ImageIcon ii, KeepSelection ks)
	{
		JLabel picLabel = new JLabel(ii);
		JButtonLengthLimited ab = ks.getJButtonLengthLimited();
		FrameMouseDragListener mouseDragListener = new FrameMouseDragListener(f, ab, picLabel);
		picLabel.addMouseMotionListener(mouseDragListener);
		picLabel.addMouseListener(mouseDragListener);
		picLabel.setName(ks.getText());
		picLabel.setToolTipText(ks.getText());
		picLabel.addMouseListener(new PicLabelMouseListener(ab, picLabel, singleClick));
		if(ba.isHighlightButton(ab))
		{
			PicLabelMouseListener.highLightLabel(ab, true);
		}
		return picLabel;
	}
	
	public void writeSave()
	{
		if(keeps.isEmpty())
			return;
		
		saveDialog();
	}
	
	public void setSaveFile(String saveFilePath)
	{
		LoggingMessages.printOut("Save file: " + saveFilePath);
		saveFilePathChosen = saveFilePath;
	}
	
	private void saveDialog()
	{
		if(!SHOW_JAVA_SWING_FILE_CHOOSER)
		{
			DirectorySelection ds = new DirectorySelection(BOOKMARKS_FILE_RELATIVE_LOCATION);
			new VideoBookMarksDialog(ds, 
					(OpenAndSaveKeepsSubscriber)ba,
					WidgetBuildController.getInstance().getFrame(),
					getProperties());
		}
		else
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			DirectorySelection ds = new DirectorySelection(BOOKMARKS_FILE_RELATIVE_LOCATION);
			File f = new File(ds.getFullPath());
			jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_SAVE_TITLE, PROPERTIES_FILE_SAVE_FILTER));
			jfc.setSelectedFile(f);
			GraphicsUtil.rightEdgeTopWindow(parentFrame, jfc);
			int choice = jfc.showSaveDialog(parentFrame);
			File chosenFile = jfc.getSelectedFile();
			if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
			{
				saveFilePathChosen = chosenFile.getAbsolutePath();
				if(!saveFilePathChosen.endsWith(PROPERTIES_FILE_EXTENSION))
				{
					saveFilePathChosen += PROPERTIES_FILE_EXTENSION;
				}
				getSaveSelections();
			}
		}
	}
	
	public void getSaveSelections()
	{
		ComboSelectionDialog csd = new ComboSelectionDialog();
		csd.buildAndShow(KeepSelection.getTextOnlyConversion(keeps), 
				DIALOG_SELECT_CHILD_COMPONENTS_TITLE,
				DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE,
				ImageMouseAdapter.this, parentFrame);
	}
	
	private void performAfterSelectionEventSave()
	{
		if(saveChosenSelection != null)
		{
			String [] [] properties = getProperties();
			PathUtility.writeProperties(saveFilePathChosen, properties);
		}
		saveChosenSelection.clear();//reset.
		saveFilePathChosen = "";
	}
	
	private String [] [] getProperties()
	{
		if(saveChosenSelection == null || saveChosenSelection.isEmpty())
		{
			saveChosenSelection = new ArrayList<String>();
			saveChosenSelection.addAll(KeepSelection.getTextOnlyConversion(keeps));
		}
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
		return properties;
	}
	
	@Override
	public void selectionChosen(List<String> chosenSelection) 
	{
		if(chosenSelection == null) return;
		LoggingMessages.printOut(LoggingMessages.combine(chosenSelection));//print
		saveChosenSelection = (List<String>) chosenSelection;
		performAfterSelectionEventSave();
	}
	
}
