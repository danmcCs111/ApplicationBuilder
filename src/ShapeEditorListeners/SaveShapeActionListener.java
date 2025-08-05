package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeImportExport;

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
		ShapeImportExport sie = new ShapeImportExport();
		sie.performSave(shapeCreator, shapeCreator.getShapeDrawingCollection(), null);
	}
}
