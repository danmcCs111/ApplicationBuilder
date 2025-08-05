package WidgetExtensionsImpl;

import java.io.File;
import java.util.ArrayList;


import ObjectTypeConversion.FileSelection;
import ShapeWidgetComponents.ShapeDrawingCollection;
import ShapeWidgetComponents.ShapeElement;
import ShapeWidgetComponents.ShapeImportExport;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ShapeDrawingCollectionLoad;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedShapeDrawingCollectionLoader implements ExtendedAttributeParam  
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		ShapeDrawingCollectionLoad sdcL = (ShapeDrawingCollectionLoad) widgetProperties.getInstance();
		File file = new File(new FileSelection(arg0).getFullPath());
		ShapeDrawingCollection sdc = new ShapeDrawingCollection();
		sdcL.addShapeDrawingCollection(sdc);
		ShapeImportExport sie = new ShapeImportExport();
		@SuppressWarnings("unchecked")
		ArrayList<ShapeElement> shapeElements = (ArrayList<ShapeElement>) sie.openXml(file);
		
		sdc.addShapeImports(shapeElements, sdcL);
			
	}

}
