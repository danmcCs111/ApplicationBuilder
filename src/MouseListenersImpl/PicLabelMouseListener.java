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
import javax.swing.border.Border;

import ActionListenersImpl.LaunchUrlActionListener;
import WidgetComponents.JButtonLengthLimited;


public class PicLabelMouseListener extends MouseAdapter 
{
	private enum LabelType{
		highlight,
		selection
	}
	
	private static ArrayList<JLabel> connectedLabels = new ArrayList<JLabel>();
	
	private static Color
		SELECTION_COLOR = Color.BLUE,
		HIGHLIGHT_COLOR = Color.RED;
	
	public static Border 
		EMPTY_BORDER = BorderFactory.createEmptyBorder(),
		SELECTION_BORDER = BorderFactory.createLineBorder(SELECTION_COLOR, 5),
		HIGHLIGHT_BORDER = BorderFactory.createLineBorder(HIGHLIGHT_COLOR, 5);
	private static AbstractButton 
		highlight,
		selected;
	
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
		HIGHLIGHT_BORDER = BorderFactory.createLineBorder(HIGHLIGHT_COLOR, 5);
	}
	
	public static void setFrameSelectionColor(Color c)
	{
		SELECTION_COLOR = c;
		SELECTION_BORDER = BorderFactory.createLineBorder(SELECTION_COLOR, 5);
	}
	
	public static void highLightLabel(AbstractButton ab, boolean on)
	{
		setLabel(LabelType.highlight, ab, on);
	}
	
	public static void selectionLabel(AbstractButton ab, boolean on)
	{
		setLabel(LabelType.selection, ab, on);
	}
	
	private static void setLabel(LabelType lt, AbstractButton ab, boolean on)
	{
		if(ab == null)
			return;
		
		Border 
			borderType = null,
			otherBorderType = null;
		switch(lt)
		{
		case highlight:
			borderType = HIGHLIGHT_BORDER;
			otherBorderType = SELECTION_BORDER;
			break;
		case selection:
			borderType = SELECTION_BORDER;
			otherBorderType = HIGHLIGHT_BORDER;
			break;
		}
		
		for(JLabel l : PicLabelMouseListener.connectedLabels)
		{
			if(ab instanceof JButtonLengthLimited && 
					l.getName().equals(((JButtonLengthLimited) ab).getFullLengthText()))
			{
				switch(lt)
				{
				case highlight:
					highlight = (on)
						? ab
						: null;
					break;
				case selection:
					if(on) selected = ab;
					break;
				}
				
				l.setBorder(
					(on)
					? borderType
					: EMPTY_BORDER
				);
			}
			else
			{
				if(l.getBorder() != null && l.getBorder().equals(otherBorderType))
					continue;
				
				if(highlight != null)
				{
					l.setBorder(l.getName().equals(((JButtonLengthLimited) highlight).getFullLengthText())
						? otherBorderType
						: EMPTY_BORDER
					);
				}
				else
				{
					l.setBorder(EMPTY_BORDER);
				}
			}
		}
		
		if(lt == LabelType.highlight)
		{
			if(!on) selectionLabel(selected, true);
		}
	}
	
	public void setSingleClick(boolean singleClick)
	{
		this.singleClick = singleClick;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(singleClick || e.getClickCount() == 2)
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
		else if(e.getButton() == MouseEvent.BUTTON2)//middle button
		{
			String [] args = LaunchUrlActionListener.buildCommand(connectedButton, 1);
			LaunchUrlActionListener.executeProcess(1, args);
		}
	}
}
