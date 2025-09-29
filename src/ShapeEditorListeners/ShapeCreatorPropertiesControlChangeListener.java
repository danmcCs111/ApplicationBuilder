package ShapeEditorListeners;

import java.awt.Color;

import DrawModesAbstract.DrawMode;
import ShapeWidgetComponents.ShapeCreator.Operation;

public interface ShapeCreatorPropertiesControlChangeListener 
{
	public Color palletteChanged(Color c);
	public DrawMode drawModeChanged(DrawMode dm);
	public Operation operationChanged(Operation op);
}
