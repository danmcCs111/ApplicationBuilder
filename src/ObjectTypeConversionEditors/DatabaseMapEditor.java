package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;

import Graphics2D.ColorTemplate;
import Params.ParameterEditor;
import Properties.DatabaseMap;
import Properties.LoggingMessages;
import WidgetComponentDialogs.DatabaseMapDialog;
import WidgetComponentInterfaces.EditButtonArrayUrls;
import WidgetComponents.CollectionEditorAddPanel;

public class DatabaseMapEditor extends JButton implements ParameterEditor, EditButtonArrayUrls
{
	private static final long serialVersionUID = 1L;

	private static final String 
		DISPLAY_DELIMITER = ",",
		TITLE_TEXT = "Edit Field and Database Column Mapping",
		DEFAULT_EDITOR_TEXT = "<Enter Database Mapping>";
	
	private DatabaseMap
		databaseMap;
	private DatabaseMapDialog
		databaseMapDialog;
	
	public DatabaseMapEditor()
	{
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(databaseMapDialog != null)
				{
					databaseMapDialog.dispose();
				}
				databaseMapDialog = new DatabaseMapDialog(
						"", 
						combineHashMap(databaseMap.getParseFieldsAndDbColumn()), 
						DatabaseMapEditor.this, 
						TITLE_TEXT
				);
				databaseMapDialog.setLocation(DatabaseMapEditor.this.getLocationOnScreen());
				databaseMapDialog.setVisible(true);
			}
		});
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
	}
	
	private static ArrayList<String> combineHashMap(HashMap<String, String> fieldAndColumns)
	{
		ArrayList<String> collection = new ArrayList<String>();
		for(String key : fieldAndColumns.keySet())
		{
			String column = fieldAndColumns.get(key);
			collection.add(key + DISPLAY_DELIMITER + column);
		}
		return collection;
	}
	
	private void rebuildDatabaseMapEditor()
	{
		if(databaseMap.getParseFieldsAndDbColumn().isEmpty())
		{
			this.setText(DEFAULT_EDITOR_TEXT);
		}
		else
		{
			this.setText(databaseMap.getXmlString());
		}
		if(databaseMapDialog != null)
		{
			databaseMapDialog.dispose();
		}
		databaseMapDialog = new DatabaseMapDialog(
				"", 
				combineHashMap(databaseMap.getParseFieldsAndDbColumn()), 
				DatabaseMapEditor.this, 
				TITLE_TEXT
		);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		if(value == null)
			return;
		
		databaseMap = (DatabaseMap) value;
		String title = databaseMap.getXmlString().isBlank()
				? DEFAULT_EDITOR_TEXT
				: databaseMap.getXmlString();
		if(databaseMapDialog != null)
		{
			databaseMapDialog.dispose();
		}
		this.setText(title);
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {databaseMap.getXmlString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return databaseMap;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return DatabaseMap.class.getName();
	}

	@Override
	public void updateButtonArrayCollection(String path, ArrayList<String> addUrls, ArrayList<?> remove) 
	{
		//TODO.
		if(addUrls != null)
		{
			for(String add : addUrls)
			{
				String [] keyValue = add.split(CollectionEditorAddPanel.KEY_VALUE_DELIMITER);
				databaseMap.addFieldAndColumnDefinition(keyValue[0], keyValue[1]);
			}
		}
		if(remove != null)
		{
			for(Object rem : remove)
			{
				LoggingMessages.printOut(rem + " remove.");
				String [] keyValue = rem.toString().split(DISPLAY_DELIMITER);
				for(String key : databaseMap.getParseFieldsAndDbColumn().keySet())
				{
					if(keyValue[0].toString().equals(key))
					{
						LoggingMessages.printOut(key + " matches");
						databaseMap.getParseFieldsAndDbColumn().remove(key);
					}
				}
			}
		}
		rebuildDatabaseMapEditor();
	}
	
}
