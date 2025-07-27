package ShapeWidgetComponents;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class ClockApp extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ShapeDrawingCollection sdc;

	public ClockApp()
	{
		sdc = new ShapeDrawingCollection();
		ShapeImportExport sie = new ShapeImportExport();
		File f = new File(PathUtility.getCurrentDirectory() +  "/src/ApplicationBuilder/shapes/tmp.xml");
		ArrayList<ShapeElement> shapeElements = sie.openXml(f);
		int count = 0;
		for(ShapeElement se : shapeElements)
		{
			LoggingMessages.printOut(se.toString());
			sdc.addShapeControlPoints(se.getPoints());
			ShapeStyling ss = se.getShapeStyling(count, null);
			NumberGeneratorConfig ngConfig = se.getNumberGeneratorConfig();
			ss.setNumberGeneratorConfig(ngConfig, se.getShape(ss));
			sdc.addShapeStyling(ss);
			
			LoggingMessages.printOut(ss.toString());
			count++;
		}
	}
	
	public static void main(String [] args)
	{
		JPanel clock = new ClockApp();
		
	}
}
