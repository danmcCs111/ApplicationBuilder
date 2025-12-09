package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.PageParser;
import HttpDatabaseRequest.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;

public class PageParserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Command Entry",
		ADD_FILTER_LABEL_PREFIX = " + ",
		ADD_FILTER_MATCH_LABEL_SUFFIX = " Match Filter",
		ADD_FILTER_REPLACE_LABEL_SUFFIX = " Replace Filter",
		PARAM_DELETE_TEXT = "X",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField 
		title = new JTextField(),
		domain = new JTextField();
	private ArrayList<JButton>
		addFilterButton = new ArrayList<JButton>();
	private HashMap<ParseAttribute, ArrayList<JButton>>
		addReplaceButton = new HashMap<ParseAttribute, ArrayList<JButton>>();
	private JButton 
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>();
	
	private String retSelection = null;
	private PageParserEditor pageParserEditor;
	private PageParser pageParser = null;

	public PageParserDialog(PageParserEditor cbe, PageParser pp)
	{
		this.pageParser = pp;
		buildWidgets(cbe, pp);
	}
	
	private void buildWidgets(PageParserEditor cbe, PageParser pp)
	{
		this.pageParserEditor = cbe;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocation(cbe.getRootPane().getParent().getLocation());
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		innerPanel.setLayout(new GridLayout(0, 1));
		
		this.add(innerPanel, BorderLayout.NORTH);
		innerPanel.add(title);
		innerPanel.add(domain);
		for(ParseAttribute pa : ParseAttribute.values())
		{
			JButton jbut = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + ADD_FILTER_MATCH_LABEL_SUFFIX);
			jbut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addFilter(pa, pp);
				}
			});
			innerPanel.add(jbut);
			addFilterButton.add(jbut);
		}
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		saveCancelPanel.add(saveButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		saveCancelPanel.add(cancelButton);
		
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
		this.add(saveCancelPanelOuter, BorderLayout.SOUTH);
		
		if(pp != null)
		{
			constructPageParser(pp);
		}
		
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		
		this.setVisible(true);
	}
	
	public void addFilter(ParseAttribute pa, PageParser pp)
	{
		LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = constructMatchAndReplacementFields(
				pp.getMatchAndReplace(pa), this.parserFilter.get(pa));
		parserFilter.put(pa, replFields);
	}
	
	public PageParser getPageParser()
	{
		return buildPageParser();
	}
	
	public void setPageParser(PageParser pageParser)
	{
		this.pageParser = pageParser;
		constructPageParser(this.pageParser);
	}
	
	private PageParser buildPageParser()
	{
		PageParser pp = new PageParser("");
		pp.setTitleLabel(title.getText());
		pp.setDomainMatch(domain.getText());
		for(ParseAttribute pa : parserFilter.keySet())
		{
			for(JTextField key : parserFilter.get(pa).keySet())
			{
				ArrayList<String> repls = new ArrayList<String>();
				for(JTextField jt : parserFilter.get(pa).get(key))
				{
					repls.add(jt.getText());
				}
				pp.addMatchAndReplace(pa, key.getText(), repls);
			}
		}
		return pp;
	}
	
	private void constructPageParser(PageParser pp)
	{
		title.setText(pp.getTitleLabel());
		domain.setText(pp.getDomainMatch());
		for(ParseAttribute pa : pp.getParseAttributes())
		{
			LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = constructMatchAndReplacementFields(
					pp.getMatchAndReplace(pa), this.parserFilter.get(pa));
			parserFilter.put(pa, replFields);
		}
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> constructMatchAndReplacementFields(
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter)
	{
		if(replacementFields == null)
		{
			JTextField matchField = new JTextField();
			ArrayList<JTextField> replFields = new ArrayList<JTextField>();
			parserFilter.put(matchField, replFields);
		}
		else
		{
			for(String match : replacementFields.keySet())
			{
				JTextField matchField = new JTextField(match);
				ArrayList<JTextField> replFields = new ArrayList<JTextField>();
				for(String repl : replacementFields.get(match))
				{
					replFields.add(new JTextField(repl));
				}
				parserFilter.put(matchField, replFields);
			}
		}
		return parserFilter;
	}
	
	private void saveAction()
	{
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}
	
}
