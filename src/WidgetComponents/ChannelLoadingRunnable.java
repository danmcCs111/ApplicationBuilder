package WidgetComponents;

import java.util.ArrayList;
import java.util.HashMap;

import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.YoutubeChannelVideo;

public class ChannelLoadingRunnable implements Runnable
{
	private JButtonLengthLimited 
		jbll;
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	
	public ChannelLoadingRunnable(JButtonLengthLimited jbll)
	{
		this.jbll = jbll;
	}
	
	public JButtonLengthLimited getButton()
	{
		return this.jbll;
	}

	@Override
	public void run() 
	{
		LookupOrCreateYoutube lcv = new LookupOrCreateYoutube();
		this.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
	}
	
	public HashMap <Integer, ArrayList <YoutubeChannelVideo>> getYoutubeChannels()
	{
		return this.ycvs;
	}
}
