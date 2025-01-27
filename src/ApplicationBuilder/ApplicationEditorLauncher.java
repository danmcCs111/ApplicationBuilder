package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationEditorLauncher 
{
	private static final String SOURCE_FILE = "src\\ApplicationBuilder\\data\\WidgetBuild.xml";
	private static WidgetBuildController widgetBuildController;
	
	public static void main(String [] args)
	{
		if(args.length == 1) buildAppFromXML(SOURCE_FILE);
		else
		{
			SwingUtilities.invokeLater(() -> {
				ApplicationLayoutEditor window = new ApplicationLayoutEditor();
			        window.setVisible(true);
			});
		}
	}
	
	public static void buildAppFromXML(String SOURCE_FILE)
	{
		if(widgetBuildController == null)
		{
			widgetBuildController = new WidgetBuildController();
		}
		widgetBuildController.readProperties(SOURCE_FILE);
	}
}
