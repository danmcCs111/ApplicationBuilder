package ActionListeners;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ApplicationBuilder.ClosePopupMenu;
import ApplicationBuilder.CommandLauncherWindow;
import ApplicationBuilder.OpenPopupMenu;
import Properties.WidgetTextProperties;
import WidgetUtility.WidgetCreator;

public class MinimizeActionListener implements ActionListener{

	private CommandLauncherWindow clWindow;
	
	public MinimizeActionListener(CommandLauncherWindow clWindow)
	{
		this.clWindow = clWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String selectedName = clWindow.getSelectedName();
		
		MenuItem miO = WidgetCreator.buildMenuItem(
				WidgetTextProperties.SYSTEM_TRAY_OPEN_OPTION.getPropertiesValue(),
				new OpenPopupMenu(clWindow));
		MenuItem miC = WidgetCreator.buildMenuItem(
				WidgetTextProperties.SYSTEM_TRAY_CLOSE_OPTION.getPropertiesValue(),
				new ClosePopupMenu());
		String trayText = selectedName == null || selectedName.equals("")
				? WidgetTextProperties.SYSTEM_TRAY_LABEL.getPropertiesValue()
				: selectedName;
		PopupMenu popMenu = WidgetCreator.buildPopupMenu(miO, miC);
		clWindow.setTrayIcon(WidgetCreator.getTrayIcon(clWindow, trayText, popMenu));
		clWindow.setVisible(false);
		clWindow.setExtendedState(JFrame.NORMAL);
	}
}
