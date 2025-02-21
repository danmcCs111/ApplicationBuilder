package ActionListeners;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class KeepSelection
{
	private String 
		path,
		text;
	private JFrame frame;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
	}
	
	public void setFrame(JFrame frame)
	{
		this.frame = frame;
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
