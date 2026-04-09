package WidgetExtensionInterfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Params.KeepSelection;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.OpenVideoChannelsUpdater;

public class OpenVideoChannelsUpdaterActionListener implements ActionListener
{
	private JButtonArray
		ba;
	private OpenVideoChannelsUpdater 
		ovcu;
	
	public OpenVideoChannelsUpdaterActionListener(JButtonArray ba)
	{
		this.ba = ba;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		ArrayList<KeepSelection> kss = ba.getKeepSelection();
		ArrayList<JButtonLengthLimited> jblls = new ArrayList<JButtonLengthLimited>();
		for(KeepSelection ks : kss)
		{
			JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
			jblls.add(jbll);
		}
		if(ovcu != null)
		{
			ovcu.dispose();
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				ovcu = new OpenVideoChannelsUpdater(jblls, ba);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

}
