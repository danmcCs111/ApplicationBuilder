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


public class PicLabelMouseListener extends MouseAdapter 
{
	private static ArrayList<JLabel> connectedLabels = new ArrayList<JLabel>();
	
	private static final Border 
		EMPTY_BORDER = BorderFactory.createEmptyBorder(),
		HIGHLIGHT_BORDER = new BevelBorder(BevelBorder.RAISED, Color.blue, Color.blue);
	private AbstractButton connectedButton;
	
	public PicLabelMouseListener(AbstractButton connectedButton, JLabel label)
	{
		this.connectedButton = connectedButton;
		PicLabelMouseListener.connectedLabels.add(label);
	}
	
	public static void highLightLabel(AbstractButton ab, boolean on)
	{
		LoggingMessages.printOut("highlight label.");
		for(JLabel l : PicLabelMouseListener.connectedLabels)
		{
			if(l.getName().equals(ab.getText()))
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
	
	public void mouseClicked(MouseEvent e)
	{
//		if(e.getClickCount() == 2)//require double click
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			for(ActionListener al : connectedButton.getActionListeners())
			{
				al.actionPerformed(new ActionEvent(connectedButton, 1, "Open From Image"));
				highLightLabel(connectedButton, true);
			}
		}
	}
}
