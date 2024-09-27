package Properties;

public enum Paths {
	
	WIDGET_TEXT(new String [] {".\\src\\ApplicationBuilder\\data\\WidgetText.properties", 
	".\\ApplicationBuilder\\data\\WidgetText.properties"}),
	LAUNCHER(new String [] {".\\src\\ApplicationBuilder\\data\\Launcher.properties", 
		".\\ApplicationBuilder\\data\\Launcher.properties"}),
	EXTENDABLE(new String [] {".\\src\\ApplicationBuilder\\data\\Extendable.properties", 
		".\\ApplicationBuilder\\data\\Extendable.properties"});
	
	private String [] paths;
	
	private Paths(String ... path)
	{
		this.paths = path;
	}
	
	public String [] getPaths()
	{
		return this.paths;
	}
}
