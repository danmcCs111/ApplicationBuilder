package WidgetComponents;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Editors.DirectorySelectionEditor;
import Editors.FileSelectionEditor;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import WidgetComponents.ParameterExtractor.StringBuildOption;

public class Parameter extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Component> orderedList = new ArrayList<Component>();
	
	public Parameter()
	{
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.setLayout(new GridLayout(1,0));
		GraphicsUtil.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
	}
	
	public void addParamString(String param)
	{
		JTextField paramText = new JTextField(param);
		GraphicsUtil.setForegroundColorButtons(paramText, ColorTemplate.getButtonForegroundColor());
		GraphicsUtil.setBackgroundColorPanel(paramText, ColorTemplate.getPanelBackgroundColor());
		orderedList.add(paramText);
		this.add(paramText);
	}
	public void addParamDirectory(DirectorySelection ds)
	{
		DirectorySelectionEditor dse = new DirectorySelectionEditor();
		GraphicsUtil.setForegroundColorButtons(dse, ColorTemplate.getButtonForegroundColor());
		GraphicsUtil.setBackgroundColorButtons(dse, ColorTemplate.getButtonBackgroundColor());
		dse.setComponentValue(ds);
		orderedList.add(dse);
		this.add(dse);
	}
	public void addParamFile(FileSelection fs)
	{
		FileSelectionEditor fse = new FileSelectionEditor();
		GraphicsUtil.setForegroundColorButtons(fse, ColorTemplate.getButtonForegroundColor());
		GraphicsUtil.setBackgroundColorButtons(fse, ColorTemplate.getButtonBackgroundColor());
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
