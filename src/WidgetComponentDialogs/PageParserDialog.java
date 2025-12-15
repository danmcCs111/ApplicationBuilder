package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.HttpDatabaseRequest;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;
import Properties.LoggingMessages;

public class PageParserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Edit Page Parsing Entry",
		ADD_FILTER_LABEL_PREFIX = " + ",
		ADD_FILTER_MATCH_LABEL_SUFFIX = " Match",
		ADD_FILTER_REPLACE_LABEL_SUFFIX = " Replace",
		MATCH_LABEL =	"Match:      ",
		REPLACE_LABEL =	"Replace:  ",
		TITLE_LABEL =	"Title:  ",
		DOMAIN_LABEL =	"Domain: ",
		PARAM_DELETE_TEXT = "X Remove",
		PARAM_DELETE_TOOLTIP = "Remove Last Filter",
		SIMULATE_LABEL = "Simulate",
		SIMULATE_TITLE_STRIPPED = "Title Filtered: ",
		SIMULATE_IMAGE_STRIPPED = "Image Filtered: ",
		SIMULATE_URL_ENTRY_STRIPPED = "Simulate with Url: ",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField 
		simulateTextField = new JTextField(20),
		simulateTitleTextField = new JTextField(20),
		simulateImageTextField = new JTextField(20),
		title = new JTextField(),
		domain = new JTextField();
	private JButton 
		simulateButton = new JButton(SIMULATE_LABEL),
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField[]>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField[]>>>();
	private HashMap<ParseAttribute, JComponent> 
		matchFilterPanel = new HashMap<PageParser.ParseAttribute, JComponent>();
	
	private PageParserEditor pageParserEditor;
	private PageParser pageParser = null;

	public PageParserDialog(PageParserEditor ppe, PageParser pp)
	{
		this.pageParser = pp;
		buildWidgets(ppe, pp);
	}
	
	private void buildWidgets(PageParserEditor ppe, PageParser pp)
	{
		Point parentLocation = null;
		this.pageParserEditor = ppe;
		if(ppe.getRootPane() == null || ppe.getRootPane().getParent() == null)
		{
			return;
		}
		parentLocation = ppe.getRootPane().getParent().getLocation();
		this.setLocation(parentLocation);
		
		this.pageParserEditor = ppe;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		innerPanel.setLayout(new GridLayout(0, 1));
		for(ParseAttribute pa : ParseAttribute.values())
		{
			JPanel matchPanel = new JPanel();
			JPanel addMatchReplace = new JPanel();
			addMatchReplace.setLayout(new FlowLayout());
			
			matchPanel.setLayout(new GridLayout(0,1));
			matchFilterPanel.put(pa, matchPanel);
			
			JPanel inner = buildFilterButton(pa, MatchOrReplace.match);
			JPanel inner2 = buildFilterButton(pa, MatchOrReplace.replace);
			JPanel inner3 = buildDeleteButton(pa);
			
			addMatchReplace.add(inner);
			addMatchReplace.add(inner2);
			addMatchReplace.add(inner3);
			matchPanel.add(addMatchReplace);
			
			innerPanel.add(matchPanel);
		}
		
		buildSaveCancel();
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(0,1));
		
		JLabel titleLabel = new JLabel(TITLE_LABEL);
		topPanel.add(titleLabel);
		topPanel.add(title);
		
		JLabel domainLabel = new JLabel(DOMAIN_LABEL);
		topPanel.add(domainLabel);
		topPanel.add(domain);
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		outerPanel.add(innerPanel, BorderLayout.NORTH);
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(outerPanel, BorderLayout.CENTER);
		this.add(saveCancelPanelOuter, BorderLayout.SOUTH);
		
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		
		constructPageParser(pp);
	}
	
	private JPanel buildFilterButton(ParseAttribute pa, MatchOrReplace mor)
	{
		String suffix = null;
		switch(mor)
		{
		case match:
			suffix = ADD_FILTER_MATCH_LABEL_SUFFIX;
			break;
		case replace:
			suffix = ADD_FILTER_REPLACE_LABEL_SUFFIX;
			break;
		}
		JButton jbut2 = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + suffix);
		jbut2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addFilter(pa, getPageParser(), new String[]{"",""}, mor);
			}
		});
		JPanel inner2 = new JPanel();
		inner2.setLayout(new BorderLayout());
		inner2.add(jbut2, BorderLayout.NORTH);
		
		return inner2;
	}
	
	private JPanel buildDeleteButton(ParseAttribute pa)
	{
		JButton remButton = new JButton(PARAM_DELETE_TEXT);
		remButton.setToolTipText(PARAM_DELETE_TOOLTIP);
		remButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteLast(pa);
			}
		});
		JPanel inner2 = new JPanel();
		inner2.setLayout(new BorderLayout());
		inner2.add(remButton, BorderLayout.NORTH);
		
		return inner2;
	}
	
	private void buildSaveCancel()
	{
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
		JPanel simulatePanel = buildSimulatePanel();
		
		saveCancelPanelOuter.add(simulatePanel, BorderLayout.NORTH);
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
	}
	
	public JPanel buildSimulatePanel()
	{
		//TODO
		JPanel simulatePanel = new JPanel();
		simulatePanel.setLayout(new GridLayout(0,1));
		simulateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulateAction(simulateTextField.getText());
			}
		});
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		simulatePanel.setBorder(b);
		
		JPanel innerPanelTitle = new JPanel();
		innerPanelTitle.setLayout(new BorderLayout());
		simulateTitleTextField.setEditable(false);
		innerPanelTitle.add(new JLabel(SIMULATE_TITLE_STRIPPED), BorderLayout.WEST);
		innerPanelTitle.add(simulateTitleTextField, BorderLayout.CENTER);
		simulatePanel.add(innerPanelTitle);
		
		JPanel innerPanelImage = new JPanel();
		innerPanelImage.setLayout(new BorderLayout());
		simulateImageTextField.setEditable(false);
		innerPanelImage.add(new JLabel(SIMULATE_IMAGE_STRIPPED), BorderLayout.WEST);
		innerPanelImage.add(simulateImageTextField, BorderLayout.CENTER);
		simulatePanel.add(innerPanelImage);
		
		JPanel innerPanelSimulate = new JPanel();
		innerPanelSimulate.setLayout(new BorderLayout());
		innerPanelSimulate.add(new JLabel(SIMULATE_URL_ENTRY_STRIPPED), BorderLayout.WEST);
		innerPanelSimulate.add(simulateTextField, BorderLayout.CENTER);
		innerPanelSimulate.add(simulateButton, BorderLayout.EAST);
		simulatePanel.add(innerPanelSimulate);
		
		return simulatePanel;
	}
	
	public void addFilter(ParseAttribute pa, PageParser pp, String [] matchInitValue, MatchOrReplace mor)
	{
		JTextField lastMatchText = getLastMatchTextField(pa);
		LinkedHashMap<JTextField, ArrayList<JTextField[]>> replFields = addMatchReplaceField(
				pa,
				matchFilterPanel.get(pa),
				pp.getMatchAndReplace(pa), 
				this.parserFilter.get(pa),
				lastMatchText,
				matchInitValue,
				mor);
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
	
	private JTextField getLastMatchTextField(ParseAttribute pa)
	{
		if(parserFilter.size() == 0 || parserFilter.get(pa) == null || parserFilter.get(pa).size() == 0)
			return null;
		
		JTextField [] keys = this.parserFilter.get(pa).keySet().toArray(new JTextField[] {});
		return keys[keys.length-1];
	}
	
	private void deleteLast(ParseAttribute pa)
	{
		JTextField lastMatch = getLastMatchTextField(pa);
		if(lastMatch == null)
			return;
		
		ArrayList<JTextField[]> repls = parserFilter.get(pa).get(lastMatch);
		if(repls == null || repls.isEmpty())
		{
			//delete match
			LoggingMessages.printOut(lastMatch.getText());
			matchFilterPanel.get(pa).remove(lastMatch.getParent().getParent());
			parserFilter.get(pa).remove(lastMatch);
		}
		else
		{
			//delete last repl
			JTextField [] lastRepl = repls.get(repls.size()-1);
			matchFilterPanel.get(pa).remove(lastRepl[0].getParent().getParent());
			repls.remove(lastRepl);
			LoggingMessages.printOut(lastRepl[0].getText() + " : " + lastRepl[1].getText());
		}
		this.validate();
	}
	
	private PageParser buildPageParser()
	{
		PageParser pp = new PageParser("");
		pp.setTitleLabel(title.getText());
		pp.setDomainMatch(domain.getText());
		for(ParseAttribute pa : parserFilter.keySet())
		{
			if(parserFilter.get(pa) == null)
			{
				parserFilter.put(pa, new LinkedHashMap<JTextField, ArrayList<JTextField[]>>());
			}
			for(JTextField key : parserFilter.get(pa).keySet())
			{
				if(key == null)
					break;
				ArrayList<String[]> repls = new ArrayList<String[]>();
				LoggingMessages.printOut(key + " is key");
				for(JTextField [] jt : parserFilter.get(pa).get(key))
				{
					repls.add(new String[] {jt[0].getText(), jt[1].getText()});
				}
				pp.addMatchAndReplace(pa, key.getText(), repls);
			}
		}
		return pp;
	}
	
	private void constructPageParser(PageParser pp)
	{
		if(pp == null)
		{
			pp = new PageParser("");
			setPageParser(pp);
		}
		title.setText(pp.getTitleLabel());
		domain.setText(pp.getDomainMatch());
		for(ParseAttribute pa : pp.getParseAttributes())
		{
			LinkedHashMap<JTextField, ArrayList<JTextField[]>> replFields = constructMatchAndReplacementFields(
					pa,
					matchFilterPanel.get(pa),
					pp.getMatchAndReplace(pa), 
					this.parserFilter.get(pa)
			);
			parserFilter.put(pa, replFields);
		}
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField[]>> constructMatchAndReplacementFields(
			ParseAttribute pa,
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String[]>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField[]>> parserFilter)
	{
		if(replacementFields == null)
		{
			JTextField matchField = new JTextField();
			ArrayList<JTextField[]> replFields = new ArrayList<JTextField[]>();
			parserFilter.put(matchField, replFields);
		}
		else
		{
			for(String match : replacementFields.keySet())
			{
				parserFilter = addMatchReplaceField(
						pa, parentComponent, replacementFields, parserFilter, getLastMatchTextField(pa), new String [] {match}, MatchOrReplace.match);
				this.parserFilter.put(pa, parserFilter);
				
				for(String [] repl : replacementFields.get(match))
				{
					parserFilter = addMatchReplaceField(
							pa, parentComponent, replacementFields, parserFilter, getLastMatchTextField(pa), repl, MatchOrReplace.replace);
					this.parserFilter.put(pa, parserFilter);
				}
			}
		}
		return parserFilter;
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField[]>> addMatchReplaceField(
			ParseAttribute pa,
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String[]>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField[]>> parserFilter,
			JTextField prevMatch,
			String [] fieldText,
			MatchOrReplace mor
			)
	{
		ArrayList<JTextField[]> replFields = null;
		if(prevMatch != null && prevMatch.getText().isBlank())
		{
			return parserFilter;
		}
		if(parserFilter == null || parserFilter.get(prevMatch) == null)
		{
			if(mor == MatchOrReplace.match)
			{
				parserFilter = new LinkedHashMap<JTextField, ArrayList<JTextField[]>>();
			}
			else
			{
				return parserFilter;
			}
		}
		else
		{
			replFields = parserFilter.get(prevMatch);
			if(replFields != null && (replFields.size() != 0 && replFields.get(replFields.size()-1)[0].getText().isBlank()))
			{
				return parserFilter;
			}
			else if(replFields == null)
			{
				replFields = new ArrayList<JTextField[]>();
			}
		}
		
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new BorderLayout());
		String labelText = null;
		JTextField textField = new JTextField(fieldText[0]);
		JPanel textFieldPanel = new JPanel();
		
		switch(mor)
		{
		case match:
			labelText = MATCH_LABEL;
			parserFilter.put(textField, new ArrayList<JTextField[]>());
			textFieldPanel.setLayout(new GridLayout(0,1));
			textFieldPanel.add(textField);
			break;
		case replace:
			JTextField replField = new JTextField(fieldText[1]);
			labelText = REPLACE_LABEL;
			replFields.add(new JTextField[] {textField, replField});
			parserFilter.put(prevMatch, replFields);
			textFieldPanel.setLayout(new GridLayout(0,2));
			textFieldPanel.add(textField);
			textFieldPanel.add(replField);
			break;
		}
		
		JLabel textLabel = new JLabel(labelText);
		matchPanel.add(textLabel, BorderLayout.WEST);
		matchPanel.add(textFieldPanel, BorderLayout.CENTER);
		JPanel inner = new JPanel();
		inner.setLayout(new BorderLayout());
		inner.add(matchPanel, BorderLayout.NORTH);
		parentComponent.add(inner);
		
		ColorTemplate.setForegroundColorButtons(matchPanel, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(matchPanel, ColorTemplate.getButtonBackgroundColor());
		
		this.validate();
		
		return parserFilter;
	}
	
	private void simulateAction(String dragDropString)
	{
		PageParser youtube = getPageParser();
		
		dragDropString = HttpDatabaseRequest.addHttpsIfMissing(dragDropString);
		LoggingMessages.printOut("drag and drop value: " + dragDropString);
		String resp = HttpDatabaseRequest.executeGetRequest(dragDropString);
		LoggingMessages.printOut("response");
		
		String imageDownload = youtube.getAttributeFromResponse(ParseAttribute.Image, resp);
		String title = youtube.getAttributeFromResponse(ParseAttribute.Title, resp);
		
		LoggingMessages.printOut(imageDownload);
		simulateImageTextField.setText(imageDownload);
		LoggingMessages.printOut(title);
		simulateTitleTextField.setText(title);
	}
	
	private void saveAction()
	{
		pageParserEditor.setComponentValue(getPageParser());
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}
	
	public enum MatchOrReplace
	{
		match,
		replace
	}
	
}
