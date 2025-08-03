package WidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComboBox;

import ActionListeners.CsvReaderSubscriber;
import ObjectTypeConversion.CsvReader;
import ObjectTypeConversion.WeatherGrabCsvConverter;
import ObjectTypeConversion.WeatherReading;
import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeDrawingCollectionGraphics;

public class WeatherGraphViewer extends GraphViewer implements CsvReaderSubscriber
{
	private static final long serialVersionUID = 1L;
	
	private static final int 
		PAD = 85,
		PLOT_SIZE = 4,
		LABEL_MARKER_LEN = 10;
	
	private static final SimpleDateFormat 
		SDF_TIMELINE = new SimpleDateFormat("MMM, dd: h a"),
		SDF_TOOLTIP = new SimpleDateFormat("MMM, dd: h a");
	
	private HashMap<Date, WeatherReading> readings;
	private HashMap<Date, Point> xYPoints;
	
	private JComboBox<String> comboSelect = new JComboBox<String>();
	
	public WeatherGraphViewer()
	{
		comboSelect.addItem("Temperature");
		comboSelect.addItem("Rain");
		comboSelect.addItem("Thunder");
		comboSelect.addItem("Precipitation Potential");
		comboSelect.addItem("Relative Humidity");
		comboSelect.addItem("Sky Cover");
		comboSelect.addItem("Heat Index");
		comboSelect.addItem("Dewpoint");
		this.add(comboSelect);
	}
	
	public void setReadings(HashMap<Date, WeatherReading> readings)
	{
		this.readings = readings;
	}
	
	public void plotReading(String key)
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
			height = this.getHeight(),
			width = this.getWidth();
		this.xYPoints = new HashMap<Date, Point>();
		List<Date> sorted = Arrays.asList(readings.keySet().toArray(new Date[] {}));
		Collections.sort(sorted);
		
		for(Date d : sorted)
		{
			Point p = new Point();
			p.x = findX(width, PAD, sorted.get(sorted.size()-1), sorted.get(0), d);
			p.y = findY(height, PAD, numberHigh, numberLow, Integer.parseInt(""+readings.get(d).getQueryValues().get(key)));//Test.
			this.xYPoints.put(d, p);
		}
		visualize(xYPoints, plotPoints, key);
	}
	
	private void visualize(HashMap<Date, Point> xYPoints, HashMap<Date, Number> plotPoints, String key)
	{
		ShapeDrawingCollectionGraphics.clearAll(this);
		for(Date d : xYPoints.keySet())
		{
			Point p = xYPoints.get(d);
			Ellipse2D ellipse = new Ellipse2D.Double(
					p.x - (PLOT_SIZE / 2), 
					p.y - (PLOT_SIZE / 2), 
					PLOT_SIZE, 
					PLOT_SIZE
			);
			ShapeDrawingCollectionGraphics.drawShape(this, ellipse, Color.blue);
			
			visualizeXBorder(xYPoints, PAD-(PLOT_SIZE/2));
			visualizeYBorder(plotPoints, PAD-(PLOT_SIZE/2));
			WeatherGraphMouseMotionListener wgListener = new WeatherGraphMouseMotionListener(
					xYPoints,
					plotPoints, 
					PAD,
					this.getWidth() - PAD,
					PAD,
					this.getHeight() - PAD,
					SDF_TOOLTIP,
					this);
			for(MouseMotionListener ml : this.getMouseMotionListeners())
			{
				this.removeMouseMotionListener(ml);
			}
			this.addMouseMotionListener(wgListener);
		}
	}
	
	private void visualizeXBorder(HashMap<Date, Point> xYPoints, int pad)
	{
		List<Date> dates = (List<Date>) Arrays.asList(xYPoints.keySet().toArray(new Date[] {}));
		Collections.sort(dates);
		Date dateLow = dates.get(0);
		Date dateHigh = dates.get(dates.size()-1);
		
		Line2D xBorder = new Line2D.Double(pad, this.getSize().getHeight()-pad, this.getSize().getWidth()-pad, this.getSize().getHeight()-pad);
		ShapeDrawingCollectionGraphics.drawShape(this, xBorder, Color.gray);
		
		Line2D xLabelLow = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				pad, this.getSize().getHeight()-pad+LABEL_MARKER_LEN);
		ShapeDrawingCollectionGraphics.drawShape(this, xLabelLow, Color.gray);
		Line2D xLabelHigh = new Line2D.Double(
				this.getSize().getWidth()-pad, this.getSize().getHeight()-pad, 
				this.getSize().getWidth()-pad, this.getSize().getHeight()-pad+LABEL_MARKER_LEN);
		ShapeDrawingCollectionGraphics.drawShape(this, xLabelHigh, Color.gray);
		
		Point lowPoint = new Point(pad, (int)this.getSize().getHeight()-pad+(LABEL_MARKER_LEN*2));
		Point highPoint = new Point((int)this.getSize().getWidth()-pad, (int)this.getSize().getHeight()-pad+(LABEL_MARKER_LEN*2));
		
		String strHigh = SDF_TIMELINE.format(dateHigh);
		String strLow = SDF_TIMELINE.format(dateLow);
		
		ShapeDrawingCollectionGraphics.drawGlyph(this, strLow + "", lowPoint, getFont(), Color.gray);
		ShapeDrawingCollectionGraphics.drawGlyph(this, strHigh + "", highPoint, getFont(), Color.gray);
	}
	
	private void visualizeYBorder(HashMap<Date, Number> xYPoints, int pad)
	{
		List<Integer> values = (List<Integer>) Arrays.asList(xYPoints.values().toArray(new Integer[] {}));
		Collections.sort(values);
		int valueLow = values.get(0);
		int valueHigh = values.get(values.size()-1);
		
		Line2D yBorder = new Line2D.Double(pad, this.getSize().getHeight()-pad, pad, pad);
		ShapeDrawingCollectionGraphics.drawShape(this, yBorder, Color.gray);
		
		Line2D yLabelLow = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				pad-LABEL_MARKER_LEN, this.getSize().getHeight()-pad);
		ShapeDrawingCollectionGraphics.drawShape(this, yLabelLow, Color.gray);
		Line2D yLabelHigh = new Line2D.Double(
				pad, pad, 
				pad-LABEL_MARKER_LEN, pad);
		ShapeDrawingCollectionGraphics.drawShape(this, yLabelHigh, Color.gray);
		
		Point lowPoint = new Point(pad-(LABEL_MARKER_LEN * 2), (int) (this.getSize().getHeight()-pad));
		Point highPoint = new Point(pad-(LABEL_MARKER_LEN * 2), pad);
		
		ShapeDrawingCollectionGraphics.drawGlyph(this, valueLow + "", lowPoint, getFont(), Color.gray);
		ShapeDrawingCollectionGraphics.drawGlyph(this, valueHigh + "", highPoint, getFont(), Color.gray);
	}

	@Override
	public void notify(CsvReader csvReader) 
	{
		LoggingMessages.printOut("notify.");
		WeatherGrabCsvConverter wgc = new WeatherGrabCsvConverter(csvReader);
		setReadings(wgc.getWeatherReadings());//?
		plotReading((String) comboSelect.getSelectedItem());
	}

}
