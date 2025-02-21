package ActionListeners;

import java.awt.event.ActionListener;

import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public interface ArrayActionListener 
{
	public abstract void addActionListener(ActionListener actionListener);
	
	public static ArrayActionListener findInstanceOfArrayActionListener()
	{
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			if(wcp.getInstance() instanceof ArrayActionListener)
			{
				return (ArrayActionListener) wcp.getInstance();
			}
		}
		return null;
	}
	
	public abstract void unselect();
	
	public abstract void addStripFilter(String filter);
}
