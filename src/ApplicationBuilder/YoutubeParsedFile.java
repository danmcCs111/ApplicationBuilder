package ApplicationBuilder;

public class YoutubeParsedFile 
{
	public String
		title,
		url,
		handle,
		imgLink;
	
	@Override
	public String toString()
	{
		return title + " " + url + " " + handle + " " + imgLink;
	}
}
