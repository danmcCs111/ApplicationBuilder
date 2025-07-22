package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeImportExport;
import ShapeWidgetComponents.ShapeStyling;

public class SaveShapeActionListener implements ActionListener 
{
	private ShapeCreator shapeCreator;
	
	public SaveShapeActionListener(ShapeCreator shapeCreator)
	{
		this.shapeCreator = shapeCreator;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		ArrayList <ArrayList <Point>>  listControlPointsScaled = shapeCreator.getControlPointsScaled();
		ArrayList <ShapeStyling> shapeStyling = shapeCreator.getShapeStylings();
		ArrayList<Shape> shapesScaled = shapeCreator.getShapesScaled();
		ShapeImportExport sie = new ShapeImportExport(listControlPointsScaled, shapeStyling, shapesScaled, null);
		sie.performSave(shapeCreator);
	}
}
