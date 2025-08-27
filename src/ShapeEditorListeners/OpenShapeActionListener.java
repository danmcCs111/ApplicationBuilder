package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;

public class OpenShapeActionListener implements ActionListener 
{
	private ShapeCreator shapeCreator;
	
	public OpenShapeActionListener(ShapeCreator shapeCreator)
	{
		this.shapeCreator = shapeCreator;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		ShapeImportExport sie = new ShapeImportExport();
		
		@SuppressWarnings("unchecked")
		ArrayList<ShapeElement> shapeElements = (
				ArrayList<ShapeElement>) sie.openXml(shapeCreator, 
				ShapeImportExport.FILE_TYPE_TITLE, 
				ShapeImportExport.FILE_TYPE_FILTER, 
				ShapeImportExport.DEFAULT_DIRECTORY_RELATIVE);
		
		ShapeDrawingCollection sdc = shapeCreator.getShapeDrawingCollection();
		sdc.addShapeImportedListener(shapeCreator);
		sdc.addShapeImports(shapeElements, shapeCreator);
		
	}
}
