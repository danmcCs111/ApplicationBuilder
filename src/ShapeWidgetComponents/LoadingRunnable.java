package ShapeWidgetComponents;

import java.awt.Container;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;

public class LoadingRunnable implements Runnable
{
	public static double 
		DEGREE_SHIFT_TIMES = 20.0;
	public static int SLEEP_MILLIS = 150;
	
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
	
	private void loadShapes()
	{
		double divCount = 360.0 / DEGREE_SHIFT_TIMES;
		
		for(int i = 1; i < DEGREE_SHIFT_TIMES; i++)
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
				ShapeDrawingCollectionGraphics.clearAll(drawContainer);
				ShapeDrawingCollectionGraphics.drawShape(drawContainer, shape, ss);
				try {
					Thread.sleep(SLEEP_MILLIS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while(true);
	}
	
}
