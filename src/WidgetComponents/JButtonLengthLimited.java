package WidgetComponents;

import javax.swing.JButton;

public class JButtonLengthLimited extends JButton 
{
	private static final String CHARACTER_LIMIT_TEXT = "..";
	private static final long serialVersionUID = 1L;
	private int characterLimit = 0;
	private String fullLengthText;
	
	public void setCharacterLimit(int charLimit)
	{
		this.characterLimit = charLimit;
	}
	
	public String getFullLengthText()
	{
		return this.fullLengthText;
	}
	
	@Override
	public void setText(String text)
	{
		this.fullLengthText = text;
		String t = (this.characterLimit != 0 && text.length() > this.characterLimit)
			? text.substring(0, this.characterLimit-CHARACTER_LIMIT_TEXT.length()) + CHARACTER_LIMIT_TEXT
			: text;
		super.setText(t);
	}

}
