package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParserCollection;
import ObjectTypeConversionEditors.PageParserCollectionEditor;
import ObjectTypeConversionEditors.PageParserEditor;
import Properties.LoggingMessages;

public class PageParserCollectionDialog extends JDialog 
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
	
	private JPanel 
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JButton 
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel arrayPageParser = new JPanel();
	private LinkedHashMap<PageParser, JComponent> pageParserAndComponent = new LinkedHashMap<PageParser, JComponent>();
	private ArrayList<PageParserEditor> pageParserEditors = new ArrayList<PageParserEditor>();
	
	private PageParserCollection pageParserCollection;
	private PageParserCollectionEditor pageParserCollectionEditor;
	
	public PageParserCollectionDialog(PageParserCollectionEditor ppce, PageParserCollection pageParserCollection)
	{
		this.pageParserCollectionEditor = ppce;
		this.pageParserCollection = pageParserCollection;
		buildWidgets();
	}
	
	public void setPageParserCollection(PageParserCollection ppc)
	{
		this.pageParserCollection = ppc;
	}
	
	public PageParserCollection getPageParserCollection()
	{
		return this.pageParserCollection;
	}
	
	private void buildWidgets()
	{
		if(pageParserCollectionEditor != null)
		{
			GraphicsUtil.centerHeightOnlyWindow(pageParserCollectionEditor, this);
		}
		this.setTitle(TITLE_TEXT);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		arrayPageParser.setLayout(new GridLayout(0,1));
		JButton addParserButton = new JButton(ADD_PARSER_BUTTON_TEXT);
		ColorTemplate.setForegroundColorButtons(addParserButton, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(addParserButton, ColorTemplate.getButtonBackgroundColor());
		
		addParserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addPageParser(new PageParser(""));
				PageParserCollectionDialog.this.validate();
			}
		});
		this.arrayPageParser.add(addParserButton);
		for(PageParser pp : getPageParsers())
		{
			addPageParser(pp);
		}
		buildSaveCancel();
		
		this.add(arrayPageParser, BorderLayout.NORTH);
		this.add(saveCancelPanelOuter, BorderLayout.SOUTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
	}
	
	private ArrayList<PageParser> getPageParsers()
	{
		return this.pageParserCollection.getPageParsers();
	}
	
	private void addPageParser(PageParser pp)
	{
		PageParserEditor ppe = new PageParserEditor();
		pageParserEditors.add(ppe);
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
		ColorTemplate.setForegroundColorButtons(ppe, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(ppe, ColorTemplate.getButtonBackgroundColor());
		
		ColorTemplate.setForegroundColorButtons(delButton, ColorTemplate.getDeleteForegroundColor());
		ColorTemplate.setBackgroundColorButtons(delButton, ColorTemplate.getDeleteBackgroundColor());
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
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
	}
	
	private void removePageParser(PageParser pp, PageParserEditor ppe)
	{
		arrayPageParser.remove(pageParserAndComponent.get(pp));
		this.getPageParsers().remove(pp);
		pageParserEditors.remove(ppe);
		
		this.validate();
	}
	
	public void saveAction()
	{
		ArrayList<PageParser> pps = new ArrayList<PageParser>();
		for(PageParserEditor ppe : pageParserEditors)
		{
			PageParser pp = (PageParser) ppe.getComponentValueObj();
			pps.add(pp);
		}
		pageParserCollection.setPageParsers(pps);
		pageParserCollectionEditor.setComponentValue(pageParserCollection);
		
		LoggingMessages.printOut(pageParserCollection.getXmlString());
		
		this.dispose();
	}
	
	public void cancelAction()
	{
		this.dispose();
	}
	
	public static void main(String [] args)
	{
		PageParserCollection ppc = new PageParserCollection(
				"Youtube@F@youtube.com@F@Title@F@@M@<title>([^<])*</title>@R@<title>@RV@@R@</title>@RV@@R@[^a-zA-Z0-9\\\\-\\\\s]@RV@@F@Image@F@@M@https://yt3.googleusercontent.com([^@Q@])*(@Q@)@R@@Q@@RV@" + PageParser.PARSER_DELIMIT_COLLECTION + 
				"GitHub@F@github.com@F@Title@F@@M@<title>([^<])*</title>@R@<title>@RV@@R@</title>@RV@@R@[^a-zA-Z0-9\\\\-\\\\s]@RV@@F@Image@F@@M@meta property=@Q@og:image@Q@ content=@Q@([^@Q@])*(@Q@)@R@meta property=@Q@og:image@Q@ content=@RV@@R@@Q@@RV@@M@Gary@R@" + PageParser.PARSER_DELIMIT_COLLECTION + 
				"GOG@F@gog.com@F@Title@F@@M@product-tile[^@Q@]*@Q@ href=@Q@[^@Q@]*@Q@.*.jpg@R@product-tile[^@Q@]*@Q@ href=@Q@@RV@@R@(@Q@).*.jpg$@RV@@R@https://www.gog.com/en/game/@RV@@F@Url@F@@M@product-tile[^@Q@]*@Q@ href=@Q@[^@Q@]*@Q@.*.jpg@R@product-tile[^@Q@]*@Q@ href=@Q@@RV@@R@(@Q@).*.jpg$@RV@@F@Image@F@@M@product-tile[^@Q@]*@Q@ href=@Q@[^@Q@]*@Q@.*.jpg@R@product-tile[^@Q@]*@Q@ href=@Q@@RV@@R@(@Q@)$@RV@@M@[^\\s]*.jpg@R@srcset=@Q@@RV@"
				);
		
		PageParserCollectionEditor ppce = new PageParserCollectionEditor();
		ppce.setComponentValue(ppc);
		
		JFrame jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setMinimumSize(new Dimension(200,200));
		jf.add(ppce);
		jf.setVisible(true);
	}

}
