package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationEditorLauncher {
	
	private static final String SOURCE_FILE = "src\\ApplicationBuilder\\data\\WidgetBuild.xml";
	
	public static void main(String [] args)
	{
		readWidgetXML(SOURCE_FILE);
		SwingUtilities.invokeLater(() -> {
			ApplicationLayoutEditor window = new ApplicationLayoutEditor();
		        window.setVisible(true);
		});
	}
	
	public static void readWidgetXML(String SOURCE_FILE)
	{
		new WidgetBuildController(SOURCE_FILE);
	}
}
