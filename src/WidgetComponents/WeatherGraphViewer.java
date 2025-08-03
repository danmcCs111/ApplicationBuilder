package WidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import ActionListeners.CsvReaderSubscriber;
import ObjectTypeConversion.CsvReader;
import ObjectTypeConversion.WeatherGrabCsvConverter;
import ObjectTypeConversion.WeatherReading;
import Properties.LoggingMessages;
import ShapeWidgetComponents.GlyphDrawingCollection;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeDrawingCollectionGraphics;
import ShapeWidgetComponents.ShapeStyling;

public class WeatherGraphViewer extends GraphViewer implements CsvReaderSubscriber
{
	private static final long serialVersionUID = 1L;
	
	private static final int 
		PAD = 85,
		PLOT_SIZE = 4,
		LABEL_MARKER_LEN = 10;
	private int
		pad = PAD,
		plotSize = PLOT_SIZE,
		labelMarkerLen = LABEL_MARKER_LEN;
	
	private static final Color 
		PLOT_COLOR = Color.blue,
		TIMELINE_COLOR = Color.gray,
		TIMELINE_LABEL_COLOR = Color.gray;
	private Color
		plotColor = PLOT_COLOR,
		timeLineColor = TIMELINE_COLOR,
		timeLineLabelColor = TIMELINE_LABEL_COLOR;
	
	private static final SimpleDateFormat 
		SDF_TIMELINE = new SimpleDateFormat("MMM, dd: h a"),//TODO converter
		SDF_TOOLTIP = new SimpleDateFormat("MMM, dd: h a");
	
	private HashMap<Date, WeatherReading> readings;
	private HashMap<Date, Point> xYPoints;
	
	private JComboBox<String> comboSelect = new JComboBox<String>();
	
