package WidgetUtility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ApplicationBuilder.LoggingMessages;
import ClassDefintions.StringToObjectConverter;
import Params.XmlToWidgetGenerator;
import WidgetComponents.ParameterEditor;
import WidgetComponents.ParameterEditorParser;

public class XmlToEditor 
{
	private List<WidgetCreatorProperty> widgetCreatorProperties;
	private HashMap<String, ArrayList<ParameterEditor>> 
		methodNameAndParameterEditors = new HashMap<String, ArrayList<ParameterEditor>>();
	private HashMap<String, ArrayList<List<String>>> 
		methodNameAndParamList = new HashMap<String, ArrayList<List<String>>>();
	private HashMap<String, ArrayList<StringToObjectConverter>> 
		methodNameAndStringToObjectConverters = new HashMap<String, ArrayList<StringToObjectConverter>>();
	
	private JFrame editorFrame;
	
	public XmlToEditor(List<WidgetCreatorProperty> widgetCreatorProperties, JFrame frame)
	{
		this.widgetCreatorProperties = widgetCreatorProperties;
		this.editorFrame = frame;
		
		collectParameterEditors();
	}
	
	public HashMap<String, ArrayList<ParameterEditor>> getMethodNameAndParameterEditors()
	{
		return methodNameAndParameterEditors;
	}
	
	public void buildEditors()
	{
		JPanel outP = new JPanel(new BorderLayout());
		JPanel p = new JPanel(new GridLayout(0,1));//rows, columns
		JScrollPane js = new JScrollPane(p);
		js.getVerticalScrollBar().setUnitIncrement(15);
		
		for(String metName : methodNameAndParameterEditors.keySet())
		{
			Label l = new Label();
			l.setText(metName);
			p.add(l);
			
			ArrayList<ParameterEditor> tmpMP = methodNameAndParameterEditors.get(metName);
			ArrayList<StringToObjectConverter> tmpSOC = methodNameAndStringToObjectConverters.get(metName);
			ArrayList<List<String>> tmpPL = methodNameAndParamList.get(metName);
			
			for(int i=0; i < tmpMP.size(); i++)
			{
				ParameterEditor pe = tmpMP.get(i);
				List<String> lst = tmpPL.get(i);
				
				Component c = pe.getComponentEditor();
				c.setMaximumSize(new Dimension(350, 150));
				
				if(lst != null && !lst.isEmpty())
				{
					String [] args = (String[]) tmpPL.get(i).toArray(new String[lst.size()]);
					Object o = tmpSOC.get(i).conversionCall(args);
					if(o != null) 
					{
						LoggingMessages.printOut("Method: " + metName);
						LoggingMessages.printOut("Set component value: " + o.toString());
						LoggingMessages.printOut("Component: " + c);
						LoggingMessages.printNewLine();
						pe.setComponentValue(o);
					}
				}
				
				p.add(c);
			}
		}
		
		outP.add(js, BorderLayout.CENTER);
		editorFrame.add(outP, BorderLayout.CENTER);
		
		editorFrame.repaint();
		editorFrame.validate();
	}
	
	private void collectParameterEditors()
	{
		int count = 0;
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			String methodPrefix = wcp.getInstance().getClass().getName();
			
			for (XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
				String keyName = methodPrefix + count + xwg.getMethodName();
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
				methodNameAndParameterEditors.put(keyName, paramEdits);
				methodNameAndParamList.put(keyName, xwg.getParamsList());
				methodNameAndStringToObjectConverters.put(keyName, lst);
				LoggingMessages.printNewLine();
			}
			count++;
		}
	}
	
}
