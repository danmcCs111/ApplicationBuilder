package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationEditorLauncher {
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(() -> {
			ApplicationLayoutEditor window = new ApplicationLayoutEditor();
		        window.setVisible(true);
		});
	}
}
