package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import ApplicationBuilder.RedrawableFrame;

public class ComponentComboBoxActionListener implements ActionListener 
{
	private RedrawableFrame redrawFrame;
	
	public ComponentComboBoxActionListener(RedrawableFrame redrawFrame)
	{
		this.redrawFrame = redrawFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		redrawFrame.rebuildInnerPanels();
	}

}
