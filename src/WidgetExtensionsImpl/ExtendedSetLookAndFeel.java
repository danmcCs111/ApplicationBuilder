package WidgetExtensionsImpl;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ObjectTypeConversion.LookAndFeelClassName;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSetLookAndFeel implements ExtendedAttributeParam
{
	public void applyMethod(LookAndFeelClassName arg0, WidgetCreatorProperty widgetProperties)
	{
		try {
			UIManager.setLookAndFeel(arg0.getLookAndFeel());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
