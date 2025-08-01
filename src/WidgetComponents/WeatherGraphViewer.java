package WidgetComponents;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ObjectTypeConversion.WeatherReading;

public class WeatherGraphViewer extends GraphViewer 
{
	private static final long serialVersionUID = 1L;
	private HashMap<Date, WeatherReading> readings;
	
	public WeatherGraphViewer()
	{
		
	}
	
	public void setReadings(HashMap<Date, WeatherReading> readings)
	{
		this.readings = readings;
	}
	
	public void readingToPlot(String key)
	{
		HashMap<Date, Number> plotPoints = new HashMap<Date, Number>();
		Number 
			numberLow = null,
			numberHigh = null;
		
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
			if(numberLow == null || numberLow.doubleValue() > ((Number)plotPointValue).doubleValue())
			{
				numberLow = (Number)plotPointValue;
			}
			if(numberHigh == null || numberHigh.doubleValue() < ((Number)plotPointValue).doubleValue())
			{
				numberHigh = (Number)plotPointValue;
			}
		}
		//Generate graph.
		List<Date> sorted = Arrays.asList(readings.keySet().toArray(new Date[] {}));
		Collections.sort(sorted);
		for(Date d : sorted)
		{
			findX(600, sorted.get(sorted.size()-1), sorted.get(0), d);
			findY(600, numberHigh, numberLow, Integer.parseInt(""+readings.get(d).temperature));//Test.
		}
	}

}
