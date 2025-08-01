package WidgetComponents;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ObjectTypeConversion.WeatherReading;
import Properties.LoggingMessages;

public class GraphViewer extends JPanel
{
	private static final long serialVersionUID = 1L;

	private HashMap<Date, WeatherReading> readings;
	
	public GraphViewer()
	{
		
	}
	
	public void setReadings(HashMap<Date, WeatherReading> readings)
	{
		this.readings = readings;
	}
	
	public void readingToPlot(String key)
	{
		HashMap<Date, Number> plotPoints = new HashMap<Date, Number>();
		Date 
			dateLow = null,
			dateHigh = null;
		
		for(Date d : readings.keySet())
		{
			WeatherReading wr = readings.get(d);
			Object plotPointValue = wr.getQueryValues().get(key);
			if(!(plotPointValue instanceof Number))
			{
				return; //value not plot point.
			}
			
			plotPoints.put(d, (Number) plotPointValue);
			
			//discover range
			if(dateLow == null || dateLow.after(d))
			{
				dateLow = d;
			}
			if(dateHigh == null || dateHigh.before(d))
			{
				dateHigh = d;
			}
		}
		//Generate graph.
		List<Date> sorted = Arrays.asList(readings.keySet().toArray(new Date[] {}));
		Collections.sort(sorted);
		for(Date d : sorted)
		{
			findX(600, dateHigh, dateLow, d);
		}
	}
	
	public int findX(int panelWidth, Date dateHigh, Date dateLow, Date d)
	{
		int 
			x = 0,
			pad = 10,
			negPad = 20;
		double 
			highTime = dateHigh.getTime(),
			lowTime = dateLow.getTime();
		double time = d.getTime();
		
		panelWidth -= negPad;
		
		time -= lowTime;//get over start point amount.
		double spread = highTime - lowTime;//percentage of spread.
		
		int xPoint = (int)((time / spread) * panelWidth);
		
		xPoint += pad;
		LoggingMessages.printOut("Date: " + d.toString() + "|  x point: " + xPoint);
		
		return x;
	}
}
