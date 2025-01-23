package ActionListeners;

import java.awt.Container;

public class WidthAndHeightWidgetProp 
{
	private Container parent;
	private String text;
	
	public WidthAndHeightWidgetProp(Container parent, String text)
	{
		this.parent = parent;
		this.text = text;
	}
	
	public Container getParent()
	{
		return parent;
	}
	
	public String getText()
	{
		return text;
	}
}
