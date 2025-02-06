package WidgetUtility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import Params.XmlToWidgetGenerator;
import WidgetComponents.ParameterEditor;

public class XmlToEditor 
{
	public static final String 
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*";
	
	private List<WidgetCreatorProperty> widgetCreatorProperties;
	
	private JFrame editorFrame;
	private JTabbedPane jtPane;
	
	public XmlToEditor(List<WidgetCreatorProperty> widgetCreatorProperties, JFrame frame)
	{
		this.widgetCreatorProperties = widgetCreatorProperties;
		this.editorFrame = frame;
	}
	
	public void destroyEditors()
	{
		jtPane.removeAll();
		editorFrame.remove(jtPane);
		editorFrame.repaint();
		editorFrame.validate();
	}
	
	public JTabbedPane getTabbedPane()
	{
		return this.jtPane;
	}
	
	public void buildEditors()
	{
		jtPane = new JTabbedPane();
		int count = 0;
		for(WidgetCreatorProperty wcp : widgetCreatorProperties)
		{
			JPanel outP = new JPanel(new BorderLayout());
			JPanel p = new JPanel(new GridLayout(0,1));//rows, columns
			JScrollPane js = new JScrollPane(p);
			js.getVerticalScrollBar().setUnitIncrement(15);
			
			String compName = wcp.getRef() + COMPONENT_SUFFIX + count;
			for(XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
				String metName = xwg.getMethodName();
				ArrayList<Object> convObjs = xwg.getConvertedObjects();
				Label l = new Label();
				l.setText(metName);
				p.add(l);
				
				for(int i = 0; i < xwg.getParameterEditors().size(); i++)
				{
					ParameterEditor pe = xwg.getParameterEditors().get(i);
					if(pe == null)
						continue;
					Component c = pe.getComponentEditor();
					pe.setComponentValue(convObjs.get(i));
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
	
}
