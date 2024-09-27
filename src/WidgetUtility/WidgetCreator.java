package WidgetUtility;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WidgetCreator implements WidgetCreationOptions {

	public static JPanel createNavigationPanel(JButton navPanel)
	{
		JPanel buttonPanel = new JPanel();
		BorderLayout blW = new BorderLayout();
		buttonPanel.setLayout(blW);
		buttonPanel.add(navPanel, BorderLayout.NORTH);
		
		return buttonPanel;
	}
	
	public static JButton createButtonWithStrip(String title, String filter)
	{
		JButton b = new JButton();
		
		b.setName(title);
		b.setText(createTitleStripped(title, filter));
		
		return b;
	}
	
	public static JButton createButton(String title)
	{
		return createButtonWithStrip(title, "");
	}
	
	public static JFrame createFrame(String title)
	{
		JFrame frame = new JFrame(title);
		return frame;
	}
	
	public static String createTitleStripped(String buttonTitle, String stripStr)
	{
		String replstr = "";
		if(stripStr == null || stripStr.equals(""))
			return buttonTitle;
		Pattern pat = Pattern.compile(stripStr, Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(buttonTitle);
		return mat.replaceAll(replstr);
	}
	
	public static void toggleHighlightButton(Component c, 
			JButton selButton, 
			JButton curButton, 
			Color defaultBackgroundColor, 
			Color highlightColor) {
		if(selButton != null)
			selButton.setBackground(defaultBackgroundColor);
		curButton.setBackground(highlightColor);
		selButton = curButton;
		
		c.repaint();
	}
	
	public static Image buildTaskbarIcon(String path)
	{
		File file = new File(path);
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	
	public static TrayIcon buildTrayIcon(JFrame frame, String selectedTitle, PopupMenu trayPopupMenu, String path)
	{
		TrayIcon retIcon = null;
		try {
			File file = new File(path);//use location from .bat script
			BufferedImage img = ImageIO.read(file);
			
			final TrayIcon trayIcon = new TrayIcon(img, selectedTitle, trayPopupMenu);
			trayIcon.setImageAutoSize(true);
			
			retIcon = trayIcon;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retIcon;
	}
	
	public static PopupMenu buildPopupMenu(MenuItem ... menuItems)
	{
		PopupMenu trayPopupMenu = new PopupMenu();
		
		for (MenuItem mi : menuItems)
			trayPopupMenu.add(mi);
		
		return trayPopupMenu;
	}
	
	public static MenuItem buildMenuItem(String labelText, ActionListener aListener)
	{
		MenuItem menuItem = new MenuItem(labelText);
		menuItem.addActionListener(aListener);
		return menuItem;
	}
	
	public static void destroySystemTray(TrayIcon trayIcon)
	{
		SystemTray systemTray = SystemTray.getSystemTray();
		systemTray.remove(trayIcon);
	}
	
	public static JButton findButton(Component [] buttons, String text)
	{
		if (text == null)
			return null;
		JButton retB = null;
		
		for (Component c : buttons)
		{
			if (c == null)
				continue;
			
			if (c instanceof JButton)
			{
				JButton b = (JButton) c;
				String bText = b.getText();
				if(bText.equals(text)){
					retB = b;
					break;
				}
			}
		}
		return retB;
	}
	
	public static void setTrayIconOnSystemTray(TrayIcon trayIcon)
	{
		SystemTray systemTray = SystemTray.getSystemTray();
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static TrayIcon getSystemTrayTrayIcon()
	{
		SystemTray systemTray = SystemTray.getSystemTray();
		return systemTray.getTrayIcons()[0];
	}

	@Override
	public JFrame createFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel createPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JScrollPane createScrollPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuBar createMenuOption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MenuItem createMenuItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JButton createButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<JButton> createButtonArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JLabel createLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<?> createCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemTray createSystemTray() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
