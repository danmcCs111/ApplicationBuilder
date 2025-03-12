package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectVideosDatabaseRequest;

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
	private static final String [] REQUEST_OPTIONS = new String [] {SelectVideosDatabaseRequest.SELECT_VIDEOS_SQL_REQUEST};
	
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
				execute();
			}
		});
		
		getType = new JComboBox<String>(GET_HTTP_OPTIONS);
		getRequest = new JComboBox<String>(REQUEST_OPTIONS);
		
		this.add(sendButton);
		this.add(getType);
		this.add(getRequest);
	}
	
	private String execute()
	{
		return HttpDatabaseRequest.executeGetRequest(
				ENDPOINT, 
				PORT_NUMBER, 
				getRequest.getSelectedItem().toString(), 
				REQUEST_TYPE_HEADER_KEY, 
				getType.getSelectedItem().toString()
		);
	}
}
