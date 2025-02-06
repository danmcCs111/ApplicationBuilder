package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ActionListeners.OpenParameterEditorActionListener;
import Properties.PathUtility;
import WidgetUtility.XmlToEditor;

public class ApplicationLayoutEditor extends RedrawableFrame
{
	private static final long serialVersionUID = 1887L;
	private static final String 
		TITLE = "Application Layout Editor",
		FILE_MENU_TEXT = "File",
		MENU_ITEM_OPEN_TEXT = "Open",
		MENU_ITEM_CLOSE_TEXT = "Close Project",
		XML_PATH_SUFFIX = "\\src\\ApplicationBuilder\\data\\ ",
		XML_FILTER_TITLE = "XML Build File",
		XML_FILTER = "xml";
	
	public static final Dimension 
		WINDOW_LOCATION = new Dimension(550, 10),
		WINDOW_SIZE = new Dimension(680, 640);
	
	private JButton openParameterButton;
	private XmlToEditor xe;
	
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
				String currentDirectory = PathUtility.getCurrentDirectory();
				LoggingMessages.printOut("Current Directory: " + currentDirectory);
				LoggingMessages.printOut("Dialog Directory: " + currentDirectory + XML_PATH_SUFFIX);
				File f = new File(currentDirectory + XML_PATH_SUFFIX);
				jfc.setFileFilter(new FileNameExtensionFilter(XML_FILTER_TITLE, XML_FILTER));
				jfc.setSelectedFile(f);
				
				int choice = jfc.showOpenDialog(ApplicationLayoutEditor.this);
				
				File chosenFile = jfc.getSelectedFile();
				if(chosenFile != null)
				{
					LoggingMessages.printOut("Chosen File: " + choice + " " + chosenFile.getAbsolutePath());
					WidgetBuildController.readProperties(chosenFile);
					xe = new XmlToEditor(WidgetBuildController.getWidgetCreationProperties(), ApplicationLayoutEditor.this);
					OpenParameterEditorActionListener opListener = new OpenParameterEditorActionListener(xe);
					openParameterButton.addActionListener(opListener);
					xe.buildEditors();
				}
			}
		});
		menu.add(open);
		
		JMenuItem closeProj = new JMenuItem(MENU_ITEM_CLOSE_TEXT);
		closeProj.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WidgetBuildController.destroyGeneratedFrame();
				xe.destroyEditors();
			}
		});
		menu.add(closeProj);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		buildEditorButtons();
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void buildEditorButtons()
	{
		JPanel p = new JPanel(new GridLayout(0,2));
		
		openParameterButton = new JButton("Add Widget Property");
		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(WidgetBuildController.getWidgetCreationProperties() != null)
				{
					WidgetBuildController.generateGraphicalInterface(WidgetBuildController.getWidgetCreationProperties());
				}
			}
		});
		p.add(openParameterButton);
		p.add(generateButton);
		
		this.add(p, BorderLayout.SOUTH);
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
