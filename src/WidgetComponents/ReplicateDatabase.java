package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import ApplicationBuilder.ShellHeadlessExecutor;
import Graphics2D.ColorTemplate;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import ObjectTypeConversion.ValueChangedListener;
import ObjectTypeConversionEditors.DirectorySelectionEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;
import Properties.StringUtility;
import WidgetComponentInterfaces.FileView;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class ReplicateDatabase extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static String
		FILE_LIST_FORMAT = "<arg> [<arg>] (size: <arg> KB)",
		STATUS_FORMAT = "Replicating: <arg>",
		REPLACE_ARG = "<arg>",
		REPLICATE_COMMAND = "rsync.sh",
		SAVE_BUTTON_TEXT = "Replicate",
		CLOSE_BUTTON_TEXT = "Close",
		CANCEL_BUTTON_TEXT = "Cancel",
		FLIP_ORIGIN_REPLICA_TEXT = "Swap",
		FLIP_ORIGIN_REPLICA_TOOLTIP_TEXT = "Swap Origin / Replica",
		ORIGIN_LABEL = "Origin",
		ORIGIN_TOOLTIP_LABEL = "Source Database",
		REPLICA_LABEL = "Replica",
		REPLICA_TOOLTIP_LABEL = "Destination Database",
		DATABASES_ORIGIN_LOCATION = "./plugin-projects/SQLiteInstall/ ",
		FILE_FILTER = ".db",
		DATABASES_REPLICA_LOCATION = null;
	private static long
		BYTE_SIZE_CONVERT = 1024; //in KB
	
	private static FileSelection
		replicateCommand = new FileSelection(
				new DirectorySelection(DATABASES_ORIGIN_LOCATION).getFullPath().trim() + REPLICATE_COMMAND, 
				false
		);
	
	private JScrollPane
		scrollPane;
	private JList<FileView>
		databasesList;
	private JLabel
		originLabel,
		replicaLabel,
		statusLabel;
	private DirectorySelectionEditor
		dsSelectionReplicaEditor,
		dsSelectionOriginEditor;
	private JToggleButton
		flipOriginAndReplica;
	private JButton
		saveButton,
		cancelButton;
	
	private boolean 
		isFlippedOriginReplica = false,
		cancelFlag = false;
	
	public ReplicateDatabase()
	{
		
	}
	
	public static void setFileListFormat(String format)
	{
		FILE_LIST_FORMAT = format;
	}
	public static void setStatusFormat(String format)
	{
		STATUS_FORMAT = format;
	}
	public static void setByteSizeConvert(int convert)
	{
		BYTE_SIZE_CONVERT = convert;
	}
	public static void setReplaceArg(String replArg)
	{
		REPLACE_ARG = replArg;
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
		databasesList = new JList<FileView>();
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(databasesList);
		
		JPanel southPanel = buildSouthPanel();
		JScrollPane northScrollPane = buildNorthPanel();
		
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
				if(!flipOriginAndReplica.isSelected())
				{
					DirectorySelection dsO =  (DirectorySelection) dsSelectionOriginEditor.getComponentValueObj();
					setReplicateLocation(dsO);
					refreshDatabasesList(dsO);
				}
			}
		});
		DirectorySelection dsO =  (DirectorySelection) dsSelectionOriginEditor.getComponentValueObj();
		setReplicateLocation(dsO);
		refreshDatabasesList(dsO);
		
		if(DATABASES_REPLICA_LOCATION != null)
		{
			DirectorySelection dsR = new DirectorySelection(DATABASES_REPLICA_LOCATION, false);
			dsSelectionReplicaEditor.setComponentValue(dsR);
			saveButton.setEnabled(dsR != null);
		}
		else
		{
			dsSelectionReplicaEditor.setComponentValue(null);
			saveButton.setEnabled(false);
		}
		dsSelectionReplicaEditor.addValueChangedListener(new ValueChangedListener() {
			@Override
			public void valueChanged(Object o) {
				DirectorySelection dsR =  (DirectorySelection) dsSelectionReplicaEditor.getComponentValueObj();
				if(flipOriginAndReplica.isSelected())
				{
					refreshDatabasesList(dsR);
				}
				if(dsR != null)
				{
					saveButton.setEnabled(true);
				}
			}
		});
		
		originLabel = new JLabel(ORIGIN_LABEL);
		originLabel.setToolTipText(ORIGIN_TOOLTIP_LABEL);
		innerPanelO.add(originLabel);
		innerPanelO.add(dsSelectionOriginEditor);
		
		replicaLabel = new JLabel(REPLICA_LABEL);
		replicaLabel.setToolTipText(REPLICA_TOOLTIP_LABEL);
		innerPanelR.add(replicaLabel);
		innerPanelR.add(dsSelectionReplicaEditor);
		
		northPanel.add(innerPanelO);
		northPanel.add(innerPanelR);
		
		northScroll.setViewportView(northPanel);
		
		return northScroll;
	}
	
	private JPanel buildSouthPanel()
	{
		JPanel
			innerRightPanel = new JPanel(),
			innerLeftPanel = new JPanel(),
			saveCancelPanel = new JPanel();
		saveCancelPanel.setLayout(new BorderLayout());
		innerLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		innerRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		statusLabel = new JLabel();
		innerLeftPanel.add(statusLabel);
		
		flipOriginAndReplica = new JToggleButton(FLIP_ORIGIN_REPLICA_TEXT);
		flipOriginAndReplica.setToolTipText(FLIP_ORIGIN_REPLICA_TOOLTIP_TEXT);
		flipOriginAndReplica.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isFlippedOriginReplica = flipOriginAndReplica.isSelected();
				if(isFlippedOriginReplica)
				{
					originLabel.setText(REPLICA_LABEL);
					originLabel.setToolTipText(REPLICA_TOOLTIP_LABEL);
					replicaLabel.setText(ORIGIN_LABEL);
					replicaLabel.setToolTipText(ORIGIN_TOOLTIP_LABEL);
					
					DirectorySelection dsR =  (DirectorySelection) dsSelectionReplicaEditor.getComponentValueObj();
					refreshDatabasesList(dsR);
				}
				else
				{
					originLabel.setText(ORIGIN_LABEL);
					originLabel.setToolTipText(ORIGIN_TOOLTIP_LABEL);
					replicaLabel.setText(REPLICA_LABEL);
					replicaLabel.setToolTipText(REPLICA_TOOLTIP_LABEL);
					
					DirectorySelection dsO =  (DirectorySelection) dsSelectionOriginEditor.getComponentValueObj();
					setReplicateLocation(dsO);
					refreshDatabasesList(dsO);
				}
			}
		});
		
		saveButton = new JButton(SAVE_BUTTON_TEXT);
		saveButton.setEnabled(false);
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
		
		innerRightPanel.add(flipOriginAndReplica);
		innerRightPanel.add(saveButton);
		innerRightPanel.add(cancelButton);
		
		saveCancelPanel.add(innerLeftPanel, BorderLayout.WEST);
		saveCancelPanel.add(innerRightPanel, BorderLayout.EAST);
		
		return saveCancelPanel;
	}
	
	private void replicate()
	{
		DirectorySelection 
			dsR = (DirectorySelection) dsSelectionReplicaEditor.getComponentValueObj();
		List<FileView> selectedValues = databasesList.getSelectedValuesList();
		
		if(dsR == null)
			return;
		
		cancelButton.setText(CANCEL_BUTTON_TEXT);
		saveButton.setEnabled(false);
		flipOriginAndReplica.setEnabled(false);
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() 
			{
				for(FileView select : selectedValues)
				{
					if(cancelFlag)
						break;
					
					String [] args; 
					if(isFlippedOriginReplica)
					{
						args = new String [] 
						{
							replicateCommand.getPathLinux() + " " +
							dsR.getPathLinux().trim() + select + " " +
							select + " " //same location as replicate command.
						};
					}
					else
					{
						args = new String [] 
						{
							replicateCommand.getPathLinux() + " " +
							select + " " + //same location as replicate command.
							dsR.getPathLinux().trim() + select + " "
						};
					}
					String stat = STATUS_FORMAT;
					stat = StringUtility.replaceArg(STATUS_FORMAT, REPLACE_ARG, select.getFilename());
					statusLabel.setText(stat);
					LoggingMessages.printOut(LoggingMessages.combine(args));
					ShellHeadlessExecutor.loadHideOption();
					ShellHeadlessExecutor.run(args, true);
				}
				
				saveButton.setEnabled(true);
				flipOriginAndReplica.setEnabled(true);
				cancelButton.setText(CLOSE_BUTTON_TEXT);
				statusLabel.setText("");
			}
		};
		Thread t = new Thread(r);
		t.start();
		
	}
	
	private void refreshDatabasesList(DirectorySelection ds)
	{
		if(ds == null)
		{
			databasesList.setListData(new FileView [] {});
		}
		else
		{
			ArrayList<String> 
				filesList = PathUtility.getOSFileList(ds.getFullPath(), FILE_FILTER);
			ArrayList<FileView>
				filesFormatted = new ArrayList<FileView>();
			
			for(String fileStr : filesList)
			{
				File 
					f = new File(ds.getFullPath().trim() + fileStr);
				long 
					byteSize = PathUtility.getSizeOfFileInBytes(f),
					sizeConvert = byteSize / BYTE_SIZE_CONVERT;
				String 
					filename = FILE_LIST_FORMAT;
				LocalDateTime 
					ldt = PathUtility.getFileModifiedDate(f);
				
				filename = StringUtility.replaceArg(
						filename, 
						REPLACE_ARG, 
						new String [] {ldt.toLocalDate().toString(), fileStr, sizeConvert + ""}
				);
				FileView fv = new FileView(fileStr, filename);
				filesFormatted.add(fv);
			}
			databasesList.setListData(filesFormatted.toArray(new FileView[] {}));
		}
		this.validate();
	}
	
	@Override
	public void postExecute() 
	{
		this.setLayout(new BorderLayout());
		buildWidgets();
	}
	
}
