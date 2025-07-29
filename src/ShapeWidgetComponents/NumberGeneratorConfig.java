package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import BezierCurveCalculations.AffineTransformRasterizer;
import Properties.LoggingMessages;

public class NumberGeneratorConfig 
{
	private double numOfSamples = 90.0;
	private int 
		rangeValLow = 1,
		rangeValHigh = 12,
		startingNumber = 3,
		fontSize = 18;
	private Color fillColor = Color.black; 
	private Font testFont = null;
	ArrayList<Point> points;
	AffineTransformRasterizer afs;
	double it;
	
	public NumberGeneratorConfig(Shape s, ShapeStyling ss, int rangeValLow, int rangeValHigh, int startingNumber, int fontSize, Color fillColor)
	{
		this.rangeValLow = rangeValLow;
		this.rangeValHigh = rangeValHigh;
		this.startingNumber = startingNumber;
		setFontSize(fontSize);
		this.fillColor = fillColor;
		generateNumberGraphics(s, ss);
	}
	
	public NumberGeneratorConfig(Shape s, ShapeStyling ss, String xmlAttributeString)
	{
		String [] params = xmlAttributeString.split(",");
		this.rangeValLow = Integer.parseInt(params[0].strip());
		this.rangeValHigh = Integer.parseInt(params[1].strip());
		this.startingNumber = Integer.parseInt(params[2].strip());
		setFontSize(Integer.parseInt(params[3].strip()));
		this.fillColor = new Color(
				Integer.parseInt(params[4].strip()), 
				Integer.parseInt(params[5].strip()), 
				Integer.parseInt(params[6].strip())
		);
		generateNumberGraphics(s, ss);
	}
	
	public double getNumberOfSamples()
	{
		return this.numOfSamples;
	}
	public int getRangeValLow()
	{
		return this.rangeValLow;
	}
	public int getRangeValHigh()
	{
		return this.rangeValHigh;
	}
	public int getStartingNumber()
	{
		return this.startingNumber;
	}
	
	public int getFontSize()
	{
		return this.fontSize;
	}
	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
		testFont = new Font("Serif", Font.BOLD, getFontSize());//TODO
	}
	
	public Font getFont()
	{
		return this.testFont;
	}
	
	public Color getFillColor()
	{
		return this.fillColor;
	}
	
	public ArrayList<Point> getPoints()
	{
		return this.points;
	}
	
	public AffineTransformRasterizer getAffineTransformRasterizer()
	{
		return this.afs;
	}
	
	public double getSampleSkipDivide()
	{
		return this.it;
	}
	
	public void generateNumberGraphics(Shape s, ShapeStyling ss)
	{
		PathIterator pi = ss.getPathIterator();
		this.afs = ss.getAffineTransform();
		points = new ArrayList<Point>();
		points.addAll(afs.samplePoints(pi, s, (1.0/getNumberOfSamples())));
		LoggingMessages.printOut("Number of Steps: " + afs.getNumberOfSteps() + " size " + points.size());
		if(afs.getNumberOfSteps() == 0)//TODO bug?
			return;
		it = ((getRangeValHigh() - (getRangeValLow()-1)) / afs.getNumberOfSteps());
		LoggingMessages.printOut("Sample skip divide: " + it);
	}
	
	@Override
	public String toString()
	{
		return LoggingMessages.combine(new Object [] {rangeValLow, rangeValHigh, startingNumber, fontSize, 
				fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()});
	}
}
