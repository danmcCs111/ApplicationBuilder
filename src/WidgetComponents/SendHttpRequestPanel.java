package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import Properties.LoggingMessages;

public class SendHttpRequestPanel extends JPanel
{
	private static final long serialVersionUID = 3000L;
	
	private static final String 
		SEND_BUTTON_TEXT = "Send Http Get",
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type";
	private static final int
		PORT_NUMBER = 8000;
	private static final String [] GET_HTTP_OPTIONS = new String [] {"Query"};
	
	private JButton sendButton;
	private JComboBox<String> getType;
	private JComboBox<String> getRequest;

	public SendHttpRequestPanel()
	{
		sendButton = new JButton();
		sendButton.setText(SEND_BUTTON_TEXT);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String responseBody = execute();
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
			}
		});
		
		SelectWebServiceQueries swsq = new SelectWebServiceQueries();
		getType = new JComboBox<String>(GET_HTTP_OPTIONS);
		getRequest = new JComboBox<String>(swsq.getQueryOptions());
		
		this.add(sendButton);
		this.add(getType);
		this.add(getRequest);
	}
	
	private String execute()
	{
		SelectWebServiceQueries swsq = new SelectWebServiceQueries();
		LoggingMessages.printOut(swsq.getQueryOptions());
		
		return HttpDatabaseRequest.executeGetRequest(
				ENDPOINT, 
				PORT_NUMBER, 
				getRequest.getSelectedItem().toString(), 
				REQUEST_TYPE_HEADER_KEY, 
				getType.getSelectedItem().toString()
		);
	}
}
