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
		this.sc.setMode(Mode.Curve);
		Mode mode = this.sc.getMode();
		sc.getDirectionsLabel().setText(mode.getDirections()[sc.getDirectionsIndex()]);
		sc.incrementDirectionsIndex(1);
		sc.getAddCurveButton().setVisible(false);
	}
	
}
