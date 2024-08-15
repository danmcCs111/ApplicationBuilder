package WidgetUtility;

import java.awt.MenuItem;
import java.awt.SystemTray;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public interface WidgetCreationOptions {

	public abstract JFrame createFrame();
	public abstract JPanel createPanel();
	public abstract JScrollPane createScrollPane();
	public abstract JMenuBar createMenuOption();
	public abstract MenuItem createMenuItem();
	public abstract JButton createButton();
	public abstract ArrayList<JButton> createButtonArray();
	public abstract JLabel createLabel();
	public abstract ArrayList<?> createCollection();
	public abstract SystemTray createSystemTray();

}
