package WidgetComponents;

import java.util.List;

import javax.swing.JButton;

import Properties.LoggingMessages;
import WidgetExtensionInterfaces.WeatherButtonListenerExtension;

public class WeatherListDisplay extends JButton implements WeatherButtonListenerExtension 
{

	private static final long serialVersionUID = 1L;

	@Override
	public void setResults(List<String> results) 
	{
		for(String result : results)
		{
			LoggingMessages.printOut(result);
		}
	}
	
}
