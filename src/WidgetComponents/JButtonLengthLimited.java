package WidgetComponents;

import java.util.Comparator;

import javax.swing.JButton;

public class JButtonLengthLimited extends JButton implements Comparator<JButtonLengthLimited>
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

	@Override
	public int compare(JButtonLengthLimited o1, JButtonLengthLimited o2) 
	{
		return o1.getText().compareTo(o2.getText());
	}
	
//	@Override
//	public boolean equals(Object o)
//	{
//		if(o instanceof JButtonLengthLimited)
//		{
//			return ((JButtonLengthLimited) o).getText().equals(this.getText());
//		}
//		else
//		{
//			return false;
//		}
//	}

}
