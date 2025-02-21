package MouseListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;

public class PicLabelMouseListener extends MouseAdapter 
{
	private AbstractButton connectedButton;
	
	public PicLabelMouseListener(AbstractButton connectedButton)
	{
		this.connectedButton = connectedButton;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2)//require double click
		{
			for(ActionListener al : connectedButton.getActionListeners())
			{
				al.actionPerformed(new ActionEvent(connectedButton, 1, "Open From Image"));
			}
		}
	}
}
