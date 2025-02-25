package WidgetExtensionsImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ClassDefinitions.FileSelection;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetupTaskbar implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		JFrame frame = (JFrame) widgetProperties.getInstance();
		File file = new File(new FileSelection(arg0).getFullPath());
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			frame.setIconImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
