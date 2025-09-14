package WidgetComponents;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;

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
	
}
