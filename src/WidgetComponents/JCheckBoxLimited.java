package WidgetComponents;

import java.util.Comparator;

import javax.swing.JCheckBox;

public class JCheckBoxLimited extends JCheckBox implements Comparator<JCheckBoxLimited>
{
	private static final long serialVersionUID = 1L;
	private static final String CHARACTER_LIMIT_TEXT = "..";
	
	private int characterLimit = 0;
	private String 
		fullLengthText,
		pathKey;
	
	public void setCharacterLimit(int charLimit)
	{
		this.characterLimit = charLimit;
	}
	
	public String getFullLengthText()
	{
		return this.fullLengthText;
	}
	
	public void setPathKey(String pathKey)
	{
		this.pathKey = pathKey;
	}
	
	public String getPathKey()
	{
		return this.pathKey;
	}
	
	@Override
	public void setName(String text)
	{
		this.fullLengthText = text;
		super.setName(text);
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
	public int compare(JCheckBoxLimited o1, JCheckBoxLimited o2) 
	{
		return o1.getText().compareTo(o2.getText());
	}

}
