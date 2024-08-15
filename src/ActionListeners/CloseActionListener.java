package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.CommandLauncher;
import ApplicationBuilder.CommandLauncherWindow;

public class CloseActionListener implements ActionListener{
	
	private CommandLauncherWindow rlWindow;
	
	public CloseActionListener(CommandLauncherWindow rlWindow)
	{
		this.rlWindow = rlWindow;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(rlWindow.getSelectedButton() != null)
		{
			rlWindow.getSelectedButton().setBackground(
					ChannelActionListener.getChannelButtonDefaultColor());
		}
		CommandLauncher.closeRokuVideo();
		rlWindow.setSelectedButtonAndText(null, null);
		rlWindow.getChannelPanel().repaint();
	}
}
