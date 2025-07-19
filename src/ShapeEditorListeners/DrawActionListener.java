package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ShapeWidgetComponents.ShapeCreator;

public class DrawActionListener implements ActionListener 
{
	private ShapeCreator sc;
	
	public DrawActionListener(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		sc.drawAll();
	}
	
}
