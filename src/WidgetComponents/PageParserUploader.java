package WidgetComponents;

import javax.swing.JButton;
import javax.swing.JPanel;

import ActionListenersImpl.PageParserSequenceLaunch;
import ObjectTypeConversion.PageParserCollection;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class PageParserUploader extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		COLLECT_BUTTON_TEXT = "Collect";
	
	private static String
		homepage = null;
	private static PageParserCollection 
		pageParserCollection = null;
	private JButton
		collectButton;
	
	public PageParserUploader()
	{
		
	}
	
	private void buildWidgets()
	{
		collectButton = new JButton(COLLECT_BUTTON_TEXT);
		PageParserSequenceLaunch ppsl = new PageParserSequenceLaunch(
				pageParserCollection, 
				homepage
		);
		collectButton.addActionListener(ppsl);
		this.add(collectButton);
	}
	
	public static void setPageParserCollection(PageParserCollection pp)
	{
		pageParserCollection = pp;
	}
	
	public static void setHomePage(String homepage)
	{
		PageParserUploader.homepage = homepage;
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
	}
}
