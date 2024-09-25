package ApplicationBuilder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClosePopupMenu implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		CommandLauncher.closeProcess();
		System.exit(0);
	}
}
