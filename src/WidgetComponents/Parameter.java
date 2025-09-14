package WidgetComponents;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import WidgetComponentInterfaces.ParamOption;

public class Parameter extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<JTextField> paramString = new ArrayList<JTextField>();
	private ArrayList<DirectorySelectionEditor> paramDirectory = new ArrayList<DirectorySelectionEditor>();
	private ArrayList<FileSelectionEditor> paramFile = new ArrayList<FileSelectionEditor>();
	
	public Parameter()
	{
		this.setLayout(new GridLayout(1,0));
	}
	
	public void addParamString(String param)
	{
		JTextField paramText = new JTextField(param);
		paramString.add(paramText);
		this.add(paramText);
	}
	public void addParamDirectory(DirectorySelection ds)
	{
		DirectorySelectionEditor dse = new DirectorySelectionEditor();
		dse.setComponentValue(ds);
		paramDirectory.add(dse);
		this.add(dse);
	}
	public void addParamFile(FileSelection fs)
	{
		FileSelectionEditor fse = new FileSelectionEditor();
		fse.setComponentValue(fs);
		paramFile.add(fse);
		this.add(fse);
	}
	
	public ArrayList<JTextField> getParamStrings()
	{
		return paramString;
	}
	public ArrayList<DirectorySelectionEditor> getParamDirectorySelections()
	{
		return paramDirectory;
	}
	public ArrayList<FileSelectionEditor> getParamFileSelections()
	{
		return paramFile;
	}
	
	public String getCommandBuildString()
	{
		String retSelection = "";
		for(JTextField jt : getParamStrings())
		{
			if(!jt.getText().strip().isBlank())
			{
				retSelection += CommandBuild.DELIMITER_PARAMETER_OPTION + jt.getText() + 
						ParamOption.TextField.getTypeXml() + ParamOption.PathModifier.none;
			}
		}
		for(DirectorySelectionEditor dse : getParamDirectorySelections())
		{
			DirectorySelection ds = (DirectorySelection) dse.getComponentValueObj();
			if(ds.getRelativePath() != null && !ds.getRelativePath().isEmpty())
			{
				retSelection += CommandBuild.DELIMITER_PARAMETER_OPTION + ds.getRelativePath() + 
						ParamOption.Directory.getTypeXml() + ParamOption.PathModifier.none;
			}
		}
		for(FileSelectionEditor fse : getParamFileSelections())
		{
			FileSelection fs = (FileSelection) fse.getComponentValueObj();
			if(fs.getRelativePath() != null && !fs.getRelativePath().isEmpty())
			{
				retSelection += CommandBuild.DELIMITER_PARAMETER_OPTION + fs.getRelativePath() + 
						ParamOption.File.getTypeXml() + ParamOption.PathModifier.none;
			}
		}
		return retSelection;
	}
	
}
