package WidgetComponentDialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.OpenAndSaveKeepsSubscriber;

public class VideoBookMarksDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static String 
		OPEN_TITLE = "Open Video Bookmark",
		SAVE_TITLE = "Save Video Bookmark",
		OPEN_BUTTON_LABEL = "Open",
		SAVE_BUTTON_LABEL = "Save",
		SAVE_FIELD_LABEL = "Filename: ",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 325);
	private static int
		LIST_WIDTH = 100,
		SAVE_FILE_COLUMN_LENGTH = 15;
	
	private static final String
		PROPERTIES_FILE_ARG_DELIMITER = "@",
		PROPERTIES_FILE_DELIMITER = "=",
		PROPERTIES_FILE_FILTER = ".txt";
	
	private HashMap<String, String[]> filenameAndTitles = new HashMap<String, String[]>();
	
	private JList<String> fileList;
	private JTextArea titlesList;
	private JTextField saveField;
	private JLabel saveLabel;
	private JPanel 
		innerPanel,
		openCancelPanel = new JPanel(),
		openCancelPanelOuter = new JPanel();
	private JButton 
		applyButton,
		cancelButton;
	
	private DirectorySelection chosenFileDirectory;
	private OpenAndSaveKeepsSubscriber openKeepsSubscriber;
	private Container refContainer;
	private boolean save = false;
	private String [] [] props = null;
	
	
	public VideoBookMarksDialog(DirectorySelection chosenFileDirectory, OpenAndSaveKeepsSubscriber openKeepsSubscriber, Container refContainer)
	{
		this(chosenFileDirectory, openKeepsSubscriber, refContainer, null);
	}
	
	public VideoBookMarksDialog(DirectorySelection chosenFileDirectory, OpenAndSaveKeepsSubscriber openKeepsSubscriber, Container refContainer, String [] [] props)
	{
		this.chosenFileDirectory = chosenFileDirectory;
		this.openKeepsSubscriber = openKeepsSubscriber;
		this.refContainer = refContainer;
		this.props = props;
		if(props != null)
		{
			this.save = true;
		}
		setupFileNameAndTitles(chosenFileDirectory);
		buildWidgets();
	}
	
	public File getFileSelection()
	{
		return new File(chosenFileDirectory.getFullPath().strip() + fileList.getSelectedValue());
	}
	
	public File getFileNameTyped()
	{
		String txt = saveField.getText();
		if(txt.isBlank())
			return null;
		
		if(!txt.endsWith(PROPERTIES_FILE_FILTER))
		{
			txt += PROPERTIES_FILE_FILTER;
		}
		return new File(chosenFileDirectory.getFullPath().strip() + txt);
	}
	
	private void buildWidgets()
	{
		this.setTitle(save ? SAVE_TITLE : OPEN_TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				performSelect(true);
			}
		});
		this.setLayout(new BorderLayout());
		
		applyButton = new JButton(save ? SAVE_BUTTON_LABEL : OPEN_BUTTON_LABEL);
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
		
		titlesList = new JTextArea();
		titlesList.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(titlesList);
		String [] titles = filenameAndTitles.keySet().toArray(new String [] {});
		fileList = new JList<String>(titles);
		Dimension d = fileList.getPreferredSize();
		d.width = LIST_WIDTH;
		fileList.setPreferredSize(d);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//TODO.
		fileList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateTextArea();
			}
		});
		fileList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					performSelect(false);
				}
			}
		});
		JScrollPane scrollPaneTitles = new JScrollPane(fileList);
		
		buildSaveCancel();
		
		innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(scrollPaneTitles, BorderLayout.WEST);
		innerPanel.add(scrollPane, BorderLayout.CENTER);
		if(save)
		{
			saveLabel = new JLabel();
			saveLabel.setText(SAVE_FIELD_LABEL);
			saveField = new JTextField();
			saveField.setColumns(SAVE_FILE_COLUMN_LENGTH);
			JPanel saveFilePanel = new JPanel();
			saveFilePanel.setLayout(new FlowLayout());
			saveFilePanel.add(saveLabel);
			saveFilePanel.add(saveField);
			innerPanel.add(saveFilePanel, BorderLayout.SOUTH);
		}
		
		this.add(innerPanel, BorderLayout.CENTER);
		
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		
		this.setVisible(true);
		GraphicsUtil.centerWindow(refContainer, this);
	}
	
	private void updateTextArea()
	{
		String select = fileList.getSelectedValue();
		if(save)
		{
			saveField.setText(select);
		}
		
		String [] selectedTitles = filenameAndTitles.get(select);
		titlesList.setText("");
		String text = "";
		for(String t : selectedTitles)
		{
			text += t + "\n";
		}
		titlesList.setText(text);
	}
	
	private void setupFileNameAndTitles(DirectorySelection chosenFileDirectory)
	{
		ArrayList<String> files = PathUtility.getOSFileList(chosenFileDirectory.getFullPath(), PROPERTIES_FILE_FILTER);
		for(String file : files)
		{
			HashMap <String, String> props = PathUtility.readProperties(
					chosenFileDirectory.getFullPath().strip() + file, PROPERTIES_FILE_DELIMITER);
			String [] titles = new String [props.keySet().size()];
			int i = 0;
			for(String key : props.keySet())
			{
				String [] k = key.split(PROPERTIES_FILE_ARG_DELIMITER);
				titles[i] = k[0];
				LoggingMessages.printOut(k[0]);
				i++;
			}
			filenameAndTitles.put(file, titles);
		}
	}
	
	private void buildSaveCancel()
	{
		openCancelPanelOuter.setLayout(new BorderLayout());
		openCancelPanel.setLayout(new GridLayout(1,2));
		
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					performSelect(false);
			}
		});
		openCancelPanel.add(applyButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performSelect(true);
			}
		});
		openCancelPanel.add(cancelButton);
		
		openCancelPanelOuter.add(openCancelPanel, BorderLayout.EAST);
		this.add(openCancelPanelOuter, BorderLayout.SOUTH);
	}
	
	private void performSelect(boolean cancel)
	{
		if(!save)
		{
			if(cancel)
			{
				openKeepsSubscriber.openKeeps(null);
			}
			else
			{
				openKeepsSubscriber.openKeeps(PathUtility.readProperties(getFileSelection().getAbsolutePath(), PROPERTIES_FILE_DELIMITER));
			}
			VideoBookMarksDialog.this.dispose();
		}
		else
		{
			if(cancel)
			{
				openKeepsSubscriber.saveKeeps(null, null);
			}
			else
			{
				openKeepsSubscriber.saveKeeps(getFileNameTyped(), props);
			}
			VideoBookMarksDialog.this.dispose();
		}
	}
}
