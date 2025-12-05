package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.NavigationButtonActionListener;
import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.YoutubePageParser;
import MouseListenersImpl.ImageMouseAdapter;
import MouseListenersImpl.PicLabelMouseListener;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentDialogs.ShiftDialog;
import WidgetComponentDialogs.VideoBookMarksDialog;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensionDefs.ExtendedStringCollection;
import WidgetExtensionInterfaces.CloseActionExtension;
import WidgetExtensionInterfaces.CloseAllActionExtension;
import WidgetExtensionInterfaces.ComboListDialogSelectedListener;
import WidgetExtensionInterfaces.MinimizeActionExtension;
import WidgetExtensionInterfaces.MouseAdapterArrayExtension;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetExtensionInterfaces.RestoreActionExtension;
import WidgetExtensionInterfaces.SaveActionExtension;
import WidgetExtensionInterfaces.ShiftFramesExtension;
import WidgetUtility.FileListOptionGenerator;
import WidgetUtility.WidgetBuildController;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 * 
 * TODO use a collection of inner panels and switch during toggle?
 */
public class JButtonArray extends JPanel implements ArrayActionListener, CharacterLimited, 
SaveActionExtension, OpenActionExtension, CloseActionExtension, CloseAllActionExtension, MinimizeActionExtension, RestoreActionExtension, ShiftFramesExtension,
ComboListDialogSelectedListener, MouseAdapterArrayExtension, 
PostWidgetBuildProcessing, ButtonArray
{
	private static final long serialVersionUID = 1883L;
	
	public static final String 
		SAVE_ID = "JButtonArray",
		CHARACTER_LIMIT_TEXT= "..",
		IMAGES_RELATIVE_FILE_LOCATION= "/images/",
		PROPERTIES_FILE_OPEN_TITLE = "Open Properties",
		PROPERTIES_FILE_OPEN_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = "\\.txt",
		PROPERTIES_FILE_ARG_DELIMITER = "@",
		PROPERTIES_FILE_DELIMITER = "=";
	
	private static Dimension
		DIM_DEFAULT_PIC = new Dimension(279,150),
		SCALED_DEFAULT_PIC = new Dimension(279, 150);
	private static int
		SCALED_WIDTH = 279,
		SCALED_WIDTH_PREVIEW = SCALED_WIDTH;
	
	public static String 
		DEFAULT_IMG = "./Properties/shapes/Default-Play-Image.xml";
	public static File 
		MOVIE_IMAGE_FILE_LOCATION = new File(PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/film-movies-icon.png");
	private static boolean
		SHOW_JAVA_SWING_FILE_CHOOSER = false,
		SHOW_TITLE_ON_POSTER = true,
		SHOW_PREVIEW = true;
	static {
		setJavaSwingFileChooser(SHOW_JAVA_SWING_FILE_CHOOSER);
		setJavaSwingFileChooser(SHOW_TITLE_ON_POSTER);
		setShowPreview(SHOW_PREVIEW);
	}
	
	private DirectorySelection keepsFileLocation;
	public Color []
		foregroundAndBackgroundColor = new Color [] {new JButton().getForeground(), new JButton().getBackground()},
		highlightForegroundAndBackgroundColor = new Color [] {foregroundAndBackgroundColor[0], foregroundAndBackgroundColor[1]};
	private JButton highlightButton = null;
	private ShiftDialog shiftDialog;
	private HashMap<String, ArrayList<JButtonLengthLimited>> collectionJButtons = new HashMap<String, ArrayList<JButtonLengthLimited>>();
	private HashMap<String, MouseListener> pathAndMouseAdapter;
	private ArrayList<String> stripFilter = new ArrayList<String>();
	private boolean isHighlight = true;
	
	private int characterLimit=0;
	
	public JButtonArray()
	{
		
	}
	
	public static void setDeleteForegroundColor(Color c)
	{
		ColorTemplate.setDeleteForegroundColor(c);
	}
	public static void setDeleteBackgroundColor(Color c)
	{
		ColorTemplate.setDeleteBackgroundColor(c);
	}
	
	public static void setShiftSliderMaxSettingXY(Point p)
	{
		ShiftDialog.setShiftSliderMaxSettingXY(p);
	}

	public static void setJavaSwingFileChooser(boolean isSwingFileChooser)
	{
		SHOW_JAVA_SWING_FILE_CHOOSER = isSwingFileChooser;
		ImageMouseAdapter.setJavaSwingFileChooser(SHOW_JAVA_SWING_FILE_CHOOSER);
	}
	
	public static void setShowTitleOnPoster(boolean showTitle)
	{
		SHOW_TITLE_ON_POSTER = showTitle;
		ImageMouseAdapter.setShowTitleOnPoster(SHOW_TITLE_ON_POSTER);
	}
	
	public static void setShowPreview(boolean showPreview)
	{
		SHOW_PREVIEW = showPreview;
		ImageMouseAdapter.setShowPreview(showPreview);
	}
	
	public String getDefaultImagePath()
	{
		return DEFAULT_IMG;
	}
	
	public void setMoviesIcon(FileSelection f)
	{
		MOVIE_IMAGE_FILE_LOCATION = new File(f.getFullPath());
	}
	
	public void setSingleClick(boolean singleClick)
	{
		for(String key : pathAndMouseAdapter.keySet())
		{
			MouseListener ml = pathAndMouseAdapter.get(key);
			if(ml instanceof ImageMouseAdapter)
			{
				((ImageMouseAdapter) ml).setSingleClick(singleClick);
			}
		}
	}
	
	@Override
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension) 
	{
		SCALED_DEFAULT_PIC = scaledDefaultPicDimension;
	}

	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		DIM_DEFAULT_PIC = defaultPicDimension;
	}

	@Override
	public void setDefaultImageXmlPath(FileSelection fs)
	{
		DEFAULT_IMG = fs.getRelativePath();
	}
	
	public void setScaledWidth(int width)
	{
		SCALED_WIDTH = width;
	}
	
	@Override
	public Dimension getDefaultPicSize() 
	{
		return DIM_DEFAULT_PIC;
	}
	
	@Override
	public Dimension getScaledDefaultPic() 
	{
		return SCALED_DEFAULT_PIC;
	}

	@Override
	public int getScaledWidth() 
	{
		return SCALED_WIDTH;
	}
	
	@Override
	public int getScaledWidthPreview() 
	{
		return SCALED_WIDTH_PREVIEW;
	}
	
	public void setScaledWidthPreview(int width) 
	{
		SCALED_WIDTH_PREVIEW = width;
	}
	
	public void setSkipImageLoading(boolean skip)
	{
		KeepSelection.skip = skip;
	}
	
	public final ActionListener highlightActionListener = new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(highlightButton != null)//return and set new selected 
			{
				setHighlightForegroundAndBackground(false);
			}
			highlightButton = (JButton) e.getSource();
			setHighlightForegroundAndBackground(true);
		}
	};
	
	public final ActionListener highlightLabelActionListener = new ActionListener() 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() instanceof JButtonLengthLimited)//TODO
			{
				JButtonLengthLimited ab = (JButtonLengthLimited) e.getSource();
				PicLabelMouseListener.highLightLabel(ab, e.getID() == (ActionEvent.ACTION_PERFORMED));
			}
		}
	};
	
	private ActionListener actionListener = null;
	
	public void setExpandedArrangementFileRelativeLocation(DirectorySelection directorySelection)
	{
		keepsFileLocation = directorySelection;
	}
	
	public static ExtendedStringCollection getExtendedStringCollection(Component c)
	{
		Component tmp = c;
		while(tmp.getParent() != null)
		{
			if(tmp.getParent() instanceof ExtendedStringCollection)
			{
				return (ExtendedStringCollection) tmp.getParent();
			}
			tmp = tmp.getParent();
		}
		return null;
	}
	
	public void setHighlight(boolean isHighlight)
	{
		this.isHighlight = isHighlight;
	}
	
	public void setHighlightForegroundColor(Color c)
	{
		highlightForegroundAndBackgroundColor[0] = c;
	}
	
	public void setHighlightBackgroundColor(Color c)
	{
		highlightForegroundAndBackgroundColor[1] = c;
	}
	
	public void addHighlightButtonActionListener(JButton but)
	{
		if(isHighlight)
		{
			but.addActionListener(highlightActionListener);
		}
	}
	
	private void clearJButtons()
	{
		this.removeAll();
	}
	
	private void rebuildButtons()
	{
		clearJButtons();
		for(AbstractButton ab : collectionJButtons.get(SwappableCollection.indexPaths.get(NavigationButtonActionListener.getCurPosition())))
		{
			if(ab.isVisible())
			{
				this.add(ab);
			}
		}
		JFrame f = WidgetBuildController.getInstance().getFrame();
		f.paintComponents(f.getGraphics());
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		int indexPos = NavigationButtonActionListener.getCurPosition();
		this.actionListener = actionListener;
		if(collectionJButtons.size()-1 >= indexPos)
		{
			addActionListeners(collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)));
		}
	}
	
	private void addActionListeners(ArrayList<JButtonLengthLimited> jButtons)
	{
		if(this.actionListener != null && !jButtons.isEmpty())
		{
			for(JButtonLengthLimited button : jButtons)
			{
				button.addActionListener(actionListener);
				button.addActionListener(highlightLabelActionListener);
			}
		}
	}
	
	public void setArrayForeground(Color c)
	{
		setArrayColor(c, 0);
	}
	
	public void setArrayBackground(Color c)
	{
		setArrayColor(c, 1);
	}
	
	public void setArrayForegroundAndBackground(Color cF, Color cB)
	{
		setArrayColor(new Color [] {cF, cB}, new int [] {0,1});
	}
	
	private void setArrayColor(Color c, int backgroundOrForeground )
	{
		setArrayColor(new Color [] {c}, new int [] {backgroundOrForeground});
	}
	
	private void setArrayColor(Color [] c, int [] backgroundOrForeground )
	{
		for(int i = 0; i < backgroundOrForeground.length; i++)
		{
			foregroundAndBackgroundColor[backgroundOrForeground[i]] = c[i];
		}
		
		for(List<JButtonLengthLimited> buts : collectionJButtons.values())
		{
			for(Component but : buts)
			{
				for(int bof : backgroundOrForeground)
				{
					if(bof == 1) but.setForeground(foregroundAndBackgroundColor[bof]);
					else but.setBackground(foregroundAndBackgroundColor[bof]);
				}
			}
		}
	}
	
	private void setHighlightForegroundAndBackground(boolean highlight)
	{
		Color [] color = highlight ? highlightForegroundAndBackgroundColor : foregroundAndBackgroundColor;
		highlightButton.setForeground(color[0]);
		highlightButton.setBackground(color[1]);
	}
	
	@Override
	public boolean isHighlightButton(AbstractButton ab)
	{
		return highlightButton == ab;
	}
	
	@Override
	public void addJButtons(String path, List<String> listOf, int index)
	{
		LoggingMessages.printOut("load buttons." + listOf.size() + " " + index);
		ArrayList<JButtonLengthLimited> jbuts = new ArrayList<JButtonLengthLimited>();
		
		clearJButtons();
		
		if(!SwappableCollection.indexPaths.contains(path))
		{
			for(Component comp : FileListOptionGenerator.buildComponents(path, listOf, JButtonLengthLimited.class))
			{
				JButtonLengthLimited jbl = (JButtonLengthLimited)comp;
				String txt = jbl.getFullLengthText();
				
				for(String s : stripFilter)
				{
					txt = txt.replace(s, "");
				}
				if(characterLimit != 0)
				{
					jbl.setCharacterLimit(characterLimit);
				}
				jbl.setText(txt);
				comp.setForeground(foregroundAndBackgroundColor[0]);
				comp.setBackground(foregroundAndBackgroundColor[1]);
				addHighlightButtonActionListener(jbl);
				jbuts.add(jbl);
				this.add(comp);
			}
			addActionListeners(jbuts);
			Collections.sort(jbuts, new JButtonLengthLimited());
			collectionJButtons.put(path, jbuts);
			
			SwappableCollection.indexPaths.add(path);
			
		}
		else
		{
			rebuildButtons();
		}
		
		ExtendedStringCollection esc = getExtendedStringCollection(this);
		if(esc != null)	esc.setPathSelected(path);
		
		JFrame f = WidgetBuildController.getInstance().getFrame();
		f.paintComponents(f.getGraphics());
		
	}

	@Override
	public void unselect() 
	{
		setHighlightForegroundAndBackground(false);
		highlightButton = null;
	}

	@Override
	public void addStripFilter(String filter) 
	{
		stripFilter.add(filter);
	}
	
	@Override
	public void setCharacterLimit(int characterLimit)
	{
		this.characterLimit = characterLimit;
	}

	@Override
	public void performSave() 
	{
		int indexPos = NavigationButtonActionListener.getCurPosition();
		for(MouseListener ml : collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)).get(0).getMouseListeners())
		{
			if(ml instanceof ImageMouseAdapter)
			{
				((ImageMouseAdapter) ml).writeSave();
				return;
			}
		}
	}
	
	@Override
	public void performOpen()
	{
		if(!SHOW_JAVA_SWING_FILE_CHOOSER)
		{
			new VideoBookMarksDialog(keepsFileLocation, this, WidgetBuildController.getInstance().getFrame());
		}
		else
		{
			HashMap<String, String> props = null;
			
			JFileChooser jfc = new JFileChooser();
			File f = new File(keepsFileLocation.getFullPath());
			jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_OPEN_TITLE, PROPERTIES_FILE_OPEN_FILTER));
			jfc.setSelectedFile(f);
			
			int choice = jfc.showOpenDialog(this);
			File chosenFile = jfc.getSelectedFile();
			if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
			{
				props = PathUtility.readProperties(chosenFile.getAbsolutePath(), PROPERTIES_FILE_DELIMITER);
				openKeeps(props);
			}
		}
	}
	
	@Override
	public void openKeeps(HashMap<String, String> props)
	{
		if(props == null)
			return;
		
		for(String s : collectionJButtons.keySet())//search entire collection. issue with duplicates when link is in more than one folder
		{
			for(MouseListener ml : collectionJButtons.get(s).get(0).getMouseListeners())
			{
				if(ml instanceof ImageMouseAdapter)
				{
					for(String key : props.keySet())
					{
						String [] k = key.split(PROPERTIES_FILE_ARG_DELIMITER);
						for(Component b : collectionJButtons.get(s))
						{
							if(((JButtonLengthLimited) b).getFullLengthText().equals(k[0]))//TODO
							{
								for(MouseListener al : b.getMouseListeners())
								{
									if(al instanceof ImageMouseAdapter)
									{
										((ImageMouseAdapter) al).setupKeepFrame(b, Integer.parseInt(k[1]), Integer.parseInt(k[2]));
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private ArrayList<KeepSelection> getKeepSelection()
	{
		return ((ImageMouseAdapter)getAMouseListener()).getKeeps();
	}

	@Override
	public List<String> getSelectionValues() 
	{
		@SuppressWarnings("unchecked")
		List<KeepSelection> cloned = (List<KeepSelection>) getKeepSelection().clone();
		Collections.sort(cloned, new KeepSelection());
		return KeepSelection.getTextOnlyConversion(cloned);
	}

	
	public ImageMouseAdapter getAMouseListener() 
	{
		int indexPos = NavigationButtonActionListener.getCurPosition();
		for(MouseListener ml : collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)).get(0).getMouseListeners())
		{
			if(ml instanceof ImageMouseAdapter)
			{
				return (ImageMouseAdapter) ml;
			}
		}
		return null;//shouldn't.
	}

	@Override
	public void selectionChosen(List<String> chosenSelection) 
	{
		if(chosenSelection == null)
			return;
		
		//Change to array to avoid concurrent issue
		for(KeepSelection ks : getKeepSelection().toArray(new KeepSelection[getKeepSelection().size()]))
		{
			if(chosenSelection.contains(ks.getText()))
			{
				ks.getFrame().dispatchEvent(new WindowEvent(ks.getFrame(), WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	@Override
	public Object getCloseListener() 
	{
		return this;
	}

	@Override
	public void closeAll() 
	{
		for(KeepSelection ks : getKeepSelection().toArray(new KeepSelection[] {}))
		{
			ks.getFrame().dispatchEvent(new WindowEvent(ks.getFrame(), WindowEvent.WINDOW_CLOSING));
		}
	}

	@Override
	public void postExecute() //perform mouse listener adapter add as post processing TODO include in xml.
	{
		if(pathAndMouseAdapter == null || pathAndMouseAdapter.isEmpty())
		{
			return;
		}
		for(String key : collectionJButtons.keySet())
		{
			LoggingMessages.printOut(key + " " + pathAndMouseAdapter.size() + " " + collectionJButtons.get(key).size());
			ImageMouseAdapter ima = (ImageMouseAdapter) pathAndMouseAdapter.get(key);
			ima.setupKeepsSelection(collectionJButtons.get(key));
			outer:
			for(Component c : collectionJButtons.get(key))
			{
				
				for(MouseListener ml : c.getMouseListeners())//TODO patch fix...need to get away from static
				{
					if(ml instanceof ImageMouseAdapter)
					{
						c.removeMouseListener(ml);
						c.addMouseListener(ima);
						continue outer;
					}
				}
				c.addMouseListener(ima);
			}
		}
		JFrame f = WidgetBuildController.getInstance().getFrame();
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				closeAll();
			}
		});
		addPasteFromClipboard(this);
	}

	private void addPasteFromClipboard(Component target)
	{
		new DropTarget(target, new DropTargetAdapter() 
		{
			@Override
			public void drop(DropTargetDropEvent dtde) 
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable t = dtde.getTransferable();
				try {
					if(t.isDataFlavorSupported(DataFlavor.stringFlavor))
					{
						String s = (String) t.getTransferData(DataFlavor.stringFlavor);
						if(YoutubePageParser.isYoutube(s))
						{
							//TODO
							s = HttpDatabaseRequest.addHttpsIfMissing(s);
							LoggingMessages.printOut("data: " + s);
							String resp = HttpDatabaseRequest.executeGetRequest(s);
							LoggingMessages.printOut("response");
							LoggingMessages.printOut(YoutubePageParser.getImageUrl(resp));
							LoggingMessages.printOut(YoutubePageParser.getTitle(resp));
						}
					}
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@Override
	public void setPathAndMouseListenerAdapter(HashMap<String, MouseListener> pathAndMouseAdapter) 
	{
		this.pathAndMouseAdapter = pathAndMouseAdapter;
	}

	@Override
	public void adjustVisibility(String searchPattern)
	{
		for(String key : collectionJButtons.keySet())
		{
			for(AbstractButton ab : collectionJButtons.get(key))
			{
				ab.setVisible(ab.getText().toLowerCase().contains(searchPattern.toLowerCase()));//case insensitive
			}
		}
		rebuildButtons();
	}

	@Override
	public void performMinimize() 
	{
		for(KeepSelection ks : getKeepSelection().toArray(new KeepSelection[] {}))
		{
			LoggingMessages.printOut(ks.getFrame().getExtendedState() + "");
			ks.getFrame().setExtendedState(Frame.ICONIFIED);
		}
	}

	@Override
	public void performRestore() 
	{
		for(KeepSelection ks : getKeepSelection().toArray(new KeepSelection[] {}))
		{
			ks.getFrame().setExtendedState(JFrame.NORMAL);
			ks.getFrame().toFront();
		}
	}

	@Override
	public void buildLoadingFrame() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setIsLoadingSpinGraphic(boolean loadGraphic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openShiftFramesDialog() 
	{
		if(shiftDialog == null || !shiftDialog.isVisible())
		{
			shiftDialog = new ShiftDialog(this, getKeepSelection());
		}
	}

	@Override
	public void saveKeeps(File saveFile, String [] [] props) 
	{
		if(saveFile == null)
			return;
		
		int indexPos = NavigationButtonActionListener.getCurPosition();
		for(MouseListener ml : collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)).get(0).getMouseListeners())
		{
			if(ml instanceof ImageMouseAdapter)
			{
				ImageMouseAdapter iml = ((ImageMouseAdapter) ml);
				iml.setSaveFile(saveFile.getAbsolutePath());
				iml.getSaveSelections();
				break;
			}
		}
	}

}
