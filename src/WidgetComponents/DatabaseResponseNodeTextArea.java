package WidgetComponents;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import HttpDatabaseResponse.DatabaseResponseNode;
import WidgetExtensionInterfaces.DatabaseResponseNodeListenerExtension;

public class DatabaseResponseNodeTextArea extends JTextArea implements DatabaseResponseNodeListenerExtension
{
	private static final long serialVersionUID = 3001L;
	
	private JTextArea textArea;
	private JScrollPane jsP;

	public DatabaseResponseNodeTextArea()
	{
		this.setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		jsP = new JScrollPane(textArea);
		this.add(jsP, BorderLayout.CENTER);
	}
	
	@Override
	public void setResults(ArrayList<ArrayList<DatabaseResponseNode>> results) 
	{
		StringBuilder sb = new StringBuilder();
		int count = 1;
		for(ArrayList<DatabaseResponseNode> drns : results)
		{
			if(drns.size() < 1)//TODO blehh.
				continue;
			sb.append("Result #" + count + "\n");
			for(DatabaseResponseNode dn : drns)
			{
				sb.append(dn.toString());
				sb.append("\n");
			}
			sb.append("\n");
			count++;
		}
		textArea.setText(sb.toString());
	}

}
