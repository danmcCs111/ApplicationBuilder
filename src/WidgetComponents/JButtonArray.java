package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.ArrayActionListener;
import MouseListenersImpl.ImageMouseAdapter;
import MouseListenersImpl.PicLabelMouseListener;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetComponentInterfaces.DialogParentReferenceContainer;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.CloseAllActionExtension;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetExtensions.ExtendedStringCollection;
import WidgetExtensions.MouseAdapterArrayExtension;
import WidgetExtensions.OpenActionExtension;
import WidgetExtensions.SaveActionExtension;
import WidgetUtility.FileListOptionGenerator;
import WidgetUtility.WidgetBuildController;

/**
 * Holds a collection of JButtons of variable generated size
 * Builds a list of Buttons
 * 
 * TODO use a collection of inner panels and switch during toggle?
 */
public class JButtonArray extends JPanel implements ArrayActionListener, CharacterLimited, 
SaveActionExtension, OpenActionExtension, CloseActionExtension, CloseAllActionExtension,  
ComboListDialogSelectedListener, DialogParentReferenceContainer, MouseAdapterArrayExtension,  
PostWidgetBuildProcessing
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
	
	public static Dimension
		DIM_DEFAULT_PIC = new Dimension(279,150),
		PIC_PAD = new Dimension(5,	20),
		SCALED_WIDTH_HEIGHT = new Dimension(279, 402),
		DIM_PIC = new Dimension(
				JButtonArray.SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				JButtonArray.SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	public static String 
		DEFAULT_IMG = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/shapes/Default-Play-Image.xml";
	
	private static String keepsFileLocation;
	public static Color []
		foregroundAndBackgroundColor = new Color [] {new JButton().getForeground(), new JButton().getBackground()},
		highlightForegroundAndBackgroundColor = new Color [] {foregroundAndBackgroundColor[0], foregroundAndBackgroundColor[1]};
	private static int indexPos=0;
	private static boolean isHighlight = true;
	private static JButton highlightButton = null;
	private static ArrayList<String> indexPaths = new ArrayList<String>();
	private static HashMap<String, ArrayList<AbstractButton>> collectionJButtons = new HashMap<String, ArrayList<AbstractButton>>();
	private static HashMap<String, MouseListener> pathAndMouseAdapter;
	private static ArrayList<String> stripFilter = new ArrayList<String>();
	private String searchFilterText = "";
	
	private int characterLimit=0;
	
	public JButtonArray()
	{
		
	}
	
	public void setDefaultImageXmlPath(FileSelection fs)
	{
		DEFAULT_IMG = fs.getFullPath();
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
	
	public void setDimensionDefaultPic(Dimension dim)
	{
		DIM_DEFAULT_PIC = dim;
	}
	
	public void setScaledWidthHeight(Dimension widthHeight)
	{
		SCALED_WIDTH_HEIGHT = widthHeight;
		DIM_PIC = new Dimension(
				JButtonArray.SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				JButtonArray.SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	}
	
	public void setPicturePadWidthHeight(Dimension widthHeight)
	{
		PIC_PAD = widthHeight;
		DIM_PIC = new Dimension(
				JButtonArray.SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				JButtonArray.SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	}
	
	public void setSkipImageLoading(boolean skip)
	{
		KeepSelection.skip = skip;
	}
	
	public static final ActionListener highlightActionListener = new ActionListener() 
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
	
	public static final ActionListener highlightLabelActionListener = new ActionListener() 
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
	
	public void addJButtons(String path, List<String> listOf, int index)
	{
		LoggingMessages.printOut("load buttons." + listOf.size() + " " + index);
		ArrayList<AbstractButton> jbuts = new ArrayList<AbstractButton>();
		JButtonArray.indexPos = index;
		
		clearJButtons();
		
		if(!indexPaths.contains(path))
		{
			for(Component comp : FileListOptionGenerator.buildComponents(path, listOf, JButtonLengthLimited.class))
			{
				if(comp instanceof Component)
				{
					if(comp instanceof JButtonLengthLimited)
					{
						String txt = ((JButtonLengthLimited) comp).getFullLengthText();
						
						for(String s : stripFilter)
						{
							txt = txt.replace(s, "");
						}
						if(characterLimit != 0)
						{
							((JButtonLengthLimited) comp).setCharacterLimit(characterLimit);
						}
						((JButtonLengthLimited) comp).setText(txt);
					}
					comp.setForeground(foregroundAndBackgroundColor[0]);
					comp.setBackground(foregroundAndBackgroundColor[1]);
					addHighlightButtonActionListener((JButton)comp);
					jbuts.add((JButton) comp);
					this.add(comp);
				}
			}
			addActionListeners(jbuts);
			collectionJButtons.put(path, jbuts);
		}
		else
		{
			jbuts = collectionJButtons.get(indexPaths.get(JButtonArray.indexPos));
			for(Component but : jbuts)
			{
				this.add(but);
			}
		}
		adjustVisibility(searchFilterText);
		
		indexPaths.add(path);
		
		ExtendedStringCollection esc = getExtendedStringCollection(this);
		esc.setPathSelected(path);
		
		Container rootCont = getRootPane();
		rootCont.paintComponents(rootCont.getGraphics());
		
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
	
	public static void setHighlight(boolean isHighlight)
	{
		JButtonArray.isHighlight = isHighlight;
	}
	
	public static void setHighlightForegroundColor(Color c)
	{
		JButtonArray.highlightForegroundAndBackgroundColor[0] = c;
	}
	
	public static void setHighlightBackgroundColor(Color c)
	{
		JButtonArray.highlightForegroundAndBackgroundColor[1] = c;
	}
	
	public void addHighlightButtonActionListener(JButton but)
	{
		if(isHighlight)
		{
			but.addActionListener(highlightActionListener);
		}
	}
	
	public void clearJButtons()
	{
		this.removeAll();
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		this.actionListener = actionListener;
		if(collectionJButtons.size()-1 >= indexPos)
		{
			addActionListeners(collectionJButtons.get(indexPaths.get(indexPos)));
		}
	}
	
	private void addActionListeners(ArrayList<AbstractButton> jButtons)
	{
		if(this.actionListener != null && !jButtons.isEmpty())
		{
			for(Component button : jButtons)
			{
				if(button instanceof AbstractButton)
				{
					((AbstractButton)button).addActionListener(actionListener);
					((AbstractButton)button).addActionListener(highlightLabelActionListener);
				}
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
			JButtonArray.foregroundAndBackgroundColor[backgroundOrForeground[i]] = c[i];
		}
		
		for(List<AbstractButton> buts : collectionJButtons.values())
		{
			for(Component but : buts)
			{
				for(int bof : backgroundOrForeground)
				{
					if(bof == 1) but.setForeground(JButtonArray.foregroundAndBackgroundColor[bof]);
					else but.setBackground(JButtonArray.foregroundAndBackgroundColor[bof]);
				}
			}
		}
	}
	
	private static void setHighlightForegroundAndBackground(boolean highlight)
	{
		Color [] color = highlight ? highlightForegroundAndBackgroundColor : foregroundAndBackgroundColor;
		highlightButton.setForeground(color[0]);
		highlightButton.setBackground(color[1]);
	}
	
	public static boolean isHighlightButton(AbstractButton ab)
	{
		return highlightButton == ab;
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
		for(MouseListener ml : collectionJButtons.get(indexPaths.get(indexPos)).get(0).getMouseListeners())
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
		
		int choice = jfc.showOpenDialog(WidgetBuildController.getInstance().getFrame());
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
		for(MouseListener ml : collectionJButtons.get(indexPaths.get(indexPos)).get(0).getMouseListeners())
		{
			if(ml instanceof ImageMouseAdapter)
			{
				return (ImageMouseAdapter) ml;
			}
		}
		return null;//shouldn't.
	}

	@Override
	public Point getContainerCenterLocationPoint() 
	{
		return new Point((int)WidgetBuildController.getInstance().getFrame().getBounds().getCenterX(), 
				(int)WidgetBuildController.getInstance().getFrame().getBounds().getCenterY());
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
		if(JButtonArray.pathAndMouseAdapter == null || JButtonArray.pathAndMouseAdapter.isEmpty())
		{
			return;
		}
		for(String key : collectionJButtons.keySet())
		{
			LoggingMessages.printOut(key + " " + JButtonArray.pathAndMouseAdapter.size() + " " + collectionJButtons.get(key).size());
			ImageMouseAdapter ima = (ImageMouseAdapter) JButtonArray.pathAndMouseAdapter.get(key);
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
		JFrame f = (JFrame) this.getRootPane().getParent();//close?
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
		JButtonArray.pathAndMouseAdapter = pathAndMouseAdapter;
	}

	public void adjustVisibility(String searchPattern)//TODO
	{
		searchFilterText = searchPattern;
		
		for(String key : collectionJButtons.keySet())
		{
			for(AbstractButton ab : collectionJButtons.get(key))
			{
				ab.setVisible(ab.getText().toLowerCase().contains(searchFilterText.toLowerCase()));//case insensitive
			}
		}
		this.validate();
	}

}
