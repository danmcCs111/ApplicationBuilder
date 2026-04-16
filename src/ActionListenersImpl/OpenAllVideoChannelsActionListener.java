package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import Params.KeepSelection;
import WidgetComponents.AllVideoChannelsOpenedPlayer;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;

public class OpenAllVideoChannelsActionListener implements ActionListener
{
	private JButtonArray 
		ba;
	private static AllVideoChannelsOpenedPlayer 
		avop;
	
	public OpenAllVideoChannelsActionListener(JButtonArray ba)
	{
		this.ba = ba;
	}
	
	public static AllVideoChannelsOpenedPlayer getAllVideoChannelsOpenedPlayer()
	{
		return avop;
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
		if(avop != null)
		{
			avop.removeAll();
			avop.dispose();
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				avop = new AllVideoChannelsOpenedPlayer(ba, jblls, ba.getRootPane().getParent());
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}
