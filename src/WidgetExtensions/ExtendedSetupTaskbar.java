package WidgetExtensions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ApplicationBuilder.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetupTaskbar implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetBuildController wbc, WidgetCreatorProperty widgetProperties) 
	{
		JFrame frame = (JFrame) widgetProperties.getInstance();
		String [] args = arg0.split(ARG_DELIMITER);
		ArrayList<BufferedImage> imgs = new ArrayList<BufferedImage>();
		for(String arg : args)
		{
			File file = new File(arg);
			
			try {
				imgs.add(ImageIO.read(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		frame.setIconImages(imgs);
	}

}
