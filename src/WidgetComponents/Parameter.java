package WidgetComponents;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import WidgetComponents.ParameterExtractor.StringBuildOption;

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
		return ParameterExtractor.getCommandBuildString(orderedList, StringBuildOption.save);
	}
	
	public String getCommandBuildString()
	{
		return ParameterExtractor.getCommandBuildString(orderedList, StringBuildOption.execute);
	}
	
}
