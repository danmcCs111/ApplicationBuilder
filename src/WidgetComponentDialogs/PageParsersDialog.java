package WidgetComponentDialogs;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPanel;

import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;

public class PageParsersDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
	private JPanel arrayPageParser = new JPanel();
	
	public PageParsersDialog()
	{
		buildWidgets();
		
		this.setVisible(true);
	}
	
	private void buildWidgets()
	{
		this.setLayout(new GridLayout(0,1));
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		
		PageParserEditor ppe = new PageParserEditor();
		ppe.setComponentValue(getPageParser());
		
		arrayPageParser.add(ppe);
		
		this.add(arrayPageParser);
	}
	
	private PageParser getPageParser()
	{
		PageParser youtube = new PageParser("");
		youtube.setTitleLabel("Youtube");
		youtube.setDomainMatch("youtube.com");
		youtube.addMatchAndReplace(ParseAttribute.Image, "https://yt3.googleusercontent.com([^\"])*(\")", 
				new ArrayList<String>(Arrays.asList(new String [] {"\""}))
				);
		youtube.addMatchAndReplace(ParseAttribute.Title, "<title>([^<])*</title>", 
				new ArrayList<String>(Arrays.asList(new String [] {"<title>","</title>","[^a-zA-Z0-9\\-\\s]"}))
				);
		
		return youtube;
	}
	
	public static void main(String [] args)
	{
		new PageParsersDialog();
	}

}
