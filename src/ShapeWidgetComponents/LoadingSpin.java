package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import ObjectTypeConversion.ShapeFileSelection;
import WidgetComponentInterfaces.ButtonArrayLoadingNotification;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponents.LoadingLabel;
import WidgetExtensionInterfaces.ShapeDrawingCollectionLoad;

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
	
	public void addShapeDrawingCollectionLoader(ShapeFileSelection fs)
	{
		ShapeDrawingCollectionLoad sdcL = this;
		File file = new File(fs.getFullPath());
		ShapeDrawingCollection sdc = new ShapeDrawingCollection();
		sdcL.addShapeDrawingCollection(sdc);
		ShapeImportExport sie = new ShapeImportExport();
		@SuppressWarnings("unchecked")
		ArrayList<ShapeElement> shapeElements = (ArrayList<ShapeElement>) sie.openXml(file);
		
		sdc.addShapeImports(shapeElements, sdcL);
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
