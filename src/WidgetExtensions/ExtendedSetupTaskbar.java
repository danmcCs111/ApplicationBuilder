package WidgetExtensions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import ClassDefinitions.DirectorySelection;
import ClassDefinitions.DirectorySelectionConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetupTaskbar implements ExtendedAttributeParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		JFrame frame = (JFrame) widgetProperties.getInstance();
		File file = new File(new DirectorySelection(arg0).getFullPath());
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			frame.setIconImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
