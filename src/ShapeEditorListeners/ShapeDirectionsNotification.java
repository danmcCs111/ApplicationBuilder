package ShapeEditorListeners;

import ShapeWidgetComponents.ShapeCreator.Operation;

public interface ShapeDirectionsNotification 
{
	public void shapeDirectionsUpdate(String updatedDirections);
	public void shapeOperationUpdate(Operation updatedOperation);
}
