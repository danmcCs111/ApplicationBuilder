package WidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ObjectTypeConversion.WeatherReading;
import ShapeWidgetComponents.ShapeDrawingCollectionGraphics;

public class WeatherGraphViewer extends GraphViewer 
{
	private static final long serialVersionUID = 1L;
	private HashMap<Date, WeatherReading> readings;
	private HashMap<Date, Point> xYPoints;
	
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
		int 
			height = 600,//this.getHeight();
			width = 600;//this.getWidth();
		this.xYPoints = new HashMap<Date, Point>();
		List<Date> sorted = Arrays.asList(readings.keySet().toArray(new Date[] {}));
		Collections.sort(sorted);
		
		for(Date d : sorted)
		{
			Point p = new Point();
			p.x = findX(width, sorted.get(sorted.size()-1), sorted.get(0), d);
			p.y = findY(height, numberHigh, numberLow, Integer.parseInt(""+readings.get(d).temperature));//Test.
			this.xYPoints.put(d, p);
		}
		visualize(xYPoints, key);
	}
	
	private void visualize(HashMap<Date, Point> xYPoints, String key)
	{
		int plotsize = 6;
		
		ShapeDrawingCollectionGraphics.clearAll(this);
		for(Date d : xYPoints.keySet())
		{
			Point p = xYPoints.get(d);
			Ellipse2D ellipse = new Ellipse2D.Double(
					p.x- (plotsize / 2), 
					p.y- (plotsize / 2), 
					plotsize, 
					plotsize);
			ShapeDrawingCollectionGraphics.drawShape(this, ellipse, Color.blue);
		}
	}

}
