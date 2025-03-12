package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SendHttpRequestPanel extends JPanel
{
	private static final long serialVersionUID = 3000L;
	
	private static final String SEND_BUTTON_TEXT = "Send Http Get";
	
	private JButton sendButton;
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
		textArea = new JTextArea();
		textArea.setName("UserInput");
		this.add(sendButton);
		this.add(textArea);
	}
	
	private String execute()
	{
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:" + PORT_NUMBER))
				.header("Test", "value")
				.header(textArea.getName(), textArea.getText())
				.method("GET", HttpRequest.BodyPublishers.noBody())
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
