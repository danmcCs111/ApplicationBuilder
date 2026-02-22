package WidgetComponents;

import java.util.Comparator;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import WidgetComponentInterfaces.LaunchUrlButton;

public class JButtonLengthLimited extends JButton implements Comparator<JButtonLengthLimited>, LaunchUrlButton
{
	private static final String CHARACTER_LIMIT_TEXT = "..";
	private static final long serialVersionUID = 1L;
	private int characterLimit = 0;
	private String fullLengthText;
	private AbstractButton highlightButton = this;
	
	public void setCharacterLimit(int charLimit)
	{
		this.characterLimit = charLimit;
	}
	
	public String getFullLengthText()
	{
		return this.fullLengthText;
	}
	
	public void setFullText(String text)
	{
		this.fullLengthText = text;
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
	public int compare(JButtonLengthLimited o1, JButtonLengthLimited o2) 
	{
		return o1.getText().compareTo(o2.getText());
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof JButtonLengthLimited)
		{
			return ((JButtonLengthLimited) o).getText().equals(this.getText());
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

}
