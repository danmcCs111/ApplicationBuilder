package ActionListeners;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ApplicationBuilder.CommandLauncher;
import ApplicationBuilder.CommandLauncherWindow;
import Properties.WidgetTextProperties;
import WidgetUtility.WidgetCreator;

public class ChannelActionListener implements ActionListener{

	private CommandLauncherWindow rlWindow;
	private JButton channelButton;
	private static Color 
		defaultBackgroundColor = new JButton().getBackground(),
		highlightColor = WidgetTextProperties.HIGHLIGHT_COLOR.getPropertyValueAsColor();
	
	public ChannelActionListener(CommandLauncherWindow rlWindow)
	{
		this.rlWindow = rlWindow;
	}
	
	public void setChannelButton(JButton channelButton) {
		this.channelButton = channelButton;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String exe = rlWindow.getVideos().get(rlWindow.getVideosListPos()).getExeName();
		if(exe != null && !exe.equals(""))
		{
			CommandLauncher.executeProcess(exe, rlWindow.getVideos().get(rlWindow.getVideosListPos()).returnVideo(channelButton.getName()));
		}
		else
		{
			CommandLauncher.executeProcess(rlWindow.getVideos().get(rlWindow.getVideosListPos()).returnVideo(channelButton.getName()));
		}
		
		toggleHighLightButton();
	}
	
	public void toggleHighLightButton() {
		WidgetCreator.toggleHighlightButton(rlWindow.getChannelPanel(), 
				rlWindow.getSelectedButton(), 
				channelButton,
				defaultBackgroundColor,
				highlightColor);
		rlWindow.setSelectedButtonAndText(channelButton, channelButton.getText());
	}
	
	public static Color getChannelButtonDefaultColor() {
		return defaultBackgroundColor;
	}

}
