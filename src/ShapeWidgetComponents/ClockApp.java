package ShapeWidgetComponents;

import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import Properties.LoggingMessages;
import Properties.PathUtility;
import ShapeEditorListeners.ShapeStylingActionListener;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ClockApp extends JPanel implements PostWidgetBuildProcessing, ShapeStylingActionListener
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc;

	public ClockApp()
	{
		
	}
	
	public void buildLayout()
	{
		sdc = new ShapeDrawingCollection();
		ShapeImportExport sie = new ShapeImportExport();
		File f = new File(PathUtility.getCurrentDirectory() +  "/src/ApplicationBuilder/shapes/circle.xml");
		ArrayList<ShapeElement> shapeElements = sie.openXml(f);
		int count = 0;
		for(ShapeElement se : shapeElements)
		{
			LoggingMessages.printOut(se.toString());
			sdc.addShapeControlPoints(se.getPoints());
			ShapeStyling ss = se.getShapeStyling(count, this);
			Shape s = se.getShape(ss);
			sdc.addShape(s);
			sdc.addShapeStyling(ss);
			
			LoggingMessages.printOut(ss.toString());
			count++;
		}
	}

	public void postExecute() 
	{
		buildLayout();
		ClockRunnable cr = new ClockRunnable(this, sdc);
		Thread t = new Thread(cr);
		t.start();
	}

	@Override
	public void notifyStylingChanged(int shapeStyleIndex, ShapeStyling shapeStyling) {
		// TODO Auto-generated method stub
		
	}
	
}
