package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.OpenKeepsSubscriber;

public class VideoBookMarksDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static String 
		TITLE = "Select Video Bookmark",
		OPEN_BUTTON_LABEL = "Open",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 325);
	
	private static final String
		PROPERTIES_FILE_ARG_DELIMITER = "@",
		PROPERTIES_FILE_DELIMITER = "=",
		PROPERTIES_FILE_FILTER = ".txt";
	
	private HashMap<String, String[]> filenameAndTitles = new HashMap<String, String[]>();
	
	private JList<String> fileList;
	private JTextArea titlesList;
	private JPanel 
		innerPanel,
		openCancelPanel = new JPanel(),
		openCancelPanelOuter = new JPanel();
	private JButton 
		applyButton = new JButton(OPEN_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	
	private DirectorySelection chosenFileDirectory;
	private OpenKeepsSubscriber openKeepsSubscriber;
	private Container refContainer;
	
	public VideoBookMarksDialog(DirectorySelection chosenFileDirectory, OpenKeepsSubscriber openKeepsSubscriber, Container refContainer)
	{
		this.chosenFileDirectory = chosenFileDirectory;
		this.openKeepsSubscriber = openKeepsSubscriber;
		this.refContainer = refContainer;
		setupFileNameAndTitles(chosenFileDirectory);
		buildWidgets();
	}
	
	public File getFileSelection()
	{
		return new File(chosenFileDirectory.getPathLinux().strip() + fileList.getSelectedValue());
	}
	
	private void buildWidgets()
	{
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		titlesList = new JTextArea();
		titlesList.setEditable(false);
		String [] titles = filenameAndTitles.keySet().toArray(new String [] {});
		fileList = new JList<String>(titles);
		fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//TODO.
		fileList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateTextArea();
			}
		});
		
		buildSaveCancel();
		
		innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(fileList, BorderLayout.WEST);
		innerPanel.add(titlesList, BorderLayout.CENTER);
		
		this.add(innerPanel, BorderLayout.CENTER);
		
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		
		this.setVisible(true);
		GraphicsUtil.centerWindow(refContainer, this);
	}
	
	private void updateTextArea()
	{
		String [] selectedTitles = filenameAndTitles.get(fileList.getSelectedValue());
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
		ArrayList<String> files = PathUtility.getOSFileList(chosenFileDirectory.getPathLinux(), PROPERTIES_FILE_FILTER);
		for(String file : files)
		{
			HashMap <String, String> props = PathUtility.readProperties(
					chosenFileDirectory.getPathLinux().strip() + file, PROPERTIES_FILE_DELIMITER);
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
				openKeepsSubscriber.openKeeps(PathUtility.readProperties(getFileSelection().getAbsolutePath(), PROPERTIES_FILE_DELIMITER));
				VideoBookMarksDialog.this.dispose();
			}
		});
		openCancelPanel.add(applyButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openKeepsSubscriber.openKeeps(null);
				VideoBookMarksDialog.this.dispose();
			}
		});
		openCancelPanel.add(cancelButton);
		
		openCancelPanelOuter.add(openCancelPanel, BorderLayout.EAST);
		this.add(openCancelPanelOuter, BorderLayout.SOUTH);
	}

}
