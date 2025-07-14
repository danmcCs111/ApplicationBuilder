package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Actions.LoadVideos;

public class LoadVideosActionListener implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		LoadVideos.loadYoutubeScrape();
	}
	
}
