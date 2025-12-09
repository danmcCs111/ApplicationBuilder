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
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
		MATCH_LABEL =	"Match:      ",
		REPLACE_LABEL =	"Replace:  ",
		TITLE_LABEL =	"Title:  ",
		DOMAIN_LABEL =	"Domain: ",
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
		addMatchFilterButton = new ArrayList<JButton>(),
		addReplaceFilterButton = new ArrayList<JButton>();
	private JButton 
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	
	private HashMap<ParseAttribute, ArrayList<JButton>>
		addReplaceButton = new HashMap<ParseAttribute, ArrayList<JButton>>();
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>();
	private HashMap<ParseAttribute, JPanel> 
		matchFilterPanel = new HashMap<PageParser.ParseAttribute, JPanel>();
	private HashMap<String, JTextField> matchStringAndTextField = new HashMap<String, JTextField>();
	
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
		for(ParseAttribute pa : ParseAttribute.values())
		{
			JPanel matchPanel = new JPanel();
			matchPanel.setLayout(new GridLayout(0,1));
			matchFilterPanel.put(pa, matchPanel);
			
			JButton jbut = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + ADD_FILTER_MATCH_LABEL_SUFFIX);
			jbut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addMatchFilter(pa, pp, "");
				}
			});
			matchPanel.add(jbut);
			addMatchFilterButton.add(jbut);
			
			JButton jbut2 = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + ADD_FILTER_REPLACE_LABEL_SUFFIX);
			jbut2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addReplaceFilter(pa, pp, "");
				}
			});
			matchPanel.add(jbut2);
			addReplaceFilterButton.add(jbut2);
			
			innerPanel.add(matchPanel);
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
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(0,1));
		
		JLabel titleLabel = new JLabel(TITLE_LABEL);
		topPanel.add(titleLabel);
		topPanel.add(title);
		
		JLabel domainLabel = new JLabel(DOMAIN_LABEL);
		topPanel.add(domainLabel);
		topPanel.add(domain);
		
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
		this.add(innerPanel, BorderLayout.CENTER);
		this.add(topPanel, BorderLayout.NORTH);
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
	
	public void addMatchFilter(ParseAttribute pa, PageParser pp, String matchInitValue)
	{
		LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = addMatchField(
				matchFilterPanel.get(pa),
				pp.getMatchAndReplace(pa), 
				this.parserFilter.get(pa),
				matchInitValue);
		parserFilter.put(pa, replFields);
	}
	
	public void addReplaceFilter(ParseAttribute pa, PageParser pp, String replaceInitValue)
	{
		JTextField [] keys = this.parserFilter.get(pa).keySet().toArray(new JTextField[] {});
		
		LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = addReplacementField(
				matchFilterPanel.get(pa),
				pp.getMatchAndReplace(pa), 
				this.parserFilter.get(pa),
				keys[keys.length-1].getText(),
				replaceInitValue);
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
					matchFilterPanel.get(pa),
					pp.getMatchAndReplace(pa), this.parserFilter.get(pa));
			parserFilter.put(pa, replFields);
		}
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> constructMatchAndReplacementFields(
			JComponent parentComponent,
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
				parserFilter = addMatchField(parentComponent, replacementFields, parserFilter, match);
				
				for(String repl : replacementFields.get(match))
				{
					parserFilter = addReplacementField(parentComponent, replacementFields, parserFilter, match, repl);
				}
				
			}
		}
		return parserFilter;
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> addMatchField(
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter,
			String fieldText
			)
	{
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new BorderLayout());
		JLabel matchLabel = new JLabel(MATCH_LABEL);
		JTextField matchField = new JTextField(fieldText);
		matchPanel.add(matchLabel, BorderLayout.WEST);
		matchPanel.add(matchField, BorderLayout.CENTER);
		parentComponent.add(matchPanel);
		
		if(parserFilter == null)
		{
			parserFilter = new LinkedHashMap<JTextField, ArrayList<JTextField>>();
		}
		matchStringAndTextField.put(fieldText, matchField);
		parserFilter.put(matchField, new ArrayList<JTextField>());
		
		this.validate();
		
		return parserFilter;
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> addReplacementField(
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter,
			String match,
			String fieldText
			)
	{
		JLabel replLabel = new JLabel(REPLACE_LABEL);
		JTextField replField = new JTextField(fieldText);
		ArrayList<JTextField> replFields = parserFilter.get(matchStringAndTextField.get(match));
		replFields.add(replField);
		
		JPanel replPanel = new JPanel();
		replPanel.setLayout(new BorderLayout());
		replPanel.add(replLabel, BorderLayout.WEST);
		replPanel.add(replField, BorderLayout.CENTER);
		parentComponent.add(replPanel);
		
		parserFilter.put(matchStringAndTextField.get(match), replFields);
		
		this.validate();
		
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
