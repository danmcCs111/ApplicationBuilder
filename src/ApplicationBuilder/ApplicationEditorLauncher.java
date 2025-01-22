package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationEditorLauncher 
{
	private static final String SOURCE_FILE = "src\\ApplicationBuilder\\data\\WidgetBuild.xml";
	
	public static void main(String [] args)
	{
		buildAppFromXML(SOURCE_FILE);
//		SwingUtilities.invokeLater(() -> {
//			ApplicationLayoutEditor window = new ApplicationLayoutEditor();
//		        window.setVisible(true);
//		});
	}
	
	public static void buildAppFromXML(String SOURCE_FILE)
	{
		new WidgetBuildController(SOURCE_FILE);
	}
}
