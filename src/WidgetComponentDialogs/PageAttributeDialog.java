package WidgetComponentDialogs;

import java.awt.Dimension;

import javax.swing.JDialog;

public class PageAttributeDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_TEXT = "Edit Page Parser Collection",
		ADD_PARSER_BUTTON_TEXT = "+ Add Page Parser",
		DELETE_PARSER_BUTTON_TEXT = "X",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
	public PageAttributeDialog()
	{
		
	}

}
