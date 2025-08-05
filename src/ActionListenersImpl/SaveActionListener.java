package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import WidgetComponentInterfaces.SaveActionSubscriber;

public class SaveActionListener implements ActionListener 
{

	private ArrayList<SaveActionSubscriber> saveSubscriber = new ArrayList<SaveActionSubscriber>();
	
	public void setSaveActionSubscriber(SaveActionSubscriber saveSubscriber)
	{
		this.saveSubscriber.add(saveSubscriber);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		for(SaveActionSubscriber sas : saveSubscriber)
		{
			sas.save();
		}
	}

}
 