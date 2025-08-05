package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import WidgetComponentInterfaces.OpenActionSubscriber;

public class OpenActionListener implements ActionListener
{
private ArrayList<OpenActionSubscriber> openSubscriber = new ArrayList<OpenActionSubscriber>();
	
	public void setOpenActionSubscriber(OpenActionSubscriber openSubscriber)
	{
		this.openSubscriber.add(openSubscriber);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		for(OpenActionSubscriber oas : openSubscriber)
		{
			oas.open();
		}
	}
}
