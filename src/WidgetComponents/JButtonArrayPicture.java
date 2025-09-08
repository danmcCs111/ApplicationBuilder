package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ActionListeners.ArrayActionListener;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.CharacterLimited;
import WidgetExtensions.ExtendedStringCollection;
import WidgetUtility.WidgetBuildController;

public class JButtonArrayPicture extends JPanel implements ButtonArray, ArrayActionListener, CharacterLimited
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
		DIM_DEFAULT_PIC = new Dimension(280,150),
		PIC_PAD = new Dimension(5,	20),
		SCALED_DEFAULT_PIC = new Dimension(140, 75),
		SCALED_WIDTH_HEIGHT = new Dimension(140, 200),
		DIM_PIC = new Dimension(
				JButtonArray.SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				JButtonArray.SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	
	private static String keepsFileLocation;
	private static HashMap<String, ArrayList<AbstractButton>> collectionJButtons = new HashMap<String, ArrayList<AbstractButton>>();
	private static ArrayList<String> stripFilter = new ArrayList<String>();
	private int characterLimit=0;
	
	public JButtonArrayPicture()
	{
		buildWidgets();
	}
	
	public void setDefaultImageXmlPath(FileSelection fs)
	{
		DEFAULT_IMG = fs.getFullPath();
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
	
	public void setScaledDefaultPic(Dimension widthHeight)
	{
		SCALED_DEFAULT_PIC = widthHeight;
	}
	
	public void setPicturePadWidthHeight(Dimension widthHeight)
	{
		PIC_PAD = widthHeight;
		DIM_PIC = new Dimension(
				JButtonArray.SCALED_WIDTH_HEIGHT.width + PIC_PAD.width,
				JButtonArray.SCALED_WIDTH_HEIGHT.height + PIC_PAD.height);
	}
	
	public void setExpandedArrangementFileRelativeLocation(DirectorySelection directorySelection)
	{
		keepsFileLocation = directorySelection.getFullPath();
	}
	
	private void buildWidgets()
	{
		this.setLayout(new GridLayout(0, 3));
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
	
	private static Image getDefaultImage(File defaultImageLocation)
	{
		Image retImage = null;
		Image tmpImage = GraphicsUtil.getImageFromXml(
				JButtonArray.DIM_DEFAULT_PIC.width, JButtonArray.DIM_DEFAULT_PIC.height, defaultImageLocation, Color.black);
		retImage = tmpImage.getScaledInstance(
				SCALED_DEFAULT_PIC.width, 
				SCALED_DEFAULT_PIC.height, 0);
		return retImage;
	}
	
	private static Image setupImage(File file, File fileDefault)
	{
		Image retImage = null;
		try {
			Image tmpImage = ImageIO.read(file);
			retImage = tmpImage.getScaledInstance(
					SCALED_WIDTH_HEIGHT.width, 
					SCALED_WIDTH_HEIGHT.height, 0);
		} catch (IOException e) {
			retImage = getDefaultImage(fileDefault);
		}
		return retImage;
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
		SwappableCollection.indexPos = index;
		
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
	
	public static List<JComponent> buildComponents(String path, List<String> fileNames)
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
		clearJButtons();
		for(AbstractButton ab : collectionJButtons.get(SwappableCollection.indexPaths.get(SwappableCollection.indexPos)))
		{
			if(ab.isVisible())
			{
				this.add(ab);
			}
		}
		JFrame f = WidgetBuildController.getInstance().getFrame();
		f.paintComponents(f.getGraphics());
	}
	
}
