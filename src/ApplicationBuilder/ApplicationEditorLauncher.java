package ApplicationBuilder;

import javax.swing.SwingUtilities;

public class ApplicationEditorLauncher {
	
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(() -> {
			 BuilderWindow window = new BuilderWindow();
		        window.setVisible(true);
		});
	}
}
