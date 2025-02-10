package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationBuilder
{
	public static void main(String [] args)
	{
		if(args.length == 1) buildAppFromXML(args[0]);
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
		WidgetBuildController.readProperties(SOURCE_FILE);
		WidgetBuildController.generateGraphicalInterface(WidgetBuildController.getWidgetCreationProperties());
	}
}
