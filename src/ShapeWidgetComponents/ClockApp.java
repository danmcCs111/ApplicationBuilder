package ShapeWidgetComponents;


import javax.swing.JPanel;

import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class ClockApp extends JPanel implements PostWidgetBuildProcessing, ShapeDrawingCollectionLoad
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc = new ShapeDrawingCollection();
	private ClockRunnable cr;

	public ClockApp()
	{
		
	}
	
	@Override
	public ShapeDrawingCollection getShapeDrawingCollection()
	{
		return this.sdc;
	}
	
	public void postExecute() 
	{
		cr = new ClockRunnable(this, sdc);
		Thread t = new Thread(cr);
		t.start();
	}
	
	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) 
	{
		if(sdc!= null && shapeStyleIndex > sdc.getShapeStylings().size()-1)
		{
			sdc.addShapeStyling(shapeStyling);
		}
	}

}
