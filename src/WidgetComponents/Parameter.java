package WidgetComponents;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import ObjectTypeConversion.DirectorySelection;

public class Parameter extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<JTextField> paramString = new ArrayList<JTextField>();
	private ArrayList<DirectorySelectionEditor> paramDirectory = new ArrayList<DirectorySelectionEditor>();
	
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
	
	public ArrayList<JTextField> getParamStrings()
	{
		return paramString;
	}
	public ArrayList<DirectorySelectionEditor> getParamDirectorySelections()
	{
		return paramDirectory;
	}
	
}
