package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;
import ShapeWidgetComponents.ShapeStyling;

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
		ArrayList <ArrayList <Point>>  listControlPointsScaled = shapeCreator.getControlPointsForShapes();
		ArrayList <ShapeStyling> shapeStyling = shapeCreator.getShapeStylings();
		ArrayList<Shape> shapesScaled = shapeCreator.getShapes();
		ShapeImportExport sie = new ShapeImportExport(listControlPointsScaled, shapeStyling, shapesScaled, null);
		
		ArrayList<ShapeElement> shapeElements = sie.openXml(shapeCreator);
		ShapeDrawingCollection sdc = shapeCreator.getShapeDrawingCollection();
		sdc.addShapeImportedListener(shapeCreator);
		sdc.addShapeImports(shapeElements, shapeCreator);
		
	}
}
