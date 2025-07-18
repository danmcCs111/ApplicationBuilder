package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import Properties.LoggingMessages;
import WidgetComponents.ShapeCreator;
import WidgetComponents.ShapeCreator.DrawMode;

public class ShapeDrawModeActionListener implements ActionListener 
{
	private ShapeCreator sc;
	
	public ShapeDrawModeActionListener(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JComboBox<DrawMode> modeSelections = sc.getModeSelectionCombo();
		LoggingMessages.printOut("mode changed: " + modeSelections.getSelectedItem());
		sc.setMode((DrawMode) modeSelections.getSelectedItem());
	}

}
