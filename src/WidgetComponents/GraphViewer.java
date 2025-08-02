package WidgetComponents;

import java.util.Date;
import javax.swing.JPanel;

import Properties.LoggingMessages;

public abstract class GraphViewer extends JPanel
{
	private static final long serialVersionUID = 1L;

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
		
		yPoint = (int)((1.0-(number / spread)) * panelHeight);
		
		yPoint += pad;
		LoggingMessages.printOut("Value: " + num.toString() + "|  y point: " + yPoint);
		
		return yPoint;
	}
}
