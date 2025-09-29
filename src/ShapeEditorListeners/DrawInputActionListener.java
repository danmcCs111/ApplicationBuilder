package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DrawModesAbstract.DrawMode;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeCreator.Operation;

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
		sc.setDirectionsIndex(0);
		sc.setDirectionsText(mode.getDirections()[sc.getDirectionsIndex()]);
	}
	
}
