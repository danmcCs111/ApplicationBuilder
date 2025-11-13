package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponents.ButtonArrayLoadingNotification;
import WidgetComponents.LoadingLabel;
import WidgetExtensions.ShapeDrawingCollectionLoad;

public class LoadingSpin extends JPanel implements PostWidgetBuildProcessing, ShapeDrawingCollectionLoad,
ButtonArrayLoadingNotification
{
	private static final long serialVersionUID = 1L;

	private ShapeDrawingCollection sdc;
	private LoadingRunnable lr;
	
	private LoadingLabel loadingLabel = new LoadingLabel();
	
	public LoadingSpin(Color backgroundColor, Color textColor)
	{
		this();
		this.setBackground(backgroundColor);
		this.loadingLabel.setForeground(Color.white);
	}
	public LoadingSpin()
	{
		super();
		this.setLayout(new BorderLayout());
		setDoubleBuffered(true);
		this.add(loadingLabel, BorderLayout.SOUTH);
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

	@Override
	public void updateCount(int count, int totalCount) 
	{
		loadingLabel.updateCount(count, totalCount);
	}

}
