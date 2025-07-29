package ShapeWidgetComponents;


import javax.swing.JPanel;

import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class ClockApp extends JPanel implements PostWidgetBuildProcessing, ShapeDrawingCollectionLoad
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc;
	private ClockRunnable cr;

	public ClockApp()
	{
		
	}
	
	@Override
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

	@Override
	public void addShapeDrawingCollection(ShapeDrawingCollection sdc) 
	{
		this.sdc = sdc;
	}

}
