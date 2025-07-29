package ShapeWidgetComponents;


import javax.swing.JPanel;

import ShapeEditorListeners.ShapeStylingActionListener;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class ClockApp extends JPanel implements PostWidgetBuildProcessing, ShapeStylingActionListener, ShapeDrawingCollectionLoad
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc;

	public ClockApp()
	{
		
	}
	
	public void postExecute() 
	{
		ClockRunnable cr = new ClockRunnable(this, sdc);
		Thread t = new Thread(cr);
		t.start();
	}

	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadedShapeDrawingCollection(ShapeDrawingCollection sdc) 
	{
		this.sdc = sdc;
	}
	
}
