package WidgetComponentInterfaces;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class ImageReaderDimensionObserver implements ImageObserver 
{
	private double width=-1, height=-1;
	
	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{
		if(width != -1)
			this.width = width;
		if(height != -1)
			this.height = height;
		return true;
	}
	
	public Dimension getScaledFromWidth(double scaledWidth)
	{
		while(width == -1 || height == -1)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		double factor = scaledWidth / width;
		
		return new Dimension((int)(factor * width), (int)(factor * height));
	}
	
	public Dimension getScaledFromHeight(double scaledHeight)
	{
		while(width == -1 || height == -1)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		double factor = scaledHeight / height;
		
		return new Dimension((int)(factor * width), (int)(factor * height));
	}

}
