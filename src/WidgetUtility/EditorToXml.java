package WidgetUtility;

import java.util.ArrayList;

import WidgetComponents.ParameterEditor;

public class EditorToXml 
{
	private String xmlFileName;
	
	public void writeXml(String sourceFile, ArrayList<ParameterEditor> parameterEditors)
	{
		this.xmlFileName = sourceFile;
		writeXml();
	}
	
	private void writeXml()//TODO
	{
		
	}
}
