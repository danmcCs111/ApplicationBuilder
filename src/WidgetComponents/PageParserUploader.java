package WidgetComponents;

import javax.swing.JButton;
import javax.swing.JPanel;

import ActionListenersImpl.PageParserSequenceLaunch;
import ObjectTypeConversion.PageParserCollection;
import Properties.DatabaseColumnTypeMap;
import Properties.DatabaseMap;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class PageParserUploader extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		COLLECT_BUTTON_TEXT = "Collect";
	
	private String
		homepage = null;
	private PageParserCollection 
		pageParserCollection = null;
	private JButton
		collectButton;
	private DatabaseMap
		databaseMap;
	private DatabaseColumnTypeMap 
		databaseColumnTypeMap;
	
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
	
	public void setPageParserCollection(PageParserCollection pp)
	{
		pageParserCollection = pp;
	}
	
	public void setHomePage(String homepage)
	{
		this.homepage = homepage;
	}
	
	public void setDatabaseMap(DatabaseMap databaseMap)
	{
		this.databaseMap = databaseMap;
	}
	
	public void setDatabaseColumnTypeMap(DatabaseColumnTypeMap databaseColumnTypeMap)
	{
		this.databaseColumnTypeMap = databaseColumnTypeMap;
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
	}
}
