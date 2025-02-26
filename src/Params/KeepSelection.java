package Params;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class KeepSelection
{
	private static final String 
		DEFAULT_IMG = PathUtility.getCurrentDirectory() + "/src/ApplicationBuilder/launch_xsm.png",
		IMAGES_RELATIVE_PATH = "/images/";
	private static final Dimension //TODO
		DIM_DEFAULT_PIC = new Dimension(300,80),
		DIM_PIC = new Dimension(300,470);
	
	private String 
		path,
		text;
	String fileLocation;
	private JFrame frame;
	private BufferedImage 
		img,
		defaultImg;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
		this.fileLocation = PathUtility.getCurrentDirectory() + PathUtility.removeCurrentWorkingDirectoryFromPath(path)  + IMAGES_RELATIVE_PATH + toPngFilename();
		File file = new File(this.fileLocation);
		File fileDefault = new File(DEFAULT_IMG);
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			try {
				defaultImg = ImageIO.read(fileDefault);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
	
	public BufferedImage getImg()
	{
		return img != null
			? img
			: defaultImg;
	}
	
	public Dimension getSize()
	{
		return img != null
			? DIM_PIC
			: DIM_DEFAULT_PIC;
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
