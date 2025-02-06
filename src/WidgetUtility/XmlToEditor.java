package WidgetUtility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.StringToObjectConverter;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ParameterEditor;
import WidgetComponents.ParameterEditorParser;

public class XmlToEditor 
{
	private List<WidgetCreatorProperty> widgetCreatorProperties;
	private HashMap<String, HashMap<String, ArrayList<ParameterEditor>>> 
		methodNameAndParameterEditors = new HashMap<String, HashMap<String, ArrayList<ParameterEditor>>>();
	private HashMap<String, HashMap<String, ArrayList<List<String>>>> 
		methodNameAndParamList = new HashMap<String, HashMap<String, ArrayList<List<String>>>>();
	private HashMap<String, HashMap<String, ArrayList<StringToObjectConverter>>> 
		methodNameAndStringToObjectConverters = new HashMap<String, HashMap<String, ArrayList<StringToObjectConverter>>>();
	
	private JFrame editorFrame;
	private JTabbedPane jtPane;
	
	public XmlToEditor(List<WidgetCreatorProperty> widgetCreatorProperties, JFrame frame)
	{
		this.widgetCreatorProperties = widgetCreatorProperties;
		this.editorFrame = frame;
		
		collectParameterEditors();
	}
	
	public void destroyEditors()
	{
		jtPane.removeAll();
		editorFrame.remove(jtPane);
		editorFrame.validate();
	}
	
	public void buildEditors()
	{
		jtPane = new JTabbedPane();
		int count = 0;
		for(String compName : methodNameAndParameterEditors.keySet())
		{
			JPanel outP = new JPanel(new BorderLayout());
			JPanel p = new JPanel(new GridLayout(0,1));//rows, columns
			JScrollPane js = new JScrollPane(p);
			js.getVerticalScrollBar().setUnitIncrement(15);
			
			LoggingMessages.printOut("BuildTab: " + compName + " " + 
					LoggingMessages.combine(methodNameAndParameterEditors.get(compName).keySet()));
			for(String metName : methodNameAndParameterEditors.get(compName).keySet())
			{
				Label l = new Label();
				l.setText(metName);
				p.add(l);
				
				ArrayList<ParameterEditor> tmpMP = methodNameAndParameterEditors.get(compName).get(metName);
				ArrayList<StringToObjectConverter> tmpSOC = methodNameAndStringToObjectConverters.get(compName).get(metName);
				ArrayList<List<String>> tmpPL = methodNameAndParamList.get(compName).get(metName);
				
				for(int i=0; i < tmpMP.size(); i++)
				{
					ParameterEditor pe = tmpMP.get(i);
					List<String> lst = tmpPL.get(i);
					
					Component c = pe.getComponentEditor();
					
					if(lst != null && !lst.isEmpty())
					{
						String [] args = (String[]) tmpPL.get(i).toArray(new String[lst.size()]);
						Object o = tmpSOC.get(i).conversionCall(args);
						if(o != null) 
						{
							pe.setComponentValue(o);
						}
					}
					p.add(c);
				}
			}
			outP.add(js, BorderLayout.CENTER);
			jtPane.add(outP);
			jtPane.setTitleAt(count++, compName);
		}
		
		editorFrame.add(jtPane, BorderLayout.CENTER);
		editorFrame.validate();
	}
	
	private void collectParameterEditors()
	{
		int count = 0;
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			String methodPrefix = wcp.getInstance().getClass().getName();
			
			HashMap<String, ArrayList<ParameterEditor>> tmpP = new HashMap<String, ArrayList<ParameterEditor>>();
			HashMap<String, ArrayList<List<String>>> tmpL = new HashMap<String, ArrayList<List<String>>>();
			HashMap<String, ArrayList<StringToObjectConverter>> tmpS = new HashMap<String, ArrayList<StringToObjectConverter>>();
			
			for (XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
				String keyName = methodPrefix + count;
				LoggingMessages.printOut(keyName);
				
				ArrayList<ParameterEditor> paramEdits = new ArrayList<ParameterEditor>();
				ArrayList<StringToObjectConverter> lst =  xwg.getObjectConverterList();
				
				for(StringToObjectConverter soc : lst)
				{
					ParameterEditor pe = ParameterEditorParser.getParameterEditor(soc.getDefinitionClass());
					if(pe != null) 
					{
						LoggingMessages.printOut(pe.toString());
						paramEdits.add(pe);
					}
					else 
					{
						LoggingMessages.printOut("Editor Not Found for: " + 
								soc.getDefinitionClass().getName());
					}
				}
				
				tmpP.put(xwg.getMethodName(), paramEdits);
				tmpL.put(xwg.getMethodName(), xwg.getParamsList());
				tmpS.put(xwg.getMethodName(), lst);
				
				methodNameAndParameterEditors.put(keyName, tmpP);
				methodNameAndParamList.put(keyName, tmpL);
				methodNameAndStringToObjectConverters.put(keyName, tmpS);
				LoggingMessages.printNewLine();
			}
			count++;
		}
	}
	
}
