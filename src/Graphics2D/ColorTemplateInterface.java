package Graphics2D;

import java.awt.Color;

public interface ColorTemplateInterface 
{
	public static void setDeleteForegroundColor(Color c)
	{
		ColorTemplate.setDeleteForegroundColor(c);
	}
	public static void setDeleteBackgroundColor(Color c)
	{
		ColorTemplate.setDeleteBackgroundColor(c);
	}
	
	public static void setButtonForegroundColor(Color c)
	{
		ColorTemplate.setButtonForegroundColor(c);
	}
	public static void setButtonBackgroundColor(Color c)
	{
		ColorTemplate.setButtonBackgroundColor(c);
	}
	
	public static void setPanelBackgroundColor(Color c)
	{
		ColorTemplate.setPanelBackgroundColor(c);
	}
}
