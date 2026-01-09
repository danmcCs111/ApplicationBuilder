package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.DatabaseSelection;
import HttpDatabaseResponse.HttpDatabaseResponse;
import ObjectTypeConversion.FileSelection;
import ObjectTypeConversionEditors.FileSelectionEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensionInterfaces.DatabaseResponseNodeListenerExtension;

public class SendHttpRequestPanel extends JPanel implements PostWidgetBuildProcessing, DatabaseSelection
{
	private static final long serialVersionUID = 3000L;
	
	private static final String 
		SEND_BUTTON_TEXT = "Execute",
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		DATABASE = "WeatherDatabase";
	private static final int
		PORT_NUMBER = 8000;
	private static final String [] 
		GET_HTTP_OPTIONS = new String [] {"Query"};
	
	private String 
		database = DATABASE;
	private JButton 
		sendButton,
		fileOpenAndSend;
	private JComboBox<String> 
		getType,
		getRequest;
	private JPanel 
		innerQsPanel;
	private SelectWebServiceQueries 
		swsq;
	private ArrayList<DatabaseResponseNodeListenerExtension> 
		drnleList = new ArrayList<DatabaseResponseNodeListenerExtension>();

	public SendHttpRequestPanel()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		this.setLayout(new GridLayout(0,1));
		
		JPanel qsPanel = buildQuerySelectionPanel();
		JPanel fsPanel = buildFilePanel();
		
		this.add(qsPanel);
		this.add(fsPanel);
	}
	
	private JPanel buildQuerySelectionPanel()
	{
		JPanel  qsPanel = new JPanel();
		innerQsPanel = new JPanel();
		qsPanel.setLayout(new BorderLayout());
		
		sendButton = new JButton();
		sendButton.setText(SEND_BUTTON_TEXT);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				populateResponse(getRequest.getSelectedItem().toString());
			}
		});
		
		getType = new JComboBox<String>(GET_HTTP_OPTIONS);
		getRequest = new JComboBox<String>();
		
		innerQsPanel.add(sendButton);
		innerQsPanel.add(getType);
		innerQsPanel.add(getRequest);
		
		qsPanel.add(innerQsPanel, BorderLayout.EAST);
		
		return qsPanel;
	}
	
	private JPanel buildFilePanel()
	{
		JPanel fileSelectionPanel = new JPanel();
		FileSelectionEditor fse = new FileSelectionEditor();
		fse.setComponentValue(new FileSelection(""));
		fileOpenAndSend = new JButton(SEND_BUTTON_TEXT);
		
		fileOpenAndSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File(((FileSelection)fse.getComponentValueObj()).getPathLinux());
				createResponseFromFile(f);
			}
		});
		
		fileSelectionPanel.add(fileOpenAndSend);
		fileSelectionPanel.add(fse);
		
		return fileSelectionPanel;
	}
	
	public void setDatabase(String database)
	{
		this.database = database;
	}
	
	private String executeRequest(String request)
	{
		return HttpDatabaseRequest.executeGetRequest(
				ENDPOINT, 
				PORT_NUMBER, 
				request, 
				REQUEST_TYPE_HEADER_KEY, 
				getType.getSelectedItem().toString()
		);
	}
	
	private void createResponseFromFile(File f)
	{
		String request = PathUtility.readFileToString(f);
		populateResponse(request);
	}
	
	private void populateResponse(String request)
	{
		String responseBody = executeRequest(request);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList<ArrayList<DatabaseResponseNode>> responseNodes = hdr.parseResponse(responseBody);
		
		for(ArrayList<DatabaseResponseNode> drns : responseNodes)
		{
			LoggingMessages.printOut("");
			for(DatabaseResponseNode drn : drns)
			{
				LoggingMessages.printOut(drn.toString());
			}
		}
		for(DatabaseResponseNodeListenerExtension drnl : drnleList)
		{
			drnl.setResults(responseNodes);
		}
	}

	@Override
	public void postExecute() 
	{
		if(swsq == null)
		{
			swsq = new SelectWebServiceQueries(getDatabase());
		}
		innerQsPanel.remove(getRequest);
		getRequest = new JComboBox<String>(swsq.getQueryOptions());
		innerQsPanel.add(getRequest);
		this.getRootPane().validate();
	}

	public void setDatabaseResponseNodeListener(DatabaseResponseNodeListenerExtension drnle) 
	{
		this.drnleList.add(drnle);
	}

	@Override
	public String getDatabase() 
	{
		return database;
	}
}
