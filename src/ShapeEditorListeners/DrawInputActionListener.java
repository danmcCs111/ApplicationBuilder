package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponents.ShapeCreator;
import WidgetComponents.ShapeCreator.DrawMode;
import WidgetComponents.ShapeCreator.Operation;

public class DrawInputActionListener implements ActionListener
{
	private ShapeCreator sc;
	
	public DrawInputActionListener(ShapeCreator sc) 
	{
		this.sc = sc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		DrawMode mode = this.sc.getMode();
		sc.setOperation(Operation.Draw);
		sc.incrementDirectionsIndex(1);
		sc.getDirectionsLabel().setText(mode.getDirections()[sc.getDirectionsIndex()]);
		sc.getAddCurveButton().setEnabled(false);
	}
	
}
