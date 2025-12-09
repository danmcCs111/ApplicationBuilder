package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.PageParser;
import HttpDatabaseRequest.PageParser.ParseAttribute;
import ObjectTypeConversionEditors.PageParserEditor;
import WidgetComponentInterfaces.ParamOption;

public class PageParserDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Command Entry",
		ADD_IMAGE_FILTER_LABEL = " + Image Filter",
		ADD_TITLE_FILTER_LABEL = " + Title Filter",
		PARAM_DELETE_TEXT = "X",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	public static final List<String> PARAM_OPTIONS = Arrays.asList(new String[] {
			ParamOption.TextField.getDisplayText(),
			ParamOption.Directory.getDisplayText(),
			ParamOption.File.getDisplayText()
	});
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField 
		title = new JTextField(),
		domain = new JTextField();
	private JButton 
		addImageFilterButton = new JButton(ADD_IMAGE_FILTER_LABEL),
		addTitleFilterButton = new JButton(ADD_TITLE_FILTER_LABEL),
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>
		parserFilter = new HashMap<ParseAttribute, LinkedHashMap<JTextField, ArrayList<JTextField>>>();
	
	private String retSelection = null;
	private PageParserEditor pageParserEditor;

	public PageParserDialog(PageParserEditor cbe, PageParser pp)
	{
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
		addImageFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addParserImageFilter();
			}
		});
		addTitleFilterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addParserTitleFilter();
			}
		});
		this.add(innerPanel, BorderLayout.NORTH);
		innerPanel.add(title);
		innerPanel.add(domain);
		innerPanel.add(addImageFilterButton);
		innerPanel.add(addTitleFilterButton);
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
			//Construct
			title.setText(pp.getTitleLabel());
			domain.setText(pp.getDomainMatch());
			for(ParseAttribute pa : ParseAttribute.values())
			{
				LinkedHashMap<JTextField, ArrayList<JTextField>> replFields = constructReplacementFields(
						pp.getMatchAndReplace(pa), this.parserFilter.get(pa));
				parserFilter.put(pa, replFields);
			}
			
		}
		
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		
		this.setVisible(true);
	}
	
	public void addParserImageFilter()
	{
		
	}
	
	public void addParserTitleFilter()
	{
		
	}
	
	public PageParser getPageParser()
	{
		return buildPageParser();
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
	
	private LinkedHashMap<JTextField, ArrayList<JTextField>> constructReplacementFields(
			LinkedHashMap<String, ArrayList<String>> replacementFields, 
			LinkedHashMap<JTextField, ArrayList<JTextField>> parserFilter)
	{
		if(replacementFields == null)
			return null;
		
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
