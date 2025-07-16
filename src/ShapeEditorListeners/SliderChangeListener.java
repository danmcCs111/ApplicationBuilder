package ShapeEditorListeners;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import WidgetComponents.ShapeCreator;

public class SliderChangeListener implements ChangeListener 
{
	private ShapeCreator sc;
	
	public SliderChangeListener(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		sc.getSliderLabel().setText(sc.getSlider().getValue()+"");
	}

}
