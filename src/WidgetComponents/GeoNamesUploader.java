package WidgetComponents;

import javax.swing.JPanel;

import ObjectTypeConversion.PageParser;

public class GeoNamesUploader extends JPanel 
{
	private static final long serialVersionUID = 1L;
	
	private static PageParser 
		pageParser = null;

	
	public GeoNamesUploader()
	{
		
	}
	
	public static void setPageParser(PageParser pp)
	{
		pageParser = pp;
	}
}
