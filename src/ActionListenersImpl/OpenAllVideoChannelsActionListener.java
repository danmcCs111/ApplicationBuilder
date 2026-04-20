package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;

import Params.KeepSelection;
import WidgetComponents.AllVideoChannelsOpenedPlayer;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;

public class OpenAllVideoChannelsActionListener implements ActionListener
{
	private JButtonArray 
		ba;
	private AllVideoChannelsOpenedPlayer 
		avop;
	
	public OpenAllVideoChannelsActionListener(JButtonArray ba)
	{
		this.ba = ba;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		ArrayList<KeepSelection> kss = ba.getKeepSelection();
		LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon = new LinkedHashMap<JButtonLengthLimited, ImageIcon>();
		for(KeepSelection ks : kss)
		{
			buttonAndIcon.put(ks.getJButtonLengthLimited(), ks.getImageIcon());
		}
		if(avop != null)
		{
			avop.removeAll();
			avop.dispose();
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				avop = new AllVideoChannelsOpenedPlayer(buttonAndIcon, ba.getRootPane().getParent());
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}
