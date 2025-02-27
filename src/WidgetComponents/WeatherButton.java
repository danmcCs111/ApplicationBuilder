package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.FileSelectionEditor;
import Properties.WeatherParser;
import Properties.PathUtility;

public class WeatherButton extends JPanel
{
	private static final long serialVersionUID = 2020L;
	
	private static final String
		WEATHER_BUTTON_TEXT = "Select Weather",
		KEY_BUTTON_DEFAULT_TEXT = "[Select Key]",
		PROPERTIES_FILE_KEY_NAME_VALUE = "api-key",//value in file in "api-key=<key-value>"
		PROPERTIES_FILE_VALUE_SEPERATOR = "=",
		DIALOG_SELECT_COMPONENT_TITLE = "Call Weather API",
		DIALOG_SELECT_COMPONENT_LABEL_MESSAGE = "Select action";
	
	private static final String [][] ACTIONS = new String [][] {
			{"Alerts", "alerts.json"},
			{"Forecast", "forecast.json"},
			{"Timezone", "timezone.json"}
	};
	private static final List<String> comboSelection = new ArrayList<String>();
	static {
		for(int i = 0; i < ACTIONS.length; i++)
		{
			comboSelection.add(ACTIONS[i][0]);
		}
	}
	private List<WeatherButtonListener> wblListeners = new ArrayList<WeatherButtonListener>();
	private JButton 
		weatherButton,
		keyLocationSelect;
	private JTextField timezoneText;
	private JLabel timezoneLabel;
	private JPanel
		borderPanel,
		buttonPanel,
		timezonePanel;
	
	public WeatherButton()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		buttonPanel = new JPanel();
		timezonePanel = new JPanel();
		borderPanel = new JPanel();
		timezonePanel.setLayout(new GridLayout());
		buttonPanel.setLayout(new GridLayout());
		borderPanel.setLayout(new BorderLayout());
		
		timezoneText = new JTextField();
		timezoneText.setText("london");
		timezoneLabel = new JLabel();
		timezoneLabel.setText("Enter Location: ");
		timezonePanel.add(timezoneLabel);
		timezonePanel.add(timezoneText);
		
		keyLocationSelect = new FileSelectionEditor();
		keyLocationSelect.setText(KEY_BUTTON_DEFAULT_TEXT);
		weatherButton = new JButton();
		weatherButton.setText(WEATHER_BUTTON_TEXT);
		buttonPanel.add(weatherButton);
		buttonPanel.add(keyLocationSelect);
		
		borderPanel.add(buttonPanel, BorderLayout.WEST);
		borderPanel.add(timezonePanel, BorderLayout.EAST);
		
		weatherButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> retRes = new ArrayList<String>();
				if(keyLocationSelect.getText().equals(KEY_BUTTON_DEFAULT_TEXT))
				{
					retRes.add("No Key Selected!");
					notifyListeners(retRes);
					return;
				}
				String ret = showDialog();
				if(ret != null) 
				{
					HashMap<String, String> keyNameValue = PathUtility.readProperties(
							keyLocationSelect.getText(), 
							PROPERTIES_FILE_VALUE_SEPERATOR);
					String keyValue = keyNameValue.get(PROPERTIES_FILE_KEY_NAME_VALUE);
					String output = startWeather(ret, keyValue, timezoneText.getText());
					retRes = WeatherParser.parse(output);
					notifyListeners(retRes);
					return;
				}
			}
		});
		this.add(borderPanel, BorderLayout.NORTH);
	}

	public void setWeatherButtonListener(WeatherButtonListener wbl)
	{
		this.wblListeners.add(wbl);
	}
	
	public void notifyListeners(List<String> results)
	{
		for(WeatherButtonListener wbl : wblListeners)
		{
			wbl.setResults(results);
		}
	}
	
	private String startWeather(String option, String keyValue, String timezone)
	{
		String opt = null;
		for(int i = 0; i < ACTIONS.length; i++)
		{
			if(ACTIONS[i][0].equals(option))
			{
				opt = ACTIONS[i][1];
				break;
			}
		}
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://weatherapi-com.p.rapidapi.com/" + opt + "?q=" + timezone))//using london
				.header("x-rapidapi-key", keyValue)
				.header("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
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
	
	private String showDialog()
	{
		String opt = (String) JOptionPane.showInputDialog(
				this,
				DIALOG_SELECT_COMPONENT_LABEL_MESSAGE, 
				DIALOG_SELECT_COMPONENT_TITLE, 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				comboSelection.toArray(), "");
		return opt;
	}
	
}
