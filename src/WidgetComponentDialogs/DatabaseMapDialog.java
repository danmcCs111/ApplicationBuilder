package WidgetComponentDialogs;

import java.util.ArrayList;

import WidgetComponentInterfaces.EditButtonArrayUrls;
import WidgetComponents.CollectionEditor;

public class DatabaseMapDialog extends CollectionEditor
{
	private static final long serialVersionUID = 1L;
	
	private static String
		ADD_ATTRIBUTE_TEXT = "Enter New Attribute: ";
	
	public DatabaseMapDialog(String path, ArrayList<?> collection, EditButtonArrayUrls ebau, String title)
	{
		super(path, collection, ebau, title, ADD_ATTRIBUTE_TEXT, true);
	}
	
}
