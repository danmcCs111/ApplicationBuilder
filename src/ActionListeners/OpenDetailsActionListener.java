package ActionListeners;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import ApplicationBuilder.LoggingMessages;
import WidgetComponents.ParameterEditor;
import WidgetComponents.ParameterEditorParser;

public class OpenDetailsActionListener implements ActionListener{

	private JButton openDetails;
	private JList<?> componentMethods;
	
	public OpenDetailsActionListener(JButton openDetails, JList<?> componentMethods)
	{
		this.openDetails = openDetails;
		this.componentMethods = componentMethods;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ListSelectionModel lsm = componentMethods.getSelectionModel();
		if(!lsm.isSelectionEmpty())
		{
			LoggingMessages.printOut(componentMethods.getSelectedValue().toString());
			ArrayList<String> methodParams = ParameterEditorParser.parseMethodParamsToList(componentMethods.getSelectedValue().toString());
			JFrame editorFrame = ParameterEditorParser.launchEditor(methodParams.get(0));
			int count = 0;
			JPanel innerPanel = new JPanel();
//			GridLayout gl = new GridLayout(0, 2);
			GridLayout gl = new GridLayout(0, 1);
			innerPanel.setLayout(gl);
			
			for(String s : methodParams.subList(1, methodParams.size()))
			{
				ParameterEditor pe = ParameterEditorParser.getParameterEditor(s);
				if(pe != null )
				{
					Component c = pe.getComponentEditor();
					if(c != null)
					{
//						JLabel l = pe.getFieldLabel("arg" + count + ":");
//						innerPanel.add(l);
						innerPanel.add(c);
						count++;
					}
				}
			}
			editorFrame.add(innerPanel, BorderLayout.NORTH);
		}
		openDetails.setEnabled(false);
	}

}
