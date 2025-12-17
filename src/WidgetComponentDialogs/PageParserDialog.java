package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.HttpDatabaseRequest;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.ParseAttribute;
import ObjectTypeConversion.ParseAttributes;
import ObjectTypeConversionEditors.PageParserEditor;
import Properties.LoggingMessages;
import Properties.OpenDialog;
import Properties.PathUtility;

public class PageParserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static int 
		SCROLL_INCREMENT = 15,
		JTEXT_FIELD_SIZE = 20;
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
		MULTI_MATCH_OPTION_TEXT = "Multi Match?",
		SHOW_RESPONSE_BUTTON_TEXT = "Show Response",
		SIMULATE_PARSE_ATTRIBUTE_SUFFIX = " Filtered: ",
		SIMULATE_URL_ENTRY_STRIPPED = "Simulate with Url: ",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(750, 600),
		DIMENSION_SHOW_HTML = new Dimension(800,600);
	
	private JPanel 
		editPanel = new JPanel(),
		viewPanel = new JPanel(),
		simulatePanel = new JPanel(),
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField 
		simulateTextField = new JTextField(JTEXT_FIELD_SIZE),
		title = new JTextField(),
		domain = new JTextField();
	private HashMap<ParseAttribute, ArrayList<JTextField>>
		simulateParseAttributeTextField = new HashMap<ParseAttribute, ArrayList<JTextField>>();
	private JButton 
		simulateButton = new JButton(SIMULATE_LABEL),
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JCheckBox
		multiButton = new JCheckBox(MULTI_MATCH_OPTION_TEXT);
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField[]>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField[]>>>();
	private HashMap<ParseAttribute, JComponent> 
		matchFilterPanel = new HashMap<ParseAttribute, JComponent>();
	
	private ParseAttributes pas; 
	private PageParserEditor pageParserEditor;
	private PageParser pageParser = null;
	private String htmlResponse;

	public PageParserDialog(PageParserEditor ppe, PageParser pp)
	{
		this.pageParser = pp;
		buildWidgets(ppe, pp);
	}
	
	private void buildWidgets(PageParserEditor ppe, PageParser pp)
	{
		pas = new ParseAttributes(pp.getParseAttributes());
		
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
		for(ParseAttribute pa : pas.parseAttributes)
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
		JScrollPane viewScroll = new JScrollPane(outerPanel);
		viewScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
		
		editPanel.setLayout(new BorderLayout());
		
		editPanel.add(topPanel, BorderLayout.NORTH);
		editPanel.add(viewScroll, BorderLayout.CENTER);
		editPanel.add(saveCancelPanelOuter, BorderLayout.SOUTH);
		
		this.add(editPanel, BorderLayout.CENTER);
		JScrollPane viewScroll2 = new JScrollPane(viewPanel);
		viewScroll2.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
		buildSimulateViewPanel();
		this.add(viewScroll2, BorderLayout.EAST);
		
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
				simulateActionWithUrl(simulateTextField.getText());
			}
		});
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		simulatePanel.setBorder(b);
		
		JPanel simulateButtonOption = new JPanel();
		JButton showResponse = new JButton(SHOW_RESPONSE_BUTTON_TEXT);
		showResponse.addActionListener(new ActionListener() {
			JFrame f;
			@Override
			public void actionPerformed(ActionEvent e) {
				f = new JFrame();
				f.setSize(DIMENSION_SHOW_HTML);
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JTextArea simulateViewResponseHtml = new JTextArea();
				simulateViewResponseHtml.setText(htmlResponse);
				JScrollPane jsp = new JScrollPane(simulateViewResponseHtml);
				f.add(jsp);
				f.setVisible(true);
			}
		});
		JButton openResponseFile = new JButton("Open html file");
		openResponseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OpenDialog od = new OpenDialog();
				File f = od.performOpen(PageParserDialog.this, 
						"", "html", ".");
				htmlResponse = PathUtility.readFileToString(f);
				LoggingMessages.printOut(f.getAbsolutePath());
				LoggingMessages.printOut(htmlResponse);
				simulateAction();
			}
		});
		simulateButtonOption.add(multiButton);
		simulateButtonOption.add(showResponse);
		simulateButtonOption.add(openResponseFile);
		
		JPanel innerPanelSimulate = new JPanel();
		innerPanelSimulate.setLayout(new BorderLayout());
		innerPanelSimulate.add(new JLabel(SIMULATE_URL_ENTRY_STRIPPED), BorderLayout.WEST);
		innerPanelSimulate.add(simulateTextField, BorderLayout.CENTER);
		
		innerPanelSimulate.add(simulateButton, BorderLayout.EAST);
		innerPanelSimulate.add(simulateButtonOption, BorderLayout.SOUTH);
		simulatePanel.add(innerPanelSimulate);
		
		return simulatePanel;
	}
	
	public JPanel buildSimulateViewPanel()
	{
		simulatePanel.setLayout(new GridLayout(0,1));
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		simulatePanel.setBorder(b);
		
		for(ParseAttribute pa : pas.parseAttributes)
		{
			JPanel innerPanelTitle = new JPanel();
			innerPanelTitle.setLayout(new BorderLayout());
			JTextField titleField = new JTextField(JTEXT_FIELD_SIZE);
			titleField.setEditable(false);
			innerPanelTitle.add(new JLabel(pa.name() + SIMULATE_PARSE_ATTRIBUTE_SUFFIX), BorderLayout.WEST);
			innerPanelTitle.add(titleField, BorderLayout.CENTER);
			if(simulateParseAttributeTextField.get(pa) == null)
			{
				simulateParseAttributeTextField.put(pa, new ArrayList<JTextField>());
			}
			simulateParseAttributeTextField.get(pa).add(titleField);
			simulatePanel.add(innerPanelTitle);
		}
		
		viewPanel.add(simulatePanel);
		
		return simulatePanel;
	}
	
	public JPanel addToSimulateViewPanel(LinkedHashMap<ParseAttribute, JTextField> mats)
	{
		for(ParseAttribute pa : mats.keySet())
		{
			JTextField jt = mats.get(pa);
			if(jt.getText().isBlank())
				continue;
			JPanel innerPanelAttr = new JPanel();
			innerPanelAttr.setLayout(new BorderLayout());
			jt.setEditable(false);
			innerPanelAttr.add(new JLabel(pa.name() + SIMULATE_PARSE_ATTRIBUTE_SUFFIX), BorderLayout.WEST);
			innerPanelAttr.add(jt, BorderLayout.CENTER);
			simulatePanel.add(innerPanelAttr);
		}
		
		viewPanel.add(simulatePanel);
		this.validate();
		
		return simulatePanel;
	}
	
	public void removeLastFromSimulatePanel(int lastIndex)
	{
		simulatePanel.remove(lastIndex);
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
			matchFilterPanel.get(pa).remove(lastMatch.getParent().getParent().getParent());
			parserFilter.get(pa).remove(lastMatch);
		}
		else
		{
			//delete last repl
			JTextField [] lastRepl = repls.get(repls.size()-1);
			matchFilterPanel.get(pa).remove(lastRepl[0].getParent().getParent().getParent());
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
		if(fieldText.length == 0)//empty field in save file.
			return parserFilter;
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
	
	private void simulateActionWithUrl(String url)
	{
		
		url = HttpDatabaseRequest.addHttpsIfMissing(url);
		LoggingMessages.printOut("drag and drop value: " + url);
		htmlResponse = HttpDatabaseRequest.executeGetRequest(url);
		LoggingMessages.printOut("response");
		simulateAction();
	}
	
	private void simulateAction()
	{
		PageParser youtube = getPageParser();
		
		LinkedHashMap<ParseAttribute, String[]> parsePagesAndMatches = new LinkedHashMap<ParseAttribute, String[]>();
		int len = 0;
		for(ParseAttribute pa : pas.parseAttributes)
		{
			String [] matches = youtube.getAttributesFromResponse(pa, htmlResponse, !multiButton.isSelected());
			if(matches == null || matches.length == 0)
				continue;
			parsePagesAndMatches.put(pa, matches);
			
			if(matches.length < len || len == 0)
				len = matches.length;//TODO
		}
		
		int widgetCount = -1;
		for(ParseAttribute pa : simulateParseAttributeTextField.keySet())
		{
			LoggingMessages.printOut("WidgetSize: " + widgetCount);
			widgetCount += simulateParseAttributeTextField.get(pa).size();
			simulateParseAttributeTextField.get(pa).clear();
		}
		LoggingMessages.printOut("WidgetSize: " + widgetCount);
		while(widgetCount >= 0)
		{
			removeLastFromSimulatePanel(widgetCount);
			widgetCount--;
		}
		
		LinkedHashMap<ParseAttribute, JTextField> mats = new LinkedHashMap<ParseAttribute, JTextField>();
		for(int i = 0; i < len; i++)
		{
			for(ParseAttribute pa : parsePagesAndMatches.keySet())
			{
				JTextField newI = new JTextField(JTEXT_FIELD_SIZE);
				newI.setEditable(false);
				String stripText = (i < parsePagesAndMatches.get(pa).length)
						?parsePagesAndMatches.get(pa)[i]
								:"";
				if(stripText.isBlank())
				{
					break;
				}
				LoggingMessages.printOut(pa.name() + " " + stripText + "");
				newI.setText(stripText);
				mats.put(pa, newI);
			}
			for(ParseAttribute pa : mats.keySet())
			{
				simulateParseAttributeTextField.get(pa).add(mats.get(pa));
			}
			LoggingMessages.printOut("");
			addToSimulateViewPanel(mats);
		}
		this.validate();
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
