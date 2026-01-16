package WidgetComponentDialogs;

import java.awt.Dimension;
import java.util.ArrayList;

import WidgetComponentInterfaces.EditButtonArrayUrls;
import WidgetComponents.CollectionEditor;

public class PageAttributeDialog extends CollectionEditor
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_TEXT = "Edit Page Attribute",
		ADD_ATTRIBUTE_TEXT = "Enter New Attribute: ",
		ADD_PARSER_BUTTON_TEXT = "+ Add Page Parser",
		DELETE_PARSER_BUTTON_TEXT = "X",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
	private static PageAttributeDialog pad = null;
	private static EditButtonArrayUrls ebau;
	
	public PageAttributeDialog(String path, ArrayList<?> collection, EditButtonArrayUrls ebau, String title) 
	{
		super(path, collection, ebau, title, ADD_ATTRIBUTE_TEXT);
	}
	
	public static void main(String [] args)
	{
		ArrayList<String> collection = new ArrayList<String>();
		collection.add("URL");
		collection.add("Title");
		ebau = new EditButtonArrayUrls() {
			@Override
			public void updateButtonArrayCollection(String path, ArrayList<String> addUrls, ArrayList<?> remove) 
			{
				for(String add : addUrls)
				{
					collection.add(add);
				}
				pad.dispose();
				pad = new PageAttributeDialog(".", collection, ebau, TITLE_TEXT);
			}
		};
		pad = new PageAttributeDialog(".", collection, ebau, TITLE_TEXT);
	}

}
