package Params;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Properties.PathUtility;
import WidgetComponents.JButtonArray;

public class KeepSelection
{
	private static final String 
		DEFAULT_IMG = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/launch_xsm.png",
		IMAGES_RELATIVE_PATH = "/images/";
	
	private String 
		path,
		text;
	String fileLocation;
	private JFrame frame;
	private Image 
		img,
		defaultImg;
	public static boolean skip = false;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
		this.fileLocation = PathUtility.getCurrentDirectory() + 
				PathUtility.removeCurrentWorkingDirectoryFromPath(path)  + 
				IMAGES_RELATIVE_PATH + toPngFilename();
		File file = new File(this.fileLocation);
		File fileDefault = new File(DEFAULT_IMG);
		try {
			if(!skip)
			{
				Image tmpImg = ImageIO.read(file);
				img = tmpImg.getScaledInstance(
						JButtonArray.SCALED_WIDTH_HEIGHT.width, 
						JButtonArray.SCALED_WIDTH_HEIGHT.height, 0);
			}
		} catch (IOException e) {
			//
		}
		try {
			defaultImg = ImageIO.read(fileDefault);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	public JFrame getFrame()
	{
		return this.frame;
	}
	
	public Point getLocationPoint()
	{
		return this.frame.getLocation();
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public Image getImg()
	{
		return img != null
			? img
			: defaultImg;
	}
	
	public Dimension getSize()
	{
		return img != null
			? JButtonArray.DIM_PIC
			: JButtonArray.DIM_DEFAULT_PIC;
	}
	
	public String toPngFilename()
	{
		return this.text + ".png";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof KeepSelection)
		{
			if(((KeepSelection) obj).getPath().equals(this.getPath()) && 
					((KeepSelection) obj).getText().equals(this.getText()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return this.path + " " + this.text;
	}
	
	public static ArrayList<String> getTextOnlyConversion (List<KeepSelection> ks)
	{
		ArrayList<String> convList = new ArrayList<String>();
		for(KeepSelection k : ks)
		{
			convList.add(k.getText());
		}
		return convList;
	}
}
