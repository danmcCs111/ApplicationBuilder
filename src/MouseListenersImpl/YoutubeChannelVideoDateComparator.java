package MouseListenersImpl;

import java.util.Comparator;

public class YoutubeChannelVideoDateComparator implements Comparator<YoutubeChannelVideo> 
{
	@Override
	public int compare(YoutubeChannelVideo o1, YoutubeChannelVideo o2) 
	{
		return (o1.getUploadDate().after(o2.getUploadDate()))
				? -1
				: 1;
	}

}
