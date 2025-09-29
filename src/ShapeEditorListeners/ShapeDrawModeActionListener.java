package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import DrawModesAbstract.DrawMode;
import Properties.LoggingMessages;
import ShapeWidgetComponents.ShapeCreator;
import ShapeWidgetComponents.ShapeCreatorToolBarPanel;

public class ShapeDrawModeActionListener implements ActionListener 
{
	private ShapeCreator sc;
	private ShapeCreatorToolBarPanel sctp;
	
	public ShapeDrawModeActionListener(ShapeCreator sc, ShapeCreatorToolBarPanel sctp)
	{
		this.sc = sc;
		this.sctp = sctp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JComboBox<DrawMode> modeSelections = sctp.getModeSelectionCombo();
		LoggingMessages.printOut("mode changed: " + modeSelections.getSelectedItem());
		sc.setMode((DrawMode) modeSelections.getSelectedItem());
	}

}
