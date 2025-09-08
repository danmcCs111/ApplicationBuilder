package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import ActionListeners.AddActionSend;
import Properties.LoggingMessages;

public class AddActionListener implements ActionListener
{
	private AddActionSend addActionSendListener = null;
	private ArrayList<AddActionReceive> addActionReceiveListeners = new ArrayList<AddActionReceive>();
	
	public AddActionListener()
	{
		
	}
	
	public void addActionSendListener(AddActionSend addActionSendListener)
	{
		this.addActionSendListener = addActionSendListener;
	}
	
	public void addActionRecieveListener(AddActionReceive addActionSendListener)
	{
		addActionReceiveListeners.add(addActionSendListener);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LoggingMessages.printOut("print");
		
		if(addActionSendListener != null)
		{
			for(AddActionReceive aas : addActionReceiveListeners)
			{
				aas.sendList(addActionSendListener.getList());
			}
		}
		else
		{
			LoggingMessages.printOut("Action send listener empty");
		}
	}
}
