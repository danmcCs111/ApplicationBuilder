package WidgetComponents;

import javax.swing.AbstractButton;
import javax.swing.JMenu;

import WidgetComponentInterfaces.LaunchUrlButton;

public class JMenuLaunchUrl extends JMenu implements LaunchUrlButton 
{
	private static final long serialVersionUID = 1L;
	
	private AbstractButton ab;
	
	public JMenuLaunchUrl(String title) 
	{
		super(title);
	}
	
	public void setHighlightButton(AbstractButton ab)
	{
		this.ab = ab;
	}
	
	@Override
	public AbstractButton getHighlightButton() 
	{
		return ab;
	}
	
}
