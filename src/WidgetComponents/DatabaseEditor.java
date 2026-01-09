package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import HttpDatabaseResponse.DatabaseResponseNode;
import ObjectTypeConversion.StringToObjectConverter;
import Params.ParamTypes;
import Params.ParameterEditor;
import Params.ParameterEditorParser;
import Properties.LoggingMessages;
import WidgetExtensionInterfaces.DatabaseResponseNodeListenerExtension;

public class DatabaseEditor extends JScrollPane implements DatabaseResponseNodeListenerExtension
{
	private static final long serialVersionUID = 1L;
	private static final String
		ATTRIBUTE_KEY_CLASS_TYPE = "classType",
		ATTRIBUTE_KEY_CONTENT = "content";
	private static int 
		SCROLL_INC = 15;
	
	private JPanel parentPanel;

	public DatabaseEditor()
	{
		super();
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		parentPanel = new JPanel();
		parentPanel.setLayout(new GridLayout(0,1));
		this.getVerticalScrollBar().setUnitIncrement(SCROLL_INC);
		this.setViewportView(parentPanel);
	}
	
	@Override
	public void setResults(ArrayList<ArrayList<DatabaseResponseNode>> results) 
	{
		parentPanel.removeAll();
		for(ArrayList<DatabaseResponseNode> result : results)
		{
			JPanel 
				resultPanel = new JPanel(),
				innerPanel = new JPanel();
			resultPanel.setLayout(new BorderLayout());
			
			for(DatabaseResponseNode drn : result)
			{
				HashMap<String, String> attrs = drn.getNodeAttributes();
				String 
					label = drn.getNodeName(),
					classType = attrs.get(ATTRIBUTE_KEY_CLASS_TYPE),
					content = attrs.get(ATTRIBUTE_KEY_CONTENT);
				JLabel resLabel = new JLabel(label);
				LoggingMessages.printOut(content + "");
				ParamTypes pt = ParamTypes.getParamType(classType);
				ParameterEditor pe = ParameterEditorParser.getParameterEditor(classType);
				StringToObjectConverter soc = pt.getConverter();
				pe.setComponentValue(soc.conversionCall(content));
				
				innerPanel.add(resLabel);
				innerPanel.add((Component) pe);
			}
			LoggingMessages.printOut("add result panel");
			resultPanel.add(innerPanel, BorderLayout.WEST);
			parentPanel.add(resultPanel);
		}
		this.revalidate();
	}
	
}
