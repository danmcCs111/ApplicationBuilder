package WidgetComponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import WidgetComponents.ParameterExtractor.StringBuildOption;

public class Parameter extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private static Color 
		panelBackgroundColor,
		buttonBackgroundColor,
		buttonForegroundColor;
	
	private ArrayList<Component> orderedList = new ArrayList<Component>();
	
	public Parameter()
	{
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.setLayout(new GridLayout(1,0));
		
		if(panelBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorPanel(this, panelBackgroundColor);
		}
	}
	
	public static void setButtonForegroundColor(Color c)
	{
		buttonForegroundColor = c;
	}
	public static void setButtonBackgroundColor(Color c)
	{
		buttonBackgroundColor = c;
	}
	
	public static void setPanelBackgroundColor(Color c)
	{
		panelBackgroundColor = c;
	}
	
	public void addParamString(String param)
	{
		JTextField paramText = new JTextField(param);
		if(buttonForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(paramText, buttonForegroundColor);
		}
		if(panelBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorPanel(paramText, panelBackgroundColor);
		}
		orderedList.add(paramText);
		this.add(paramText);
	}
	public void addParamDirectory(DirectorySelection ds)
	{
		DirectorySelectionEditor dse = new DirectorySelectionEditor();
		if(buttonForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(dse, buttonForegroundColor);
		}
		if(buttonBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorButtons(dse, buttonBackgroundColor);
		}
		dse.setComponentValue(ds);
		orderedList.add(dse);
		this.add(dse);
	}
	public void addParamFile(FileSelection fs)
	{
		FileSelectionEditor fse = new FileSelectionEditor();
		if(buttonForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(fse, buttonForegroundColor);
		}
		if(buttonBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorButtons(fse, buttonBackgroundColor);
		}
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
