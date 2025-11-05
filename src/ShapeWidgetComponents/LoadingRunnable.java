package ShapeWidgetComponents;

import java.awt.Container;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

public class LoadingRunnable implements Runnable
{
	public static double 
		DEGREE_SHIFT = 50.0;
	public static int SLEEP_MILLIS = 200;
	
	private Container drawContainer;
	private ShapeDrawingCollection sdc;
	private Shape originalShape;
	private ArrayList<Point> cps;
	private ShapeStyling ss;
	
	public LoadingRunnable(Container drawContainer, ShapeDrawingCollection sdc)
	{
		this.drawContainer = drawContainer;
		this.sdc = sdc;
		
		originalShape = sdc.getShapes().get(0);//TODO;
		cps = sdc.getShapeControlPoints().get(0);
		ss = sdc.getShapeStylings().get(0);
	}
	
	@Override
	public void run() 
	{
		int count = 1;
		try {
			Thread.sleep(SLEEP_MILLIS);//TODO
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		do
		{
			try {
				Thread.sleep(SLEEP_MILLIS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			double degrees = (DEGREE_SHIFT * count);
			rotate(degrees, sdc);
			count++;
		} while(true);
	}
	
	private void rotate(double degrees, ShapeDrawingCollection sdc)
	{
		ArrayList<Point> recalcPoints = ShapeUtils.rotate(originalShape, cps, degrees);
		Shape newShape = ShapeUtils.recalculateShape(originalShape, recalcPoints);
		
		ShapeDrawingCollectionGraphics.clearAll(drawContainer);
		ShapeDrawingCollectionGraphics.drawShape(drawContainer, newShape, ss);
	}

}
