package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

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
		FLIP_ORIGIN_REPLICA_TEXT = "Swap",
		FLIP_ORIGIN_REPLICA_TOOLTIP_TEXT = "Swap Origin / Replica",
		ORIGIN_LABEL = "Origin",
		ORIGIN_TOOLTIP_LABEL = "Source Database",
		REPLICA_LABEL = "Replica",
		REPLICA_TOOLTIP_LABEL = "Destination Database",
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
	private JLabel
		originLabel,
		replicaLabel;
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
		JPanel saveCancelPanel = new JPanel();
		saveCancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
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
				}
				else
				{
					originLabel.setText(ORIGIN_LABEL);
					originLabel.setToolTipText(ORIGIN_TOOLTIP_LABEL);
					replicaLabel.setText(REPLICA_LABEL);
					replicaLabel.setToolTipText(REPLICA_TOOLTIP_LABEL);
				}
			}
		});
		
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
		
		saveCancelPanel.add(flipOriginAndReplica);
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
	
    public static void main(String[] args) {
        try {
            // 1. Tell Swing to draw the window decorations instead of the OS
            JFrame.setDefaultLookAndFeelDecorated(true);
            
            // 2. Change the active and inactive title bar background colors
            UIManager.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.DARK_GRAY));
            UIManager.put("inactiveCaption", new javax.swing.plaf.ColorUIResource(Color.LIGHT_GRAY));
            
            // 3. Change the text color on the title bar
            UIManager.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.WHITE));
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Standard JFrame creation
        JFrame frame = new JFrame("Custom Title Bar Color");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
	
}
