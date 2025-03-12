package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SendHttpRequestPanel extends JPanel
{
	private static final long serialVersionUID = 3000L;
	
	private static final String 
		SEND_BUTTON_TEXT = "Send Http Get",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type";
	
	private static final String [] GET_HTTP_OPTIONS = new String [] {"Query"};
	
	private JButton sendButton;
	private JComboBox<String> getType;
	private JTextArea textArea;
	
	private static int
		PORT_NUMBER = 8000;

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
		
		textArea = new JTextArea();
		this.add(sendButton);
		this.add(getType);
		this.add(textArea);
	}
	
	private String execute()
	{
		BodyPublisher bp = HttpRequest.BodyPublishers.ofString(textArea.getText());
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + PORT_NUMBER))
				.header("Test", "value")
				.header(REQUEST_TYPE_HEADER_KEY, getType.getSelectedItem().toString())
				.method("GET", bp)
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return response==null
				? null
				: response.body();
	}
}
