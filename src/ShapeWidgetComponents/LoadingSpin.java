package ShapeWidgetComponents;

import javax.swing.JPanel;

import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class LoadingSpin extends JPanel implements PostWidgetBuildProcessing, ShapeDrawingCollectionLoad 
{
	private static final long serialVersionUID = 1L;

	private ShapeDrawingCollection sdc;
	private LoadingRunnable lr;
	
	public LoadingSpin()
	{
		
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

	@Override
	public void postExecute() 
	{
		lr = new LoadingRunnable(this, sdc);
		Thread t = new Thread(lr);
		t.start();
	}

}
