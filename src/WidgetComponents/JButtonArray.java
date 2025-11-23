package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
import MouseListenersImpl.ImageMouseAdapter;
import MouseListenersImpl.PicLabelMouseListener;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.CloseAllActionExtension;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetExtensions.ExtendedStringCollection;
import WidgetExtensions.MinimizeActionExtension;
import WidgetExtensions.MouseAdapterArrayExtension;
import WidgetExtensions.OpenActionExtension;
import WidgetExtensions.RestoreActionExtension;
import WidgetExtensions.SaveActionExtension;
import WidgetExtensions.ShiftFramesExtension;
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
		SCALED_DEFAULT_PIC = new Dimension(279, 150),
		PIC_PAD = new Dimension(5,	20),
		SCALED_WIDTH_HEIGHT = new Dimension(279, 402),
		DIM_PIC = new Dimension(
				SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	
	public static String 
		DEFAULT_IMG = "./Properties/shapes/Default-Play-Image.xml";
	public static File 
		MOVIE_IMAGE_FILE_LOCATION = new File(PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/film-movies-icon.png");
	
	private String keepsFileLocation;
	public Color []
		foregroundAndBackgroundColor = new Color [] {new JButton().getForeground(), new JButton().getBackground()},
		highlightForegroundAndBackgroundColor = new Color [] {foregroundAndBackgroundColor[0], foregroundAndBackgroundColor[1]};
	private boolean isHighlight = true;
	private JButton highlightButton = null;
	private HashMap<String, ArrayList<JButtonLengthLimited>> collectionJButtons = new HashMap<String, ArrayList<JButtonLengthLimited>>();
	private HashMap<String, MouseListener> pathAndMouseAdapter;
	private ArrayList<String> stripFilter = new ArrayList<String>();
	private ShiftDialog shiftDialog;
	
	private int characterLimit=0;
	
	public JButtonArray()
	{
		
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
	
	public void setScaledWidthHeight(Dimension widthHeight)
	{
		SCALED_WIDTH_HEIGHT = widthHeight;
		DIM_PIC = new Dimension(
				SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	}
	
	public void setPicturePadWidthHeight(Dimension widthHeight)
	{
		PIC_PAD = widthHeight;
		DIM_PIC = new Dimension(
				SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
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
	public Dimension getScaledWidthHeight() 
	{
		return DIM_PIC;
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
		keepsFileLocation = directorySelection.getFullPath();
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
		HashMap<String, String> props = performPropertiesOpen();
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
	
	private HashMap<String, String> performPropertiesOpen()
	{
		HashMap<String, String> props = null;
		
		JFileChooser jfc = new JFileChooser();
		File f = new File(keepsFileLocation);
		jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_OPEN_TITLE, PROPERTIES_FILE_OPEN_FILTER));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showOpenDialog(this);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			props = PathUtility.readProperties(chosenFile.getAbsolutePath(), PROPERTIES_FILE_DELIMITER);
		}
		return props;
	}
	
	private ArrayList<KeepSelection> getKeepSelection()
	{
		return ((ImageMouseAdapter)getAMouseListener()).getKeeps();
	}

	@Override
	public List<String> getSelectionValues() 
	{
		return KeepSelection.getTextOnlyConversion(getKeepSelection());
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
		performMinimize();
		for(KeepSelection ks : getKeepSelection().toArray(new KeepSelection[] {}))
		{
			ks.getFrame().setExtendedState(JFrame.NORMAL);
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

}