	private ShapeDrawingCollection shapeDrawingCollection;
	private GlyphDrawingCollection glyphDrawingCollection;
	
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
		JButton plotButton = new JButton("Plot");
		plotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				plotReading((String) comboSelect.getSelectedItem());
			}
		});
		this.add(comboSelect);
		this.add(plotButton);
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
		shapeDrawingCollection = new ShapeDrawingCollection();
		glyphDrawingCollection = new GlyphDrawingCollection();
		visualize(xYPoints, plotPoints, key);
	}
	
	private void visualize(HashMap<Date, Point> xYPoints, HashMap<Date, Number> plotPoints, String key)
	{
		ShapeDrawingCollectionGraphics.clearAll(this);
		for(Date d : xYPoints.keySet())
		{
			Point p = xYPoints.get(d);
			Ellipse2D ellipse = new Ellipse2D.Double(
					p.x - (plotSize / 2), 
					p.y - (plotSize / 2), 
					plotSize, 
					plotSize
			);
			addShape(ellipse, plotColor);
			
			visualizeXBorder(xYPoints, pad-(plotSize/2));
			visualizeYBorder(plotPoints, pad-(plotSize/2));
			WeatherGraphMouseMotionListener wgListener = new WeatherGraphMouseMotionListener(
					xYPoints,
					plotPoints, 
					pad,
					this.getWidth() - pad,
					pad,
					this.getHeight() - pad,
					SDF_TOOLTIP,
					this);
			for(MouseMotionListener ml : this.getMouseMotionListeners())
			{
				this.removeMouseMotionListener(ml);
			}
			this.addMouseMotionListener(wgListener);
		}
		draw();
	}
	
	private void visualizeXBorder(HashMap<Date, Point> xYPoints, int pad)
	{
		List<Date> dates = (List<Date>) Arrays.asList(xYPoints.keySet().toArray(new Date[] {}));
		Collections.sort(dates);
		Date dateLow = dates.get(0);
		Date dateHigh = dates.get(dates.size()-1);
		
		Line2D xBorder = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				this.getSize().getWidth()-pad, this.getSize().getHeight()-pad);
		
		Line2D xLabelLow = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				pad, this.getSize().getHeight()-pad+labelMarkerLen);
		
		Line2D xLabelHigh = new Line2D.Double(
				this.getSize().getWidth()-pad, this.getSize().getHeight()-pad, 
				this.getSize().getWidth()-pad, this.getSize().getHeight()-pad+labelMarkerLen);
		
		addShape(xBorder, timeLineColor);
		addShape(xLabelLow, timeLineColor);
		addShape(xLabelHigh, timeLineColor);
		
		Point lowPoint = new Point(pad, (int)this.getSize().getHeight()-pad+(labelMarkerLen*2));
		Point highPoint = new Point((int)this.getSize().getWidth()-pad, (int)this.getSize().getHeight()-pad+(labelMarkerLen*2));
		
		String strHigh = SDF_TIMELINE.format(dateHigh);
		String strLow = SDF_TIMELINE.format(dateLow);
		
		addGlyph(strLow, lowPoint, timeLineLabelColor);
		addGlyph(strHigh, highPoint, timeLineLabelColor);
	}
	
	private void visualizeYBorder(HashMap<Date, Number> xYPoints, int pad)
	{
		List<Integer> values = (List<Integer>) Arrays.asList(xYPoints.values().toArray(new Integer[] {}));
		Collections.sort(values);
		int valueLow = values.get(0);
		int valueHigh = values.get(values.size()-1);
		
		Line2D yBorder = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				pad, pad);
		Line2D yLabelLow = new Line2D.Double(
				pad, this.getSize().getHeight()-pad, 
				pad-labelMarkerLen, this.getSize().getHeight()-pad);
		Line2D yLabelHigh = new Line2D.Double(
				pad, pad, 
				pad-labelMarkerLen, pad);
		
		addShape(yBorder, timeLineColor);
		addShape(yLabelLow, timeLineColor);
		addShape(yLabelHigh, timeLineColor);
		
		Point lowPoint = new Point(pad-(labelMarkerLen * 2), (int) (this.getSize().getHeight()-pad));
		Point highPoint = new Point(pad-(labelMarkerLen * 2), pad);
		
		addGlyph(valueLow + "", lowPoint, timeLineLabelColor);
		addGlyph(valueHigh + "", highPoint, timeLineLabelColor);
	}
	
	public void draw()
	{
		for(int i = 0; i < shapeDrawingCollection.getShapeStylings().size(); i++)
		{
			ShapeStyling ss = shapeDrawingCollection.getShapeStylings().get(i);
			Shape s = shapeDrawingCollection.getShapes().get(i);
			ShapeDrawingCollectionGraphics.drawShape(this, s, ss.getDrawColor());
		}
		for(Point p : glyphDrawingCollection.getGlyphs().keySet())
		{
			String glyph = glyphDrawingCollection.getGlyphs().get(p);
			ShapeStyling ss = glyphDrawingCollection.getShapeStylingGlyphs().get(p);
			ShapeDrawingCollectionGraphics.drawGlyph(this, glyph, p, getFont(), ss.getDrawColor());
		}
	}
	
	private void addGlyph(String glyph, Point p, Color drawColor)
	{
		ShapeStyling ss = new ShapeStyling(0, drawColor, drawColor, null);//TODO
		glyphDrawingCollection.addGlyph(p, glyph);
		glyphDrawingCollection.addShapeStylingGlyphs(p, ss);
	}
	
	private void addShape(Shape s, Color drawColor)
	{
		int index = shapeDrawingCollection.getShapes().size();
		ShapeStyling ss = new ShapeStyling(index, drawColor, drawColor, null);
		shapeDrawingCollection.addShape(s);
		shapeDrawingCollection.addShapeStyling(ss);
	}
	
	public void setPlotColor(Color plotColor)
	{
		this.plotColor = plotColor;
	}
	public void setTimelineColor(Color timeLineColor)
	{
		this.timeLineColor = timeLineColor;
	}
	public void setTimelineLabelColor(Color timeLineLabelColor)
	{
		this.timeLineLabelColor = timeLineLabelColor;
	}
	
	public void setPad(int pad)
	{
		this.pad = pad;
	}
	public void setPlotSize(int plotSize)
	{
		this.plotSize = plotSize;
	}
	public void setLabelMarkerLen(int labelMarkerLen)
	{
		this.labelMarkerLen = labelMarkerLen;
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
