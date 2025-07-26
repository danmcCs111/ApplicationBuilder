package ShapeWidgetComponents;

import java.awt.Color;

import Properties.LoggingMessages;

public class NumberGeneratorConfig 
{
	private double numOfSamples = 240.0;
	private int 
		rangeValLow = 1,
		rangeValHigh = 12,
		startingNumber = 3,
		fontSize = 18;
	private Color fillColor = Color.black; 
	
	public NumberGeneratorConfig(int rangeValLow, int rangeValHigh, int startingNumber, int fontSize, Color fillColor)
	{
		this.rangeValLow = rangeValLow;
		this.rangeValHigh = rangeValHigh;
		this.startingNumber = startingNumber;
		this.fontSize = fontSize;
		this.fillColor = fillColor;
	}
	
	public NumberGeneratorConfig(String xmlAttributeString)
	{
		String [] params = xmlAttributeString.split(",");
		this.rangeValLow = Integer.parseInt(params[0].strip());
		this.rangeValHigh = Integer.parseInt(params[1].strip());
		this.startingNumber = Integer.parseInt(params[2].strip());
		this.fontSize = Integer.parseInt(params[3].strip());
		this.fillColor = new Color(
				Integer.parseInt(params[4].strip()), 
				Integer.parseInt(params[5].strip()), 
				Integer.parseInt(params[6].strip())
		);
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
	
	public Color getFillColor()
	{
		return this.fillColor;
	}
	
	@Override
	public String toString()
	{
		return LoggingMessages.combine(new Object [] {rangeValLow, rangeValHigh, startingNumber, fontSize, 
				fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue()});
	}
}
