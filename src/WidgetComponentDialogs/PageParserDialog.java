package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import HttpDatabaseRequest.PageParser;
import HttpDatabaseRequest.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;
import Properties.LoggingMessages;

public class PageParserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Command Entry",
		ADD_FILTER_LABEL_PREFIX = " + ",
		ADD_FILTER_MATCH_LABEL_SUFFIX = " Match",
		ADD_FILTER_REPLACE_LABEL_SUFFIX = " Replace",
		MATCH_LABEL =	"Match:      ",
		REPLACE_LABEL =	"Replace:  ",
		TITLE_LABEL =	"Title:  ",
		DOMAIN_LABEL =	"Domain: ",
		PARAM_DELETE_TEXT = "X",
		SIMULATE_LABEL = "Simulate",
		SIMULATE_TITLE_STRIPPED = "Title Filtered: ",
		SIMULATE_IMAGE_STRIPPED = "Image Filtered: ",
		SIMULATE_URL_ENTRY_STRIPPED = "Simulate with Url: ",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 600);
	
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
	private ArrayList<JButton>
		addMatchFilterButton = new ArrayList<JButton>(),
		addReplaceFilterButton = new ArrayList<JButton>();
	private JButton 
		simulateButton = new JButton(SIMULATE_LABEL),
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	
	private HashMap<ParseAttribute, ArrayList<JButton>>
		addReplaceButton = new HashMap<ParseAttribute, ArrayList<JButton>>();
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>();
	private HashMap<ParseAttribute, JComponent> 
		matchFilterPanel = new HashMap<PageParser.ParseAttribute, JComponent>();
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
			JPanel addMatchReplace = new JPanel();
			addMatchReplace.setLayout(new GridLayout(0,2));
			
			matchPanel.setLayout(new GridLayout(0,1));
			matchFilterPanel.put(pa, matchPanel);
			
			JButton jbut = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + ADD_FILTER_MATCH_LABEL_SUFFIX);
			jbut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addMatchFilter(pa, pp, "");
				}
			});
			addMatchReplace.add(jbut);
			addMatchFilterButton.add(jbut);
			
			JButton jbut2 = new JButton(ADD_FILTER_LABEL_PREFIX + pa.name() + ADD_FILTER_REPLACE_LABEL_SUFFIX);
			jbut2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addReplaceFilter(pa, pp, "");
				}
			});
			addMatchReplace.add(jbut2);
			addReplaceFilterButton.add(jbut2);
			
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
		
		if(pp != null)
		{
			constructPageParser(pp);
		}
		
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		
		this.setVisible(true);
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
		innerPanelTitle.add(new JLabel(SIMULATE_TITLE_STRIPPED), BorderLayout.WEST);
		innerPanelTitle.add(simulateTitleTextField, BorderLayout.CENTER);
		simulatePanel.add(innerPanelTitle);
		
		JPanel innerPanelImage = new JPanel();
		innerPanelImage.setLayout(new BorderLayout());
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
	
	public void addMatchFilter(ParseAttribute pa, PageParser pp, String matchInitValue)
	{
		JTextField lastMatchText = getLastMatchTextField(pa);
		LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = addMatchField(
				pa,
				matchFilterPanel.get(pa),
				pp.getMatchAndReplace(pa), 
				this.parserFilter.get(pa),
				lastMatchText.getText(),
				matchInitValue);
		parserFilter.put(pa, replFields);
	}
	
	public void addReplaceFilter(ParseAttribute pa, PageParser pp, String replaceInitValue)
	{
		JTextField lastMatchText = getLastMatchTextField(pa);
		
		LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = addReplacementField(
				pa,
				matchFilterPanel.get(pa),
				pp.getMatchAndReplace(pa), 
				this.parserFilter.get(pa),
				lastMatchText.getText(),
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
	
	private JTextField getLastMatchTextField(ParseAttribute pa)
	{
		if(parserFilter.size() == 0 || parserFilter.get(pa).size() == 0)
			return new JTextField();
		
		JTextField [] keys = this.parserFilter.get(pa).keySet().toArray(new JTextField[] {});
		return keys[keys.length-1];
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
					pa,
					matchFilterPanel.get(pa),
					pp.getMatchAndReplace(pa), 
					this.parserFilter.get(pa)
			);
			parserFilter.put(pa, replFields);
		}
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> constructMatchAndReplacementFields(
			ParseAttribute pa,
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
				parserFilter = addMatchField(pa, parentComponent, replacementFields, parserFilter, match, match);
				
				for(String repl : replacementFields.get(match))
				{
					parserFilter = addReplacementField(pa, parentComponent, replacementFields, parserFilter, match, repl);
				}
			}
		}
		return parserFilter;
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> addMatchField(
			ParseAttribute pa,
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter,
			String prevMatch,
			String fieldText
			)
	{
		if(prevMatch.isBlank())
		{
			return parserFilter;
		}
		if(parserFilter == null)
		{
			parserFilter = new LinkedHashMap<JTextField, ArrayList<JTextField>>();
		}
		ArrayList<JTextField> replFields = parserFilter.get(matchStringAndTextField.get(prevMatch));
		if(replFields != null && (replFields.size() != 0 && replFields.get(replFields.size()-1).getText().isBlank()))
		{
			return parserFilter;
		}
		
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(new BorderLayout());
		JLabel matchLabel = new JLabel(MATCH_LABEL);
		JTextField matchField = new JTextField(fieldText);
		matchPanel.add(matchLabel, BorderLayout.WEST);
		matchPanel.add(matchField, BorderLayout.CENTER);
		
		parentComponent.add(matchPanel);
		
		
		matchStringAndTextField.put(fieldText, matchField);
		parserFilter.put(matchField, new ArrayList<JTextField>());
		
		this.validate();
		
		return parserFilter;
	}
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> addReplacementField(
			ParseAttribute pa,
			JComponent parentComponent,
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter,
			String match,
			String fieldText
			)
	{
		if(match.isBlank())
		{
			return parserFilter;
		}
		ArrayList<JTextField> replFields = parserFilter.get(matchStringAndTextField.get(match));
		if(replFields.size() != 0 && replFields.get(replFields.size()-1).getText().isBlank())
		{
			return parserFilter;
		}
		
		JLabel replLabel = new JLabel(REPLACE_LABEL);
		JTextField replField = new JTextField(fieldText);
		LoggingMessages.printOut(match);
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
	
	private void simulateAction(String dragDropString)
	{
		PageParser youtube = getPageParser();
		
		HttpDatabaseRequest.addHttpsIfMissing(dragDropString);
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
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}
	
}
