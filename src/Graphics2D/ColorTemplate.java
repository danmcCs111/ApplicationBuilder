package Graphics2D;

import java.awt.Color;

public class ColorTemplate 
{
	private static Color 
		panelBackgroundColor = null,
		deleteBackgroundColor = null,
		deleteForegroundColor = Color.red,
		buttonBackgroundColor = null,
		buttonForegroundColor = null;
	
	public static void setButtonForegroundColor(Color c)
	{
		buttonForegroundColor = c;
	}
	public static void setButtonBackgroundColor(Color c)
	{
		buttonBackgroundColor = c;
	}
	public static void setDeleteForegroundColor(Color c)
	{
		deleteForegroundColor = c;
	}
	public static void setDeleteBackgroundColor(Color c)
	{
		deleteBackgroundColor = c;
	}
	public static void setPanelBackgroundColor(Color c)
	{
		panelBackgroundColor = c;
	}
	
	public static Color getButtonForegroundColor()
	{
		return buttonForegroundColor;
	}
	public static Color getButtonBackgroundColor()
	{
		return buttonBackgroundColor;
	}
	public static Color getDeleteForegroundColor()
	{
		return deleteForegroundColor;
	}
	public static Color getDeleteBackgroundColor()
	{
		return deleteBackgroundColor;
	}
	public static Color getPanelBackgroundColor()
	{
		return panelBackgroundColor;
	}
}
