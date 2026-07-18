package WidgetComponentInterfaces;

public class FileView 
{
	private String
		dirPath,
		filename,
		filenameView;
	
	public FileView(String filename, String filenameView)
	{
		this.filename = filename;
		this.filenameView = filenameView;
	}
	
	public void setDirectoryPath(String path)
	{
		this.dirPath = path;
	}
	
	public String getDirectoryPath()
	{
		return dirPath;
	}
	public String getFilename()
	{
		return filename;
	}
	public String getFilenameView()
	{
		return filenameView;
	}
	
	@Override
	public String toString()
	{
		return getFilenameView();
	}
}
