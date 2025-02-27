package WidgetComponents;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Properties.LoggingMessages;
import WidgetExtensions.WeatherButtonListenerExtension;

public class OutputWeatherResultsTextArea extends JPanel implements WeatherButtonListenerExtension
{

	private static final long serialVersionUID = 2021L;
	
	private JScrollPane jsP;
	private JTextArea outputWeatherResults;
	
	public OutputWeatherResultsTextArea()
	{
		this.setLayout(new BorderLayout());
		outputWeatherResults = new JTextArea();
		outputWeatherResults.setLineWrap(true);
		outputWeatherResults.setWrapStyleWord(true);
		jsP = new JScrollPane(outputWeatherResults);
		this.add(jsP, BorderLayout.CENTER);
	}

	@Override
	public void setResults(List<String> results) 
	{
		outputWeatherResults.setText(
			(results == null)
			? "API Failure!"
			: LoggingMessages.combine("\n\n", results)
		);
	}

}
