package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import WidgetComponentDialogs.MouseDragScrollUnitAdjust;
import WidgetUtility.WidgetBuildController;

public class MouseDragScrollUnitAdjustActionListener implements ActionListener 
{
	private MouseDragScrollUnitAdjust 
		mdsu;
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		if(mdsu != null)
		{
			mdsu.dispose();
		}
		mdsu = new MouseDragScrollUnitAdjust(frame);
	}
}
