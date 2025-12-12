package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Graphics2D.ColorTemplate;
import ObjectTypeConversion.PageParser;
import ObjectTypeConversion.PageParserCollection;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import WidgetComponentDialogs.PageParserCollectionDialog;

public class PageParserCollectionEditor extends JButton implements ParameterEditor 
{
	private static final long serialVersionUID = 1L;

	private static final int 
		CHARACTER_LIMIT = 100;
	private static final String 
		LIMIT_POSTFIX = "..",
		DEFAULT_EDITOR_TEXT = "<Enter Page Filter>";
	
	private String commandText = DEFAULT_EDITOR_TEXT;
	private PageParserCollection pageParserCollection;
	private PageParserCollectionDialog parserCollectionDialog;
	
	public PageParserCollectionEditor()
	{
		buildWidgets();
	}

	public void buildWidgets()
	{
		this.setText(commandText);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(parserCollectionDialog != null)
				{
					parserCollectionDialog.dispose();
				}
				parserCollectionDialog = new PageParserCollectionDialog(PageParserCollectionEditor.this, pageParserCollection);
				parserCollectionDialog.setVisible(true);
			}
		});
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
	}
	
	private String getTitles()
	{
		String title = "";
		if(pageParserCollection.getPageParsers() == null)
			return "";
		
		for(PageParser pp : pageParserCollection.getPageParsers())
		{
			if(pp.getTitleLabel() != null && !pp.getTitleLabel().isBlank())
			{
				title += pp.getTitleLabel();
			}
		}
		return title;
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		PageParserCollection parserCollection = (PageParserCollection) value;
		this.pageParserCollection = parserCollection;
		
		String title = getTitles();
		
		LoggingMessages.printOut("set editor title: " + title);
		
		if(title == null || title.isBlank())
		{
			title = DEFAULT_EDITOR_TEXT;
		}
		if(parserCollectionDialog == null)
		{
			this.setText(title);
		}
		else
		{
			this.setText(title);
		}
		
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {pageParserCollection.getXmlString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return pageParserCollection;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return PageParserCollection.class.getName();
	}

}
