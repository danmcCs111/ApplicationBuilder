package ShapeWidgetComponents;

import java.awt.Container;

import Properties.LoggingMessages;

public class ClockRunnable implements Runnable 
{
	private Container drawContainer;
	private ShapeDrawingCollection sdc;
	
	public ClockRunnable(Container drawContainer, ShapeDrawingCollection sdc)
	{
		this.drawContainer = drawContainer;
		this.sdc = sdc;
	}
	
	@Override
	public void run() 
	{
		ShapeDrawingCollectionGraphics.drawAll(drawContainer, sdc, null, false);
		LoggingMessages.printOut("draw: " + sdc);
		do
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ShapeDrawingCollectionGraphics.drawAll(drawContainer, sdc, null, false);
			LoggingMessages.printOut("draw: " + sdc);
		} while(true);
	}

}
