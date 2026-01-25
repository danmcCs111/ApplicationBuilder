package MouseListenersImpl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Properties.LoggingMessages;
import WidgetComponents.JButtonLengthLimited;


public class PicLabelMouseListener extends MouseAdapter 
{
	private static ArrayList<JLabel> connectedLabels = new ArrayList<JLabel>();
	
	private static Color
		HIGHLIGHT_COLOR = Color.blue;
	
	private static Border 
		EMPTY_BORDER = BorderFactory.createEmptyBorder(),
		HIGHLIGHT_BORDER = new BevelBorder(BevelBorder.RAISED, HIGHLIGHT_COLOR, HIGHLIGHT_COLOR);
	private AbstractButton 
		connectedButton;
	public boolean 
		singleClick = false;
	
	public PicLabelMouseListener(AbstractButton connectedButton, JLabel label, boolean singleClick)
	{
		this.connectedButton = connectedButton;
		PicLabelMouseListener.connectedLabels.add(label);
		setSingleClick(singleClick);
	}
	
	public static void setFrameHighlightColor(Color c)
	{
		HIGHLIGHT_COLOR = c;
		HIGHLIGHT_BORDER = new BevelBorder(BevelBorder.RAISED, HIGHLIGHT_COLOR, HIGHLIGHT_COLOR);
	}
	
	public static void highLightLabel(JButtonLengthLimited ab, boolean on)
	{
		LoggingMessages.printOut("highlight label.");
		if(ab == null)
			return;
		
		for(JLabel l : PicLabelMouseListener.connectedLabels)
		{
			if(l.getName().equals(ab.getFullLengthText()))
			{
				l.setBorder(on
					? HIGHLIGHT_BORDER
					: EMPTY_BORDER);
			}
			else
			{
				l.setBorder(EMPTY_BORDER);
			}
		}
	}
	
	public void setSingleClick(boolean singleClick)
	{
		this.singleClick = singleClick;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(singleClick || e.getClickCount() == 2)//require double click
		{
			if(e.getButton() == MouseEvent.BUTTON1)
			{
				for(ActionListener al : connectedButton.getActionListeners())
				{
					al.actionPerformed(new ActionEvent(connectedButton, 1, "Open From Image"));
					highLightLabel((JButtonLengthLimited) connectedButton, true);
				}
			}
		}
	}
}
