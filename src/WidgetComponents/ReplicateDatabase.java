package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ApplicationBuilder.ShellHeadlessExecutor;
import Graphics2D.ColorTemplate;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import ObjectTypeConversion.ValueChangedListener;
import ObjectTypeConversionEditors.DirectorySelectionEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class ReplicateDatabase extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static String
		REPLICATE_COMMAND = "rsync.sh",
		SAVE_BUTTON_TEXT = "Replicate",
		CLOSE_BUTTON_TEXT = "Close",
		CANCEL_BUTTON_TEXT = "Cancel",
		ORIGIN_LABEL = "Origin",
		REPLICA_LABEL = "Replica",
		DATABASES_ORIGIN_LOCATION = "./plugin-projects/SQLiteInstall/ ",
		FILE_FILTER = ".db",
		DATABASES_REPLICA_LOCATION = null;
	
	private static FileSelection
		replicateCommand = new FileSelection(
				new DirectorySelection(DATABASES_ORIGIN_LOCATION).getFullPath().trim() + REPLICATE_COMMAND, 
				false
		);
	
	private JScrollPane
		scrollPane;
	private JList<String>
		databasesList;
	private DirectorySelectionEditor
		dsSelectionReplicaEditor,
		dsSelectionOriginEditor;
	private JButton
		saveButton,
		cancelButton;
	
	private boolean 
		cancelFlag = false;
	
	public ReplicateDatabase()
	{
		
	}
	
	public static void setReplicateLocation(DirectorySelection ds)
	{
		replicateCommand = new FileSelection(ds.getFullPath().trim() + REPLICATE_COMMAND, false);
	}
	
	public static void setDatabasesOriginLocation(DirectorySelection ds)
	{
		DATABASES_ORIGIN_LOCATION = ds.getRelativePath();
	}
	
	//TODO need absolute path in editor.
//	public static void setDatabasesReplicaLocation(DirectorySelection ds)
//	{
//		DATABASES_REPLICA_LOCATION = ds.getRelativePath();
//	}
	
	private void buildWidgets()
	{
		databasesList = new JList<String>();
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(databasesList);
		
		JScrollPane northScrollPane = buildNorthPanel();
		JPanel southPanel = buildSouthPanel();
		
		this.add(northScrollPane, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(southPanel, BorderLayout.SOUTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), northScrollPane);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
	}
	
	private JScrollPane buildNorthPanel()
	{
		JScrollPane northScroll = new JScrollPane();
		JPanel 
			innerPanelO = new JPanel(),
			innerPanelR = new JPanel(),
			northPanel = new JPanel();
		
		innerPanelO.setLayout(new FlowLayout(FlowLayout.LEFT));
		innerPanelR.setLayout(new FlowLayout(FlowLayout.LEFT));
		northPanel.setLayout(new GridLayout(0,1));
		
		dsSelectionOriginEditor = new DirectorySelectionEditor();
		dsSelectionOriginEditor.setIsRelativePath(true);
		dsSelectionReplicaEditor = new DirectorySelectionEditor();
		dsSelectionReplicaEditor.setIsRelativePath(false);
		
		dsSelectionOriginEditor.setComponentValue(new DirectorySelection(DATABASES_ORIGIN_LOCATION));
		dsSelectionOriginEditor.addValueChangedListener(new ValueChangedListener() {
			@Override
			public void valueChanged(Object o) {
				DirectorySelection dsO =  (DirectorySelection) dsSelectionOriginEditor.getComponentValueObj();
				setReplicateLocation(dsO);
				refreshDatabasesList(dsO);
			}
		});
		DirectorySelection dsO =  (DirectorySelection) dsSelectionOriginEditor.getComponentValueObj();
		refreshDatabasesList(dsO);
		
		if(DATABASES_REPLICA_LOCATION != null)
		{
			dsSelectionReplicaEditor.setComponentValue(new DirectorySelection(DATABASES_REPLICA_LOCATION, false));
		}
		else
		{
			dsSelectionReplicaEditor.setComponentValue(null);
		}
		
		innerPanelO.add(new JLabel(ORIGIN_LABEL));
		innerPanelO.add(dsSelectionOriginEditor);
		
		innerPanelR.add(new JLabel(REPLICA_LABEL));
		innerPanelR.add(dsSelectionReplicaEditor);
		
		northPanel.add(innerPanelO);
		northPanel.add(innerPanelR);
		
		northScroll.setViewportView(northPanel);
		
		return northScroll;
	}
	
	private JPanel buildSouthPanel()
	{
		JPanel saveCancelPanel = new JPanel();
		saveCancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		saveButton = new JButton(SAVE_BUTTON_TEXT);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//perform replication.
				replicate();
			}
		});
		cancelButton = new JButton(CLOSE_BUTTON_TEXT);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cancelButton.getText().equals(CANCEL_BUTTON_TEXT))
				{
					cancelFlag = true;
				}
				else
				{
					System.exit(0);
				}
			}
		});
		
		saveCancelPanel.add(saveButton);
		saveCancelPanel.add(cancelButton);
		
		return saveCancelPanel;
	}
	
	private void replicate()
	{
		DirectorySelection 
			dsR = (DirectorySelection) dsSelectionReplicaEditor.getComponentValueObj();
		List<String> selectedValues = databasesList.getSelectedValuesList();
		
		cancelButton.setText(CANCEL_BUTTON_TEXT);
		saveButton.setEnabled(false);
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				for(String select : selectedValues)
				{
					if(cancelFlag)
						break;
					
					String [] args = new String [] 
					{
						replicateCommand.getPathLinux() + " " +
						select + " " + //same location as replicate command.
						dsR.getPathLinux().trim() + select + " "
					};
					
					LoggingMessages.printOut(LoggingMessages.combine(args));
					ShellHeadlessExecutor.loadHideOption();
					ShellHeadlessExecutor.run(args, true);
				}
				saveButton.setEnabled(true);
				cancelButton.setText(CLOSE_BUTTON_TEXT);
			}
		};
		Thread t = new Thread(r);
		t.start();
		
	}
	
	private void refreshDatabasesList(DirectorySelection ds)
	{
		if(ds == null)
			return;
		
		ArrayList<String> filesList = PathUtility.getOSFileList(ds.getFullPath(), FILE_FILTER);
		databasesList.setListData(filesList.toArray(new String[] {}));
		this.validate();
	}
	
	@Override
	public void postExecute() 
	{
		this.setLayout(new BorderLayout());
		buildWidgets();
	}
	
}
