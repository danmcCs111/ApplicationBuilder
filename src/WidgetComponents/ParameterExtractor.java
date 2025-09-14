package WidgetComponents;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import WidgetComponentInterfaces.ParamOption;

public interface ParameterExtractor 
{
	public static String getCommandBuildString(ArrayList<Component> orderedList, StringBuildOption sbo)
	{
		String retSelection = "";
		switch(sbo)
		{
		case save:
			retSelection = CommandBuild.DELIMITER_PARAMETER_OPTION;
			break;
		case execute:
			break;
		}
		for(Component comp : orderedList)
		{
			if(comp instanceof JTextField)
			{
				JTextField jt = (JTextField) comp;
				if(!jt.getText().strip().isBlank())
				{
					switch(sbo)
					{
					case save:
						retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
						ParamOption.TextField.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
						jt.getText();
						break;
					case execute:
						retSelection += jt.getText();
						break;
					}
				}
			}
			else if(comp instanceof DirectorySelectionEditor)
			{
				DirectorySelectionEditor dse = (DirectorySelectionEditor) comp;
				DirectorySelection ds = (DirectorySelection) dse.getComponentValueObj();
				if(ds.getRelativePath() != null && !ds.getRelativePath().isEmpty())
				{
					switch(sbo)
					{
					case save:
						retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
						ParamOption.Directory.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
						ds.getRelativePath();
						break;
					case execute:
						retSelection += ds.getRelativePath().replaceAll(" ", "\\ ");
						break;
					}
				}
			}
			else if(comp instanceof FileSelectionEditor)
			{
				FileSelectionEditor fse = (FileSelectionEditor) comp;
				FileSelection fs = (FileSelection) fse.getComponentValueObj();
				if(fs.getRelativePath() != null && !fs.getRelativePath().isEmpty())
				{
					switch(sbo)
					{
					case save:
						retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
						ParamOption.File.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
						fs.getRelativePath();
						break;
					case execute:
						retSelection += fs.getRelativePath().replaceAll(" ", "\\ ");
						break;
					}
				}
			}
		}
		return retSelection;
	}
	
	public enum StringBuildOption
	{
		save,
		execute
	}
}
