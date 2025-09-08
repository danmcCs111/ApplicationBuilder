package WidgetComponents;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ActionListeners.ActionListenerSubTypeExtension;
import ActionListeners.ArrayActionListener;
import ActionListeners.ConnectedComponent;
import ActionListenersImpl.NavigationButtonActionListener;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetExtensions.ExtendedStringCollection;
import WidgetUtility.WidgetBuildController;

public class JButtonArrayPicture extends JPanel implements ButtonArray, ArrayActionListener, CharacterLimited, ActionListenerSubTypeExtension
{
	private static final long serialVersionUID = 1L;

	private static final String 
		IMAGES_RELATIVE_PATH = "/images/";

	String fileLocation;
	private Image 
		img;
	private static Image
		defaultImg;
	
	public static String 
		DEFAULT_IMG = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/shapes/Default-Play-Image.xml";
	
	public static Dimension
		DEFAULT_PIC = new Dimension(279, 150),
		SCALED_DEFAULT_PIC = new Dimension(140, 75),
		SCALED_WIDTH_HEIGHT = new Dimension(140, 200);
	
	private static String keepsFileLocation;
	private static HashMap<String, ArrayList<AbstractButton>> collectionJButtons = new HashMap<String, ArrayList<AbstractButton>>();
	private static ArrayList<String> stripFilter = new ArrayList<String>();
	private JButtonArrayPicture connectedComp;
	private int characterLimit=0;
	private int columns = 3;
	
	public JButtonArrayPicture()
	{
		buildWidgets();
	}
	
	public void setDefaultImageXmlPath(FileSelection fs)
	{
		DEFAULT_IMG = fs.getFullPath();
	}
	
	@Override
	public String getDefaultImagePath() 
	{
		return DEFAULT_IMG;
	}
	
	public void setDefaultPic(Dimension widthHeight)
	{
		DEFAULT_PIC = widthHeight;
	}
	
	public void setScaledDefaultPic(Dimension widthHeight)
	{
		SCALED_DEFAULT_PIC = widthHeight;
	}
	
	public void setScaledWidthHeight(Dimension widthHeight)
	{
		SCALED_WIDTH_HEIGHT = widthHeight;
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
		keepsFileLocation = directorySelection.getFullPath();
	}
	
	public void setColumnSize(int columns)
	{
		this.columns = columns;
	}
	
	private void buildWidgets()
	{
		this.setLayout(new GridLayout(0, this.columns));
	}
	
	public Image getImg()
	{
		if(img == null)
		{
			setupImage(new File(this.fileLocation), new File(JButtonArray.DEFAULT_IMG));
		}
		return img != null
			? img
			: defaultImg;
	}
	
	private void clearJButtons()
	{
		this.removeAll();
	}

	@Override
	public void addJButtons(String path, List<String> listOf, int index) 
	{
		LoggingMessages.printOut("load buttons." + listOf.size() + " " + index);
		ArrayList<AbstractButton> jbuts = new ArrayList<AbstractButton>();
		
		clearJButtons();
		
		if(!SwappableCollection.indexPaths.contains(path))
		{
			for(Component comp : buildComponents(path, listOf))
			{
				if(comp instanceof Component)
				{
					if(comp instanceof JCheckBoxLimited)
					{
						String txt = ((JCheckBoxLimited) comp).getFullLengthText();
						
						for(String s : stripFilter)
						{
							txt = txt.replace(s, "");
						}
						if(characterLimit != 0)
						{
							((JCheckBoxLimited) comp).setCharacterLimit(characterLimit);
						}
						((JCheckBoxLimited) comp).setText(txt);
					}
					jbuts.add((AbstractButton) comp);
					this.add(comp);
				}
			}
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
	
	public List<JComponent> buildComponents(String path, List<String> fileNames)
	{
		List<JComponent> components = new ArrayList<JComponent>();
		{
			for(String fileName: fileNames)
			{
				JCheckBoxLimited button = new JCheckBoxLimited();
				String fileImage = PathUtility.getCurrentDirectory()+PathUtility.removeCurrentWorkingDirectoryFromPath(path)+IMAGES_RELATIVE_PATH+fileName.replaceAll(".url", ".png");
				LoggingMessages.printOut(fileImage);
				Image img = setupImage(new File(fileImage), new File(DEFAULT_IMG));
				button.setIcon(new ImageIcon(img));
				button.setText(fileName);
				button.setToolTipText(fileName);
				button.setBorderPainted(true);
//				button.setName(UrlToValueReader.parse(fileName, path));
				components.add(button);
			}
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
		int indexPos = NavigationButtonActionListener.getCurPosition();
		clearJButtons();
		for(AbstractButton ab : collectionJButtons.get(SwappableCollection.indexPaths.get(indexPos)))
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
	public void setActionListenerSubTypeExtension(Class<?> clazz, String type) 
	{
		//TODO
	}

	@Override
	public void setConnectedComp(ConnectedComponent comp) 
	{
		ConnectedComponent connectedComp = comp;//TODO
		
	}

}
