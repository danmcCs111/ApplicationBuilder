package ShapeEditorListeners;

import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;
import ShapeWidgetComponents.ShapeStyling;
import ShapeWidgetComponents.ShapeUtils.DrawMode;

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
		for(ShapeElement se : shapeElements)
		{
			String shapeClassName = se.getShapeClassName();
			DrawMode dm = DrawMode.getMatchingClassName(shapeClassName);
			for(Point p : se.getPoints())
				shapeCreator.addControlPoint(p);
			ShapeStyling ss = se.getShapeStyling(shapeCreator.getNumShapes(), shapeCreator);
			Shape s = shapeCreator.constructShape(dm, (Point []) se.getPoints().toArray(new Point [] {}), ss);
//			ss.setNumberGeneratorConfig(se.getNumberGeneratorConfig(), s);
		}
	}
}
