package WidgetComponents;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import WidgetComponentInterfaces.LaunchUrlButton;

public class JMenuItemLaunchUrl extends JMenuItem implements LaunchUrlButton 
{
	private static final long serialVersionUID = 1L;
	
	private AbstractButton ab;
	private String reqText = "";
	
	public JMenuItemLaunchUrl(String title) 
	{
		super(title);
	}
	
	public void setReqText(String text)
	{
		this.reqText = text;
	}
	
	public String getReqText()
	{
		return this.reqText;
	}

	public void setHighlightButton(AbstractButton ab)
	{
		this.ab = ab;
	}
	
	@Override
	public AbstractButton getHighlightButton() 
	{
		return this.ab;
	}

}
