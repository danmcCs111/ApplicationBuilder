package ShapeEditorListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ShapeWidgetComponents.ShapeCreator;

public class ClearActionListener implements ActionListener
{
	private ShapeCreator sc;
	
	public ClearActionListener(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		sc.clearAll();
	}

}
