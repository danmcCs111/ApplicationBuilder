package ShapeEditorListeners;

import java.awt.Color;

import ShapeWidgetComponents.ShapeCreator.Operation;
import ShapeWidgetComponents.ShapeUtils.DrawMode;

public interface ShapeCreatorPropertiesControlChangeListener 
{
	public Color palletteChanged(Color c);
	public DrawMode drawModeChanged(DrawMode dm);
	public Operation operationChanged(Operation op);
}
