package WidgetExtensions;

import ClassDefintions.BooleanConverter;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedPostProcessingExec implements ExtendedAttributeStringParam 
{
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		BooleanConverter bc = new BooleanConverter();
		boolean doPostExecute = (boolean) bc.conversionCall(arg0);
		PostWidgetBuildProcessing compPost = (PostWidgetBuildProcessing) widgetProperties.getInstance();
		compPost.setDoPostExecute(doPostExecute);
	}

}
