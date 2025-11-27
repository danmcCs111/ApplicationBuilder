package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.AddActionSend;
import ActionListeners.ArrayActionListener;
import ActionListenersImpl.AddActionReceive;
import ActionListenersImpl.NavigationButtonActionListener;
import Graphics2D.ColorTemplate;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import ShapeWidgetComponents.LoadingSpin;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;
import WidgetComponentDialogs.VideoBookMarksDialog;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.ButtonArrayLoadingNotification;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionDefs.ExtendedStringCollection;
import WidgetExtensionInterfaces.ButtonArrayLoadingNotifier;
import WidgetExtensionInterfaces.ClearActionExtension;
import WidgetExtensionInterfaces.ConnectedComponentName;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetExtensionInterfaces.SaveActionExtension;
import WidgetUtility.WidgetBuildController;

public class JButtonArrayListPicture extends JPanel implements ButtonArray, ArrayActionListener, CharacterLimited, 
AddActionSend, AddActionReceive,
OpenActionExtension, SaveActionExtension, ClearActionExtension,
ConnectedComponentName, ButtonArrayLoadingNotifier,
PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;

	public static final String 
		SAVE_ID = "JButtonArray",
		CHARACTER_LIMIT_TEXT= "..",
		IMAGES_RELATIVE_FILE_LOCATION= "/images/",
		PROPERTIES_FILE_OPEN_TITLE = "Open Properties",
		PROPERTIES_FILE_OPEN_FILTER = "txt",
		PROPERTIES_FILE_EXTENSION = "\\.txt",
		PROPERTIES_FILE_ARG_DELIMITER = "@",
		PROPERTIES_FILE_DELIMITER = "=";

	public ImageReader 
		imageReader = null;
	
	public static String 
		DEFAULT_IMG = "./Properties/shapes/Default-Play-Image.xml";
	public static Dimension
		DEFAULT_PIC = new Dimension(279, 150),
		SCALED_DEFAULT_PIC = new Dimension(140, 75),
		SCALED_WIDTH_HEIGHT = new Dimension(140, 200);
	private static DirectorySelection 
		keepsFileLocation;
	private static boolean
		SHOW_JAVA_SWING_FILE_CHOOSER = false,
		SHOW_TITLE_ON_POSTER = false;
	
	private HashMap<String, ArrayList<JCheckBoxLimited>> collectionJButtons = new HashMap<String, ArrayList<JCheckBoxLimited>>();
	private Point 
		saveIncPoint = new Point(100, 0),
		saveStartLocation = new Point(100, 700);
	private ArrayList<String> stripFilter = new ArrayList<String>();
	private int 
		characterLimit=0,
		columns = 3;
	private boolean 
		showAll = false,
		isLoadingSpinGraphic = true;
	private String connectedComponentName;
	private ArrayList<ButtonArrayLoadingNotification> loadingNofications = new ArrayList<ButtonArrayLoadingNotification>();
	private FileSelection xmlFile = new FileSelection("./Properties/shapes/reload.xml");
	private JFrame loadingFrame;
	
	public JButtonArrayListPicture()
	{
		
	}
	
	private void buildWidgets()
	{
		this.setLayout(new GridLayout(0, this.columns));
	}
	
	public static void setDeleteForegroundColor(Color c)
	{
		ColorTemplate.setDeleteForegroundColor(c);
	}
	public static void setDeleteBackgroundColor(Color c)
	{
		ColorTemplate.setDeleteBackgroundColor(c);
	}
	
	public static void setButtonForegroundColor(Color c)
	{
		ColorTemplate.setButtonForegroundColor(c);
	}
	public static void setButtonBackgroundColor(Color c)
	{
		ColorTemplate.setButtonBackgroundColor(c);
	}
	
	public static void setPanelBackgroundColor(Color c)
	{
		ColorTemplate.setPanelBackgroundColor(c);
	}
	
	public static void setJavaSwingFileChooser(boolean isSwingFileChooser)
	{
		SHOW_JAVA_SWING_FILE_CHOOSER = isSwingFileChooser;
	}
	
	public static void setShowTitleOnPoster(boolean showTitleOnPoster)
	{
		SHOW_TITLE_ON_POSTER = showTitleOnPoster;
	}
	
	public void setSaveIncPoint(Point saveIncPoint)
	{
		this.saveIncPoint = saveIncPoint;
	}
	
	public void setSaveStartLocation(Point saveStartLocation)
	{
		this.saveStartLocation = saveStartLocation;
	}
	
	public void setShowAll(boolean showAll)
	{
		this.showAll = showAll;
	}
	
	public boolean isShowAll()
	{
		return this.showAll;
	}
	
	public void setDefaultImageXmlPath(FileSelection fs)
	{
		DEFAULT_IMG = fs.getRelativePath();
	}
	
	@Override
	public String getDefaultImagePath() 
	{
		return DEFAULT_IMG;
	}
	
	@Override
	public void setScaledDefaultPic(Dimension widthHeight)
	{
		SCALED_DEFAULT_PIC = widthHeight;
	}

	@Override
	public void setScaledWidthHeight(Dimension widthHeight)
	{
		SCALED_WIDTH_HEIGHT = widthHeight;
	}
	
	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		DEFAULT_PIC = defaultPicDimension;
	}
	
	@Override
	public Dimension getDefaultPicSize() 
	{
		return DEFAULT_PIC;
	}
	
	@Override
	public Dimension getScaledDefaultPic() 
	{
		return SCALED_DEFAULT_PIC;
	}

	@Override
	public Dimension getScaledWidthHeight() 
	{
		return SCALED_WIDTH_HEIGHT;
	}
	
	public void setExpandedArrangementFileRelativeLocation(DirectorySelection directorySelection)
	{
		keepsFileLocation = directorySelection;
	}
	
	public void setColumnSize(int columns)
	{
		this.columns = columns;
	}
	
	private void clearJButtons()
	{
		this.removeAll();
	}
	
	private int getCollectionButtonSize()
	{
		int count = 0;
		for(String key : collectionJButtons.keySet())
		{
			count += collectionJButtons.get(key).size();
		}
		return count;
	}

	@Override
	public void addJButtons(String path, List<String> listOf, int index) 
	{
		ArrayList<JCheckBoxLimited> jbuts = new ArrayList<JCheckBoxLimited>();
		
		clearJButtons();
		
		if(!SwappableCollection.indexPaths.contains(path))
		{
			for(Component comp : buildComponents(path, listOf))
			{
				AbstractButton ab = (AbstractButton) comp;
				ImageIcon img = (ImageIcon) (ab.getIcon());
				String strippedText = getStrippedTextOnComponent((JCheckBoxLimited)comp);
				((JCheckBoxLimited) comp).setStrippedText(strippedText);
				if(SHOW_TITLE_ON_POSTER || img.equals(imageReader.getDefaultImageIcon()))
				{
					ab.setText(strippedText);
				}
				jbuts.add((JCheckBoxLimited) comp);
				this.add(comp);
			}
			Collections.sort(jbuts, new JCheckBoxLimited());
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
	
	private ImageIcon getImageIcon(String name, String path)
	{
		if(imageReader == null)
		{
			imageReader = new ImageReader(this);
		}
		String fileImage = PathUtility.getCurrentDirectory() +
				PathUtility.removeCurrentWorkingDirectoryFromPath(path) +
				IMAGES_RELATIVE_FILE_LOCATION +
				name.replaceAll(".url", ".png");
		return imageReader.setupImageIcon(new File(fileImage));
	}
	
	private String getStrippedTextOnComponent(JCheckBoxLimited comp)
	{
		String txt = comp.getFullLengthText();
		
		for(String s : stripFilter)
		{
			txt = txt.replace(s, "");
		}
		if(characterLimit != 0)
		{
			comp.setCharacterLimit(characterLimit);
		}
		return txt;
	}
	
	private void performLoadingNotify(int count, int total)
	{
		for(ButtonArrayLoadingNotification baln : loadingNofications)
		{
			baln.updateCount(count, total);
		}
	}
	
	private ArrayList<JComponent> buildComponents(String path, List<String> fileNames)
	{
		SwappableCollection swappableCollection = (SwappableCollection) ExtendedAttributeParam.findComponent(SwappableCollection.class);
		int fileCount = swappableCollection.getFileCount();
		int count = getCollectionButtonSize() + 1;
		
		ArrayList<JComponent> components = new ArrayList<JComponent>();
		for(String fileName: fileNames)
		{
			JCheckBoxLimited button = new JCheckBoxLimited();
			
			LoggingMessages.printOut("loading: " + count + " of " + fileCount);//TODO create notify
			performLoadingNotify(count, fileCount);
			LoggingMessages.printOut(fileName);

			ImageIcon img = getImageIcon(fileName, path);
			button.setIcon(img);
			
			if(SHOW_TITLE_ON_POSTER || img.equals(imageReader.getDefaultImageIcon()))
			{
				button.setText(fileName);
			}
			
			button.setName(fileName);
			button.setToolTipText(fileName);
			button.setPathKey(path);
			button.setBorderPainted(true);
			components.add(button);
			
			count++;
		}
		return components;
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
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
	
	private void rebuildButtons()
	{
		rebuildButtons(false);
	}
	private void rebuildButtons(boolean allKeys)
	{
		clearJButtons();
		
		if(!allKeys)
		{
			int indexPos = NavigationButtonActionListener.getCurPosition();
			for(AbstractButton ab : collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)))
			{
				if(ab.isVisible())
				{
					this.add(ab);
				}
			}
		}
		else
		{
			for(String key : collectionJButtons.keySet())
			{
				for(AbstractButton ab : collectionJButtons.get(key))
				{
					if(ab.isVisible())
					{
						this.add(ab);
					}
				}
			}
		}
		
		JFrame f = WidgetBuildController.getInstance().getFrame();
		f.paintComponents(f.getGraphics());
	}

	@Override
	public boolean isHighlightButton(AbstractButton ab) 
	{
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sendList(ArrayList<?> objs) 
	{
		HashMap<String, ArrayList<JCheckBoxLimited>> tmpColl = new HashMap<String, ArrayList<JCheckBoxLimited>>();
		for(JCheckBoxLimited cbl : (ArrayList<JCheckBoxLimited>)objs)
		{
			LoggingMessages.printOut(cbl.getPathKey());
			cbl.setSelected(false);
			ArrayList<JCheckBoxLimited> tmps;
			if(!tmpColl.containsKey(cbl.getPathKey()))
			{
				tmps = new ArrayList<JCheckBoxLimited>();
				tmps.add(cbl);
			}
			else
			{
				tmps = tmpColl.get(cbl.getPathKey());
				tmps.add(cbl);
			}
			tmpColl.put(cbl.getPathKey(), tmps);
		}
		
		for(String key : tmpColl.keySet())
		{
			ArrayList<JCheckBoxLimited> tmps;
			if(collectionJButtons.containsKey(key))
			{
				tmps = collectionJButtons.get(key);
			}
			else
			{
				tmps = new ArrayList<JCheckBoxLimited>();
			}
			tmps.addAll(tmpColl.get(key));
			Collections.sort(tmps, new JCheckBoxLimited());
			collectionJButtons.put(key, tmps);
		}
		rebuildButtons(isShowAll());
		
	}
	
	@Override
	public ArrayList<?> getList() 
	{
		ArrayList<JCheckBoxLimited> btns = new ArrayList<JCheckBoxLimited>();
		for(String key : collectionJButtons.keySet())
		{
			for(JCheckBoxLimited ab : collectionJButtons.get(key))
			{
				if(ab.isSelected())
				{
					btns.add(ab);
				}
			}
		}
		
		for(JCheckBoxLimited cb : btns)
		{
			String key = cb.getPathKey();
			if(collectionJButtons.get(key).contains(cb))
			{
				collectionJButtons.get(key).remove(cb);
			}
		}
		
		rebuildButtons(isShowAll());
		
		return btns;
	}
	
	@Override
	public void performClear() 
	{
		ArrayList<JCheckBoxLimited> cbls = new ArrayList<JCheckBoxLimited>();
		for(String key : collectionJButtons.keySet())
		{
			for(JCheckBoxLimited cbl : collectionJButtons.get(key))
			{
				cbls.add(cbl);
			}
		}
		collectionJButtons.clear();
		
		AddActionReceive addActionReceive = (AddActionReceive)WidgetBuildController.getInstance().findRefByName(connectedComponentName).getInstance();
		addActionReceive.sendList(cbls);
		
		rebuildButtons(isShowAll());
	}

	@Override
	public void performSave() 
	{
		ArrayList<String[]> allToSave = new ArrayList<String[]>();
		int 
			x = saveStartLocation.x,
			y = saveStartLocation.y;
		
		for(String k : collectionJButtons.keySet())
		{
			for(JCheckBoxLimited cbl : collectionJButtons.get(k))
			{
				allToSave.add(new String[] {cbl.getStrippedText()+"@"+x+"@"+y, cbl.getPathKey()});
				x+=saveIncPoint.x;
				y+=saveIncPoint.y;
			}
		}
		String [] [] props = allToSave.toArray(new String[][] {});
		
		performPropertiesSave(props);
	}

	@Override
	public void performOpen() 
	{
		performPropertiesOpen();
	}
	private void performPropertiesOpen()
	{
		if(!SHOW_JAVA_SWING_FILE_CHOOSER)
		{
			new VideoBookMarksDialog(keepsFileLocation, this, WidgetBuildController.getInstance().getFrame());
		}
		else //TODO linux / alternate option
		{
			HashMap<String, String> props = null;
			JFileChooser jfc = new JFileChooser();
			File f = new File(keepsFileLocation.getFullPath());
			jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_OPEN_TITLE, PROPERTIES_FILE_OPEN_FILTER));
			jfc.setSelectedFile(f);
			
			int choice = jfc.showOpenDialog(WidgetBuildController.getInstance().getFrame());
			File chosenFile = jfc.getSelectedFile();
			if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
			{
				props = PathUtility.readProperties(chosenFile.getAbsolutePath(), PROPERTIES_FILE_DELIMITER);
				openKeeps(props);
			}
		}
	}
	
	private void performPropertiesSave(String [] [] props)
	{
		if(!SHOW_JAVA_SWING_FILE_CHOOSER)
		{
			new VideoBookMarksDialog(keepsFileLocation, this, WidgetBuildController.getInstance().getFrame(), props);
		}
		else
		{
			JFileChooser jfc = new JFileChooser();
			File f = new File(keepsFileLocation.getFullPath());
			jfc.setFileFilter(new FileNameExtensionFilter(PROPERTIES_FILE_OPEN_TITLE, PROPERTIES_FILE_OPEN_FILTER));
			jfc.setSelectedFile(f);
			
			int choice = jfc.showSaveDialog(WidgetBuildController.getInstance().getFrame());
			File chosenFile = jfc.getSelectedFile();
			if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
			{
				PathUtility.writeProperties(chosenFile.getAbsolutePath(), props);
			}
		}
	}
	
	@Override
	public void postExecute() 
	{
		buildWidgets();
		if(loadingFrame != null)
		{
			loadingFrame.dispose();
		}
	}

	@Override
	public void setConnectedComponentName(String compName) 
	{
		this.connectedComponentName = compName;
	}

	@Override
	public void adjustVisibility(String searchPattern) 
	{
		for(String key : collectionJButtons.keySet())
		{
			for(AbstractButton ab : collectionJButtons.get(key))
			{
				ab.setVisible(ab.getName().toLowerCase().contains(searchPattern.toLowerCase()));//case insensitive
			}
		}
		rebuildButtons();
	}

	@Override
	public void addButtonArrayLoadingSubscriber(ButtonArrayLoadingNotification baln) 
	{
		loadingNofications.add(baln);
	}
	
	public void setLoadingSpinXmlFile(FileSelection xmlFile)
	{
		this.xmlFile = xmlFile;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void buildLoadingFrame() 
	{
		loadingFrame = new JFrame();//TODO.
		loadingFrame.setResizable(false);
		loadingFrame.setLocation(WidgetBuildController.getInstance().getFrame().getLocation());
		loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setVisible(true);
		
		if(this.isLoadingSpinGraphic)
		{
			loadingFrame.setMinimumSize(new Dimension(150,160));//TODO
			ShapeImportExport sie = new ShapeImportExport();
			ShapeDrawingCollection sdc = new ShapeDrawingCollection();
			LoadingSpin spin = new LoadingSpin(Color.black, Color.white);
			ArrayList<ShapeElement> shapes = (ArrayList<ShapeElement>) sie.openXml(new File(xmlFile.getRelativePath()));
			sdc.addShapeImports(shapes, spin);
			spin.addShapeDrawingCollection(sdc);
			spin.postExecute();
			this.addButtonArrayLoadingSubscriber(spin);
			loadingFrame.add(spin);
		}
		else
		{
			loadingFrame.setMinimumSize(new Dimension(180,70));//TODO
			LoadingLabel label = new LoadingLabel();
			this.addButtonArrayLoadingSubscriber(label);
			loadingFrame.add(label);
		}
	}

	@Override
	public void setIsLoadingSpinGraphic(boolean loadGraphic)
	{
		this.isLoadingSpinGraphic = loadGraphic;
	}

	@Override
	public void openKeeps(HashMap<String, String> props) 
	{
		if(props == null)
			return;
		
		ArrayList<JCheckBoxLimited> cbls = new ArrayList<JCheckBoxLimited>();
		for(String key : props.keySet())
		{
			String val = props.get(key);
			String keyName = key.replaceAll("\\@[0-9]*\\@[0-9]*", "").strip();
			LoggingMessages.printOut("key: " + keyName + " value: " + val);
			for(String k : collectionJButtons.keySet())
			{
				for(JCheckBoxLimited cbl : collectionJButtons.get(k))
				{
					if(PathUtility.getFilenameNoExtension(cbl.getName()).equals(keyName) && cbl.getPathKey().contains(val))
					{
						cbl.setVisible(true);
						cbls.add(cbl);
					}
				}
			}
		}
		for(JCheckBoxLimited cb : cbls)
		{
			String key = cb.getPathKey();
			if(collectionJButtons.get(key).contains(cb))
			{
				collectionJButtons.get(key).remove(cb);
			}
		}
		rebuildButtons(isShowAll());
		
		LoggingMessages.printOut(connectedComponentName);
		AddActionReceive addActionReceive = (AddActionReceive)WidgetBuildController.getInstance().findRefByName(connectedComponentName).getInstance();
		addActionReceive.sendList(cbls);
		
		LoggingMessages.printOut(cbls.size() + "");
		
	}

	@Override
	public void saveKeeps(File saveFile, String [] [] props) 
	{
		if(saveFile == null)
			return;
		
		PathUtility.writeProperties(saveFile.getAbsolutePath(), props);
	}

	@Override
	public Dimension getScaledWidthHeightPreview() 
	{
		return getScaledWidthHeight();
	}

}
