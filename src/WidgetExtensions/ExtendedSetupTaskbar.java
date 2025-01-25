package WidgetExtensions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
		File file = new File(arg0);
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			frame.setIconImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
