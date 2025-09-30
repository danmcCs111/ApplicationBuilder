package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import DrawModes.GeneralPathDrawMode;
import DrawModesAbstract.DrawMode;
import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeCreator.Operation;
import WidgetUtility.WidgetBuildController;

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
		if(mode.getDirections().length == 0)
		{
			mode.constructShape(null, null);
		}
		else
		{
			if(mode instanceof GeneralPathDrawMode)
			{
				GeneralPathDrawMode gpdm = ((GeneralPathDrawMode) mode);
				int opt = JOptionPane.showConfirmDialog(WidgetBuildController.getInstance().getFrame(), "Reset General Path of... " + 
						LoggingMessages.combine(gpdm.getGeneralPathShape().getDrawPaths()));
				if(opt == JOptionPane.YES_OPTION) 
				{
					gpdm.clearDirectionsAndDrawPath();
					mode.constructShape(null, null);
				}
			}
			else
			{
				sc.setDirectionsText(mode.getDirections()[sc.getDirectionsIndex()]);
			}
		}
	}
	
}
