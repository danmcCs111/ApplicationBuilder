package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.ArrayActionListener;
import MouseListeners.ImageMouseAdapter;
import MouseListeners.PicLabelMouseListener;
import Params.KeepSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.ExtendedStringCollection;
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
public class JButtonArray extends JPanel implements ArrayActionListener, CharacterLimited, SaveActionExtension, OpenActionExtension, CloseActionExtension, ComboListDialogSelectedListener, DialogParentReferenceContainer
{
	private static final long serialVersionUID = 1883L;
	public static final String 
		SAVE_ID = "JButtonArray",
		CHARACTER_LIMIT_TEXT= "..",
		IMAGES_RELATIVE_FILE_LOCATION= "/images/",
		PROPERTIES_FILE_LOCATION = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/data/ ",
		PROPERTIES_FILE_OPEN_TITLE = "Open Properties",
		PROPERTIES_FILE_OPEN_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = "\\.txt",
		PROPERTIES_FILE_ARG_DELIMITER = "@",
		PROPERTIES_FILE_DELIMITER = "=";
	
	public static Color []
		backgroundAndForegroundColor = new Color [] {new JButton().getBackground(), new JButton().getForeground()},
		highlightBackgroundAndForegroundColor = new Color [] {backgroundAndForegroundColor[0], backgroundAndForegroundColor[1]};
	private static int indexPos=0;
	private static boolean isHighlight = true;
	private static JButton highlightButton = null;
	private static final ArrayList<String> indexPaths = new ArrayList<String>();
	private static HashMap<String, ArrayList<AbstractButton>> collectionJButtons = new HashMap<String, ArrayList<AbstractButton>>();
	private static ArrayList<String> stripFilter = new ArrayList<String>();
	
	private int characterLimit=0;
	
	public JButtonArray()
	{
		//startup
		super();
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
			if(e.getSource() instanceof AbstractButton)
			{
				AbstractButton ab = (AbstractButton) e.getSource();
				PicLabelMouseListener.highLightLabel(ab, e.getID() == (ActionEvent.ACTION_PERFORMED));
			}
		}
	};
	
	private ActionListener actionListener = null;
	
	public void addJButtons(String path, List<String> listOf, int index)
	{
		LoggingMessages.printOut("load buttons." + listOf.size() + " " + index);
		ArrayList<AbstractButton> jbuts = new ArrayList<AbstractButton>();
		JButtonArray.indexPos = index;
		
		clearJButtons();
		
		if(!indexPaths.contains(path))
		{
			for(Component comp : FileListOptionGenerator.buildComponents(path, listOf, JButton.class))
			{
				if(comp instanceof Component)
				{
					if(comp instanceof AbstractButton)
					{
						String txt = ((AbstractButton) comp).getText();
						
						for(String s : stripFilter)
						{
							txt = txt.replace(s, "");
						}
						if(characterLimit != 0)
						{
							((AbstractButton) comp).setText(txt.length() >= this.characterLimit
									? txt.substring(0, this.characterLimit-CHARACTER_LIMIT_TEXT.length()) + CHARACTER_LIMIT_TEXT
									: txt);
						}
						else
						{
							((AbstractButton) comp).setText(txt);
						}
						ImageMouseAdapter ima;
						try {
							ima = new ImageMouseAdapter(comp, 
									WidgetBuildController.getInstance().getFrame(),
									PathUtility.getCurrentDirectory() + PathUtility.removeCurrentWorkingDirectoryFromPath(path) + IMAGES_RELATIVE_FILE_LOCATION, 
									txt);
							((AbstractButton) comp).addMouseListener(ima);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					comp.setForeground(backgroundAndForegroundColor[1]);
					comp.setBackground(backgroundAndForegroundColor[0]);
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
		JButtonArray.highlightBackgroundAndForegroundColor[1] = c;
	}
	
	public static void setHighlightBackgroundColor(Color c)
	{
		JButtonArray.highlightBackgroundAndForegroundColor[0] = c;
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
		setArrayColor(c, 1);
	}
	
	public void setArrayBackground(Color c)
	{
		setArrayColor(c, 0);
	}
	
	public void setArrayForegroundAndBackground(Color cF, Color cB)
	{
		setArrayColor(new Color [] {cB, cF}, new int [] {0,1});
	}
	
	private void setArrayColor(Color c, int backgroundOrForeground )
	{
		setArrayColor(new Color [] {c}, new int [] {backgroundOrForeground});
	}
	
	private void setArrayColor(Color [] c, int [] backgroundOrForeground )
	{
		for(int i = 0; i < backgroundOrForeground.length; i++)
		{
			JButtonArray.backgroundAndForegroundColor[backgroundOrForeground[i]] = c[i];
		}
		
		for(List<AbstractButton> buts : collectionJButtons.values())
		{
			for(Component but : buts)
			{
				for(int bof : backgroundOrForeground)
				{
					if(bof == 1) but.setForeground(JButtonArray.backgroundAndForegroundColor[bof]);
					else but.setBackground(JButtonArray.backgroundAndForegroundColor[bof]);
				}
			}
		}
	}
	
	private static void setHighlightForegroundAndBackground(boolean highlight)
	{
		Color [] color = highlight ? highlightBackgroundAndForegroundColor : backgroundAndForegroundColor;
		highlightButton.setForeground(color[1]);
		highlightButton.setBackground(color[0]);
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
		
		for(String s : collectionJButtons.keySet())//search entire collection.
		{
			for(MouseListener ml : collectionJButtons.get(s).get(0).getMouseListeners())
			{
				if(ml instanceof ImageMouseAdapter)
				{
					for(String key : props.keySet())
					{
						String [] k = key.split(PROPERTIES_FILE_ARG_DELIMITER);
						for(AbstractButton b : collectionJButtons.get(s))
						{
							if(b.getText().equals(k[0]))
							{
								for(MouseListener al : b.getMouseListeners())
								{
									if(al instanceof ImageMouseAdapter)
									{
										((ImageMouseAdapter) al).setupKeepFrame(Integer.parseInt(k[1]), Integer.parseInt(k[2]));
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
		File f = new File(PROPERTIES_FILE_LOCATION);
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


}
