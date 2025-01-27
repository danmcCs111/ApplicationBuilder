package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.OpenParameterEditorActionListener;

public class ApplicationLayoutEditor extends RedrawableFrame
{
	private static final long serialVersionUID = 1L;
	private static final String 
		TITLE = "Application Layout Editor",
		FILE_MENU_TEXT = "File",
		MENU_ITEM_OPEN_TEXT = "Open",
		XML_PATH_SUFFIX = "\\src\\ApplicationBuilder\\data\\ ",
		XML_FILTER_TITLE = "XML Build File",
		XML_FILTER = "xml";
	
	private static final Dimension 
		WINDOW_LOCATION = new Dimension(550, 50),
		WINDOW_SIZE = new Dimension(480, 640);
	
	private JButton openParameterButton;
	
	public ApplicationLayoutEditor()
	{
		this.setTitle(TITLE);
		this.setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(FILE_MENU_TEXT);
		
		JMenuItem open = new JMenuItem(MENU_ITEM_OPEN_TEXT);
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				String currentDirectory = System.getProperty("user.dir");
				LoggingMessages.printOut("Current Directory: " + currentDirectory);
				LoggingMessages.printOut("Dialog Directory: " + currentDirectory + XML_PATH_SUFFIX);
				File f = new File(currentDirectory + XML_PATH_SUFFIX);
				jfc.setFileFilter(new FileNameExtensionFilter(XML_FILTER_TITLE, XML_FILTER));
				jfc.setSelectedFile(f);
				int choice = jfc.showOpenDialog(ApplicationLayoutEditor.this);
				
				File chosenFile = jfc.getSelectedFile();
				if(chosenFile != null)
				{
					LoggingMessages.printOut("Chosen File: " + chosenFile.getAbsolutePath());
					ApplicationEditorLauncher.buildAppFromXML(chosenFile.getAbsolutePath());
				}
			}
		});
		menu.add(open);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		buildOpenParmeterEditorButton();
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void buildOpenParmeterEditorButton()
	{
		//setup add Property button
		openParameterButton = new JButton("Add Widget");
		openParameterButton.addActionListener(new OpenParameterEditorActionListener());
		this.add(openParameterButton, BorderLayout.SOUTH);
	}

	@Override
	public void clearInnerPanels() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rebuildInnerPanels() {
		// TODO Auto-generated method stub
		
	}
	
}
