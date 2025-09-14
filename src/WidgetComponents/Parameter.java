package WidgetComponents;

import java.awt.Component;
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
	
	private ArrayList<Component> orderedList = new ArrayList<Component>();
	
	public Parameter()
	{
		this.setLayout(new GridLayout(1,0));
	}
	
	public void addParamString(String param)
	{
		JTextField paramText = new JTextField(param);
		orderedList.add(paramText);
		this.add(paramText);
	}
	public void addParamDirectory(DirectorySelection ds)
	{
		DirectorySelectionEditor dse = new DirectorySelectionEditor();
		dse.setComponentValue(ds);
		orderedList.add(dse);
		this.add(dse);
	}
	public void addParamFile(FileSelection fs)
	{
		FileSelectionEditor fse = new FileSelectionEditor();
		fse.setComponentValue(fs);
		orderedList.add(fse);
		this.add(fse);
	}
	
	public ArrayList<Component> getOrderedList()
	{
		return this.orderedList;
	}
	
	public String getCommandBuildSaveString()
	{
		String retSelection = CommandBuild.DELIMITER_PARAMETER_OPTION;
		for(Component comp : orderedList)
		{
			if(comp instanceof JTextField)
			{
				JTextField jt = (JTextField) comp;
				if(!jt.getText().strip().isBlank())
				{
					retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
							ParamOption.TextField.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
							jt.getText();
				}
			}
			else if(comp instanceof DirectorySelectionEditor)
			{
				DirectorySelectionEditor dse = (DirectorySelectionEditor) comp;
				DirectorySelection ds = (DirectorySelection) dse.getComponentValueObj();
				if(ds.getRelativePath() != null && !ds.getRelativePath().isEmpty())
				{
					retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
							ParamOption.Directory.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
							ds.getRelativePath();
				}
			}
			else if(comp instanceof FileSelectionEditor)
			{
				FileSelectionEditor fse = (FileSelectionEditor) comp;
				FileSelection fs = (FileSelection) fse.getComponentValueObj();
				if(fs.getRelativePath() != null && !fs.getRelativePath().isEmpty())
				{
					retSelection += CommandBuild.DELIMITER_PARAMETER_TYPE + 
							ParamOption.File.getTypeXml() + ParamOption.PathModifier.none.getModVal() +
							fs.getRelativePath();
				}
			}
		}
		return retSelection;
	}
	
	public String getCommandBuildString()
	{
		String retSelection = "";
		for(Component comp : orderedList)
		{
			if(comp instanceof JTextField)
			{
				JTextField jt = (JTextField) comp;
				if(!jt.getText().strip().isBlank())
				{
					retSelection += jt.getText();
				}
			}
			else if(comp instanceof DirectorySelectionEditor)
			{
				DirectorySelectionEditor dse = (DirectorySelectionEditor) comp;
				DirectorySelection ds = (DirectorySelection) dse.getComponentValueObj();
				if(ds.getRelativePath() != null && !ds.getRelativePath().isEmpty())
				{
					retSelection += ds.getRelativePath().replaceAll(" ", "\\ ");
				}
			}
			else if(comp instanceof FileSelectionEditor)
			{
				FileSelectionEditor fse = (FileSelectionEditor) comp;
				FileSelection fs = (FileSelection) fse.getComponentValueObj();
				if(fs.getRelativePath() != null && !fs.getRelativePath().isEmpty())
				{
					retSelection += fs.getRelativePath().replaceAll(" ", "\\ ");
				}
			}
		}
		return retSelection;
	}
	
}
