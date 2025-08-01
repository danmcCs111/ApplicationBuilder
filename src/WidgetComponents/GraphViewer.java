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
	
	public int findX(int panelWidth, Date dateHigh, Date dateLow, Date d)
	{
		int 
			xPoint = 0,
			pad = 10,
			negPad = 20;
		double 
			highTime = dateHigh.getTime(),
			lowTime = dateLow.getTime();
		double time = d.getTime();
		
		panelWidth -= negPad;
		
		time -= lowTime;//get over start point amount.
		double spread = highTime - lowTime;//percentage of spread.
		
		xPoint = (int)((time / spread) * panelWidth);
		
		xPoint += pad;
		LoggingMessages.printOut("Date: " + d.toString() + "|  x point: " + xPoint);
		
		return xPoint;
	}
	
	public int findY(int panelHeight, Number numberHigh, Number numberLow, Number num)
	{
		int 
			yPoint = 0,
			pad = 10,
			negPad = 20;
		double 
			highNumber = numberHigh.doubleValue(),
			lowNumber = numberLow.doubleValue();
		double number = num.doubleValue();
		
		panelHeight -= negPad;
		
		number -= lowNumber;//get over start point amount.
		double spread = highNumber - lowNumber;//percentage of spread.
		
		
		yPoint = (int)((number / spread) * panelHeight);
		
		yPoint += pad;
		LoggingMessages.printOut("Value: " + num.toString() + "|  y point: " + yPoint);
		
		return yPoint;
	}
}
