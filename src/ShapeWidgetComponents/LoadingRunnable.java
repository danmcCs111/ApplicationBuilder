package ShapeWidgetComponents;

import java.awt.Container;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import Graphics2D.ShapeDrawingCollectionGraphics;

public class LoadingRunnable implements Runnable
{
	public double 
		degreeShift = 20.0;
	public int sleepMillisInterval = 100;
	
	private Container drawContainer;
	private Shape originalShape;
	private ArrayList<Point> cps;
	private ShapeStyling ss;
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	public LoadingRunnable(Container drawContainer, ShapeDrawingCollection sdc)
	{
		this.drawContainer = drawContainer;
		
		originalShape = sdc.getShapes().get(0);//TODO; switch to images...
		cps = sdc.getShapeControlPoints().get(0);
		ss = sdc.getShapeStylings().get(0);
	}
	
	public void setShiftDegrees(double degrees)
	{
		this.degreeShift = degrees;
	}
	
	public void setSleepMillisInterval(int sleep)
	{
		this.sleepMillisInterval = sleep;
	}
	
	private void loadShapes()
	{
		double divCount = 360.0 / this.degreeShift;
		
		for(int i = 1; i < this.degreeShift; i++)
		{
			ArrayList<Point> recalcPoints = ShapeUtils.rotate(originalShape, cps, divCount * i);
			Shape newShape = ShapeUtils.recalculateShape(originalShape, recalcPoints);
			shapes.add(newShape);
		}
	}
	
	@Override
	public void run() 
	{
		loadShapes();
		do
		{
			for(Shape shape : shapes)
			{
				SwingUtilities.invokeLater(new Runnable() {
				    public void run() {
				    	ShapeDrawingCollectionGraphics.clearAll(drawContainer);
				        ShapeDrawingCollectionGraphics.drawShape(drawContainer, shape, ss);
				    }
				});
				try {
					Thread.sleep(this.sleepMillisInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while(true);
	}
	
}
