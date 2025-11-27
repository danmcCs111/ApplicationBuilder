package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Calendar;

import BezierCurveCalculations.AffineTransformRasterizer;
import Graphics2D.ShapeDrawingCollectionGraphics;
import Properties.LoggingMessages;

public class ClockRunnable implements Runnable 
{
	private Container drawContainer;
	private ShapeDrawingCollection sdc;
	private Ellipse2D clockEllipse = null;
	private Line2D 
		hourHand = new Line2D.Double(),
		minuteHand = new Line2D.Double(),
		secondHand = new Line2D.Double();
	private AffineTransformRasterizer afs;
	private PathIterator pi;
	private Point centerPoint;
	private ArrayList<Point> points;
	
	public ClockRunnable(Container drawContainer, ShapeDrawingCollection sdc)
	{
		this.drawContainer = drawContainer;
		this.sdc = sdc;
		int count = 0;
		for(Shape s : sdc.getShapes())
		{
			if(s instanceof Ellipse2D)
			{
				if(sdc.getShapeStylings().get(count).getNumberGeneratorConfig() == null)//TODO
				{
					clockEllipse = (Ellipse2D) s;
					break;
				}
			}
			count++;
		}
		setup(clockEllipse);
	}
	
	@Override
	public void run() 
	{
		try {
			Thread.sleep(1000);//TODO
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ShapeDrawingCollectionGraphics.drawAll(drawContainer, sdc, null, false);
		do
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getTimeWarp(clockEllipse);
		} while(true);
	}
	
	private void getTimeWarp(Ellipse2D boundsShape)
	{
		int hourDeg = getDegIndex(Calendar.HOUR);
		int minuteDeg = getDegIndex(Calendar.MINUTE);
		int secondDeg = getDegIndex(Calendar.SECOND);
		
		hourHand.setLine(centerPoint, points.get(hourDeg));
		minuteHand.setLine(centerPoint, points.get(minuteDeg));
		secondHand.setLine(centerPoint, points.get(secondDeg));
		
		ShapeDrawingCollectionGraphics.drawAll(drawContainer, sdc, null, false);
		
		ShapeDrawingCollectionGraphics.drawShape(drawContainer, hourHand, Color.gray);
		ShapeDrawingCollectionGraphics.drawShape(drawContainer, minuteHand, Color.blue);
		ShapeDrawingCollectionGraphics.drawShape(drawContainer, secondHand, Color.red);
		
	}
	
	private static int getDegIndex(int calSelect)
	{
		Calendar cal = Calendar.getInstance();
		int deg = 0;
		switch(calSelect)
		{
		case Calendar.HOUR:
			double hour = cal.get(Calendar.HOUR);
			deg = (int)((hour / 12.0) * 360.0);
			break;
		case Calendar.MINUTE:
			double minute = cal.get(Calendar.MINUTE);
			deg = (int)((minute / 60.0) * 360.0);
			break;
		case Calendar.SECOND:
			double second = cal.get(Calendar.SECOND);
			deg = (int)((second / 60.0) * 360.0);
			break;
		}
		
		deg += (deg > 90)
			? -90
			: 270;
		return deg-1;
	}
	
	private void setup(Ellipse2D boundsShape)
	{
		if(boundsShape == null)
		{
			LoggingMessages.printOut("no clock ellipse found");
			return;
		}
		this.afs = new AffineTransformRasterizer();
		this.pi = boundsShape.getPathIterator(afs);
		
		centerPoint = new Point((int)(boundsShape.getCenterX()), (int)(boundsShape.getCenterY()));
		points = getPoints(boundsShape, pi, afs);
	}
	
	private static ArrayList<Point> getPoints(Shape s, PathIterator pi, AffineTransformRasterizer afs)
	{
		ArrayList<Point> points = new ArrayList<Point>();
		points.addAll(afs.samplePoints(pi, s, (1.0/90.0)));
		return points;
	}
	
}
