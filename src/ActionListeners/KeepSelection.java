package ActionListeners;

import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class KeepSelection
{
	private String 
		path,
		text;
	private JFrame frame;
	private ImageMouseAdapter ima;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
	}
	
	public void setFrame(JFrame frame, ImageMouseAdapter ima)
	{
		this.frame = frame;
		this.ima = ima;
//		addListener();
	}
	
//	private void addListener()
//	{
//		frame.addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosed(WindowEvent e) {
//				ima.removeSel(KeepSelection.this);
//			}
//		});
//	}
	
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
}
