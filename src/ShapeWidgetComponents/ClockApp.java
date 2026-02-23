package ShapeWidgetComponents;


import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import ObjectTypeConversion.FileSelection;
import ObjectTypeConversion.ShapeFileSelection;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensionInterfaces.ShapeDrawingCollectionLoad;

/**
 * Visual demonstrating the JVMs expanding memory and the increased performance over time. 
 * 
 */
public class ClockApp extends JPanel implements PostWidgetBuildProcessing, ShapeDrawingCollectionLoad
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc;
	private ClockRunnable cr;

	public ClockApp()
	{
		
	}
	
	public void addShapeDrawingCollectionLoader(ShapeFileSelection sfs)
	{
		ShapeDrawingCollectionLoad sdcL = this;
		File file = new File(sfs.getFullPath());
		ShapeDrawingCollection sdc = new ShapeDrawingCollection();
		sdcL.addShapeDrawingCollection(sdc);
		ShapeImportExport sie = new ShapeImportExport();
		@SuppressWarnings("unchecked")
		ArrayList<ShapeElement> shapeElements = (ArrayList<ShapeElement>) sie.openXml(file);
		
		sdc.addShapeImports(shapeElements, sdcL);
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
