package WidgetComponents;

import java.awt.FlowLayout;
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
	public static final String
		KEY_VALUE_DELIMITER = "=@=";
	
	private static final String
		ADD_BUTTON_TEXT = "Add",
		ADD_BUTTON_TOOLTIP_TEXT = "Add new to collection";
	private static int 
		URL_COLUMNSIZE = 35,
		KEY_COLUMNSIZE = 15,
		VALUE_COLUMNSIZE = 20;
	
	private JTextField 
		optionField,
		keyField,
		valueField;
	private ArrayList<String> 
		addOptions = new ArrayList<String>();
	private EditButtonArrayUrls 
		ebau;
	private String 
		labelText = "",
		path;

	public CollectionEditorAddPanel(JPanel southPanel, EditButtonArrayUrls ebau, String path, String labelText)
	{
		this(southPanel, ebau, path, labelText, false);
	}
	
	public CollectionEditorAddPanel(JPanel southPanel, EditButtonArrayUrls ebau, String path, String labelText, boolean keyValue)
	{
		this.ebau = ebau;
		this.path = path;
		this.labelText = labelText;
		buildWidgets(southPanel, keyValue);
	}
	
	private void buildWidgets(JPanel southPanel, boolean keyValue)
	{
		JPanel optionPanel = new JPanel();
		JLabel optionLabel = new JLabel(labelText);
		JButton addUrlButton = new JButton(ADD_BUTTON_TEXT);
		addUrlButton.setToolTipText(ADD_BUTTON_TOOLTIP_TEXT);
		
		optionPanel.add(optionLabel);
		if(keyValue)
		{
			JPanel keyValuePanel = new JPanel();
			keyValuePanel.setLayout(new FlowLayout());
			
			keyField = new JTextField();
			keyField.setColumns(KEY_COLUMNSIZE);
			valueField = new JTextField();
			valueField.setColumns(VALUE_COLUMNSIZE);
			
			addUrlButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String option = keyField.getText() + KEY_VALUE_DELIMITER + valueField.getText();
					addOption(option);
					keyField.setText("");
					valueField.setText("");
				}
			});
			keyValuePanel.add(keyField);
			keyValuePanel.add(valueField);
			
			optionPanel.add(keyValuePanel);
		}
		else
		{
			optionField = new JTextField();
			optionField.setColumns(URL_COLUMNSIZE);
			addUrlButton.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					String option = optionField.getText();
					addOption(option);
					optionField.setText("");
				}
			});
			optionPanel.add(optionField);
		}
		
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
