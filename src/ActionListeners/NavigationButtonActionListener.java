package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.Direction;
import ApplicationBuilder.CommandLauncherWindow;

public class NavigationButtonActionListener implements ActionListener{

	private CommandLauncherWindow rlWindow;
	private Direction direction;
	
	public NavigationButtonActionListener(CommandLauncherWindow rlWindow, Direction direction){
		this.rlWindow = rlWindow;
		this.direction = direction;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		rlWindow.setNextVideoIndex(rlWindow.getVideosListPos(), direction);
		rlWindow.addChannelButtons();
		rlWindow.paintComponents(rlWindow.getGraphics());
	}

}
