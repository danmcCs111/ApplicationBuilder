package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import WidgetComponentInterfaces.EditButtonArrayUrls;

public class CollectionEditorAddPanel
{
	private static final String
		ADD_BUTTON_TEXT = "Add",
		ADD_BUTTON_TOOLTIP_TEXT = "Add new to collection";
	private static int 
		URL_COLUMNSIZE = 35;
	
	private JTextField 
		optionField;
	private ArrayList<String> 
		addOptions = new ArrayList<String>();
	private EditButtonArrayUrls 
		ebau;
	private String 
		labelText = "",
		path;

	public CollectionEditorAddPanel(JPanel southPanel, EditButtonArrayUrls ebau, String path,
			String labelText)
	{
		this.ebau = ebau;
		this.path = path;
		this.labelText = labelText;
		buildWidgets(southPanel);
	}
	
	private void buildWidgets(JPanel southPanel)
	{
		JPanel optionPanel = new JPanel();
		JLabel optionLabel = new JLabel(labelText);
		optionField = new JTextField();
		optionField.setColumns(URL_COLUMNSIZE);
		JButton addUrlButton = new JButton(ADD_BUTTON_TEXT);
		addUrlButton.setToolTipText(ADD_BUTTON_TOOLTIP_TEXT);
		addUrlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String option = optionField.getText();
				addOption(option);
				optionField.setText("");
			}
		});
		optionPanel.add(optionLabel);
		optionPanel.add(optionField);
		optionPanel.add(addUrlButton);

		southPanel.add(optionPanel);
	}
	
	private void addOption(String option)
	{
		addOptions.add(option);
		ebau.updateButtonArrayCollection(this.path, addOptions, null);
		addOptions.clear();
	}
}
