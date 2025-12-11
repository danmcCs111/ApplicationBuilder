package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;

public class PageParsersDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static String
		ADD_PARSER_BUTTON_TEXT = "+ Add Page Parser",
		DELETE_PARSER_BUTTON_TEXT = "X";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
	private JPanel arrayPageParser = new JPanel();
	private HashMap<PageParser, JComponent> pageParserAndComponent = new HashMap<PageParser, JComponent>();
	private ArrayList<PageParser> pageParsers = new ArrayList<PageParser>();
	
	public PageParsersDialog()
	{
		addPageParsers();
		buildWidgets();
		this.setVisible(true);
	}
	
	private void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		arrayPageParser.setLayout(new GridLayout(0,1));
		JButton addParserButton = new JButton(ADD_PARSER_BUTTON_TEXT);
		addParserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPageParser(new PageParser(""));
				PageParsersDialog.this.validate();
			}
		});
		this.arrayPageParser.add(addParserButton);
		for(PageParser pp : getPageParsers())
		{
			addPageParser(pp);
		}
		this.add(arrayPageParser, BorderLayout.NORTH);
	}
	
	private ArrayList<PageParser> getPageParsers()
	{
		return this.pageParsers;
	}
	
	private void addPageParser(PageParser pp)
	{
		PageParserEditor ppe = new PageParserEditor();
		ppe.setComponentValue(pp);
		
		JPanel inner = new JPanel();
		inner.setLayout(new BorderLayout());
		JButton delButton = new JButton(DELETE_PARSER_BUTTON_TEXT);
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removePageParser(pp, ppe);
			}
		});
		inner.add(delButton, BorderLayout.WEST);
		inner.add(ppe, BorderLayout.CENTER);
		pageParserAndComponent.put(pp, inner);
		arrayPageParser.add(inner);
	}
	
	private void addPageParsers()//TODO test
	{
		PageParser youtube = new PageParser("");
		PageParser youtube2 = new PageParser("");
		youtube.setTitleLabel("Youtube");
		youtube.setDomainMatch("youtube.com");
		youtube.addMatchAndReplace(ParseAttribute.Image, "https://yt3.googleusercontent.com([^\"])*(\")", 
				new ArrayList<String>(Arrays.asList(new String [] {"\""}))
				);
		youtube.addMatchAndReplace(ParseAttribute.Title, "<title>([^<])*</title>", 
				new ArrayList<String>(Arrays.asList(new String [] {"<title>","</title>","[^a-zA-Z0-9\\-\\s]"}))
				);
		
		youtube2.setTitleLabel("Youtube2");
		youtube2.setDomainMatch("youtube.com");
		youtube2.addMatchAndReplace(ParseAttribute.Image, "https://yt3.googleusercontent.com([^\"])*(\")", 
				new ArrayList<String>(Arrays.asList(new String [] {"\""}))
				);
		youtube2.addMatchAndReplace(ParseAttribute.Title, "<title>([^<])*</title>", 
				new ArrayList<String>(Arrays.asList(new String [] {"<title>","</title>","[^a-zA-Z0-9\\-\\s]"}))
				);
		
		this.pageParsers.add(youtube);
		this.pageParsers.add(youtube2);
	}
	
	private void removePageParser(PageParser pp, PageParserEditor ppe)
	{
		arrayPageParser.remove(pageParserAndComponent.get(pp));
		this.pageParsers.remove(pp);
		
		this.validate();
	}
	
	public static void main(String [] args)
	{
		new PageParsersDialog();
	}

}
