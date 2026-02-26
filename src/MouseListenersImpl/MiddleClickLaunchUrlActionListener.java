package MouseListenersImpl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;

public class MiddleClickLaunchUrlActionListener extends MouseAdapter 
{
	private AbstractButton ab;
	
	public MiddleClickLaunchUrlActionListener(AbstractButton ab)
	{
		this.ab = ab;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON2)
		{
			String [] args = LaunchUrlActionListener.buildCommand(ab, 1);
			LaunchUrlActionListener.executeProcess(1, args);
		}
	}
}
