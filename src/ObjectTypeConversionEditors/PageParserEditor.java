package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import Graphics2D.ColorTemplate;
import ObjectTypeConversion.PageParser;
import Params.ParameterEditor;
import WidgetComponentDialogs.PageParserDialog;

public class PageParserEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 1L;
	
	private static final int 
		CHARACTER_LIMIT = 100;
	private static final String 
		LIMIT_POSTFIX = "..",
		DEFAULT_EDITOR_TEXT = "<Enter Page Filter>";
	
	private String commandText = DEFAULT_EDITOR_TEXT;
	private PageParser pageParser;
	private PageParserDialog parserDialog;
	
	public PageParserEditor()
	{
		super();
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.setText(commandText);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(parserDialog != null)
				{
					parserDialog.dispose();
				}
				parserDialog = new PageParserDialog(PageParserEditor.this, pageParser);
				parserDialog.setVisible(true);
			}
		});
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
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
		pageParser = (PageParser) value;
		String title = pageParser.getTitleLabel().isBlank()
				? DEFAULT_EDITOR_TEXT
				: pageParser.getTitleLabel();
		if(parserDialog != null)
		{
			parserDialog.dispose();
		}
		this.setText(title);
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {pageParser.getXmlString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return pageParser;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return PageParser.class.getName();
	}

}
