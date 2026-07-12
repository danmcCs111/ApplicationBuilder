package WidgetComponents;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ActionListenersImpl.PageParserSequenceLaunch;
import ObjectTypeConversion.PageParserCollection;
import Properties.DatabaseColumnTypeMap;
import Properties.DatabaseMap;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.TextOutputSubscriber;

public class PageParserUploader extends JPanel implements TextOutputSubscriber, PostWidgetBuildProcessing
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
	private JTextArea
		outputText;
	private JScrollPane
		scrollPane;
	private long
		sleepMillis = 20000l;//20sec default.
	private DatabaseMap
		databaseMap;
	private DatabaseColumnTypeMap 
		databaseColumnTypeMap;
	
	public PageParserUploader()
	{
		this.setLayout(new BorderLayout());
	}
	
	private void buildWidgets()
	{
		collectButton = new JButton(COLLECT_BUTTON_TEXT);
		PageParserSequenceLaunch ppsl = new PageParserSequenceLaunch(
				pageParserCollection, 
				homepage,
				this,
				sleepMillis
		);
		collectButton.addActionListener(ppsl);
		collectButton.setEnabled(false);
		collectButton.setToolTipText("disabled.");
		outputText = new JTextArea();
		scrollPane = new JScrollPane(outputText);
		this.add(collectButton, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
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
	
	public void setSleepMillis(long sleepDurationMillis)
	{
		sleepMillis = sleepDurationMillis;
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
	}

	@Override
	public void textOutput(String text) 
	{
		outputText.setText(outputText.getText() + text);
		outputText.setCaretPosition(outputText.getText().length());
	}
}
