package MouseListenersImpl;

import java.util.ArrayList;
import java.util.HashMap;


public interface YoutubeVideosContainer 
{
	public void update();
	public void buildVideoChannelPlayer();
	public HashMap <Integer, ArrayList <YoutubeChannelVideo>> getYoutubeVideos();
}
