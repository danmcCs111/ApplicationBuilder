package WidgetComponents;

import java.util.Comparator;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import WidgetComponentInterfaces.LaunchUrlButton;

public class JToggleButtonLengthLimited extends JButton implements Comparator<JToggleButtonLengthLimited>, LaunchUrlButton
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		CHARACTER_LIMIT_TEXT = "..";
	
	private int 
		characterLimit = 0;
	private String 
		fullLengthText,
		path;
	private AbstractButton 
		highlightButton = this;
	
	public void setCharacterLimit(int charLimit)
	{
		this.characterLimit = charLimit;
		this.setText(this.getText());
	}
	
	public int getCharacterLimit()
	{
		return this.characterLimit;
	}
	
	public String getFullLengthText()
	{
		return this.fullLengthText;
	}
	
	public void setFullText(String text)
	{
		this.fullLengthText = text;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	@Override
	public void setText(String text)
	{
		String t = (this.characterLimit != 0 && text.length() > this.characterLimit)
			? text.substring(0, this.characterLimit-CHARACTER_LIMIT_TEXT.length()) + CHARACTER_LIMIT_TEXT
			: text;
		super.setText(t);
	}
	
	@Override
	public int compare(JToggleButtonLengthLimited o1, JToggleButtonLengthLimited o2) 
	{
		return o1.getText().compareTo(o2.getText());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof JToggleButtonLengthLimited)
		{
			return ((JToggleButtonLengthLimited) o).getText().equals(this.getText());
		}
		else
		{
			return false;
		}
	}
	
	public void setHighlightButton(AbstractButton ab)
	{
		this.highlightButton = ab;
	}

	@Override
	public AbstractButton getHighlightButton() 
	{
		return highlightButton;
	}
	
	@Override
	public String toString()
	{
		return this.getText();
	}

}
