package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetComponents.ShapeCreator;
import WidgetComponents.ShapeCreator.Mode;

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
		Mode mode = this.sc.getMode();
		sc.incrementDirectionsIndex(1);
		sc.getDirectionsLabel().setText(mode.getDirections()[sc.getDirectionsIndex()]);
		sc.getAddCurveButton().setVisible(false);
	}
	
}
