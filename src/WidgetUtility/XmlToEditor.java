package WidgetUtility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import ApplicationBuilder.ApplicationLayoutEditor;
import Params.ParameterEditor;
import Params.XmlToWidgetGenerator;

public class XmlToEditor 
{
	public static final String 
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	
	private List<WidgetCreatorProperty> widgetCreatorProperties;
	private ApplicationLayoutEditor editorFrame;
	private JTabbedPane jtPane;
	
	public XmlToEditor(List<WidgetCreatorProperty> widgetCreatorProperties, ApplicationLayoutEditor editorFrame)
	{
		this.widgetCreatorProperties = widgetCreatorProperties;
		this.editorFrame = editorFrame;
	}
	
	public void destroyEditors()
	{
		if(jtPane != null)
		{
			jtPane.removeAll();
			editorFrame.remove(jtPane);
			editorFrame.repaint();
			editorFrame.validate();
		}
	}
	
	public JTabbedPane getTabbedPane()
	{
		return this.jtPane;
	}
	
	public int getSelectedIndex()
	{
		return jtPane.getSelectedIndex();
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
				Component [] cs = new Component [xwg.getParameterEditors().size()];
				JPanel pi = new JPanel(new BorderLayout());
				JButton del = new JButton();
				
				l.setText(metName);
				del.setText(DELETE_BUTTON_TEXT);
				pi.add(del, BorderLayout.WEST);
				pi.add(l, BorderLayout.CENTER);
				del.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						for(Component c : cs) if(c != null) p.remove(c);
						pi.remove(l);
						pi.remove(del);
						p.repaint();
						editorFrame.validate();
						wcp.getXmlToWidgetGenerators().remove(xwg);//remove from generators for saving removal
						
						editorFrame.updatePropertyEditor();
					}
				});
				
				p.add(pi);
				
				for(int i = 0; i < xwg.getParameterEditors().size(); i++)
				{
					ParameterEditor pe = xwg.getParameterEditors().get(i);
					if(pe == null)
						continue;
					
					cs[i] = pe.getComponentEditor();
					pe.setComponentValue(convObjs.get(i));
					p.add(cs[i]);
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
