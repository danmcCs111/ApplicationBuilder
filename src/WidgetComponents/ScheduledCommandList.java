package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import ActionListeners.EditorStateChangeListener;
import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.FileNewActionExtension;
import WidgetExtensions.OpenActionExtension;
import WidgetExtensions.SaveActionExtension;
import WidgetExtensions.ScheduledCommandExecuteExtension;

public class ScheduledCommandList extends JPanel implements PostWidgetBuildProcessing, 
	FileNewActionExtension, SaveActionExtension, OpenActionExtension, 
	EditorStateChangeListener, ScheduledCommandExecuteExtension
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ScheduledCommandEditor> scheduledCommandEditors = new ArrayList<ScheduledCommandEditor>();
	private ScheduledCommandImportExport scie = new ScheduledCommandImportExport();
	private HashMap<Integer, JPanel> commandEditorDeleteButtonList = new HashMap<Integer, JPanel>();
	private int indexCount = 0;
	
	private ScheduledCommandEditor blankEditor;
	
	public ScheduledCommandList()
	{
		
	}
	
	public void buildWidgets()
	{
		addBlankEditor(null);
	}
	
	private void buildWidgets(ArrayList<ScheduledCommand> scs)
	{
		for(ScheduledCommand sc : scs)
		{
			ScheduledCommandEditor scEditor = new ScheduledCommandEditor();
			scEditor.setComponentValue(sc);
			scEditor.getEditorStateChangedDistributor().addEditorChangeListener(this);
			addDeleteButton(scEditor);
			
			scheduledCommandEditors.add(scEditor);
		}
		this.getRootPane().getParent().validate();
	}
	
	private void clearWidgets()
	{
		this.removeAll();
	}
	
	private void addBlankEditor(ScheduledCommandEditor pe)
	{
		if( blankEditor == null || checkEditorFilledState(pe))
		{
			if(blankEditor != null)
			{
				blankEditor.getEditorStateChangedDistributor().removeEditorChangeListener(this);
			}
			blankEditor = new ScheduledCommandEditor();
			blankEditor.getEditorStateChangedDistributor().addEditorChangeListener(this);
			scheduledCommandEditors.add(blankEditor);
			this.add(blankEditor);
		}
		//else not filled requirements and already active
	}
	
	private boolean checkEditorFilledState(ScheduledCommandEditor sce)
	{
		return (
				((ScheduledCommand)sce.getComponentValueObj()) != null && 
				((ScheduledCommand)sce.getComponentValueObj()).getCommandBuild() != null
		);
	}
	
	public boolean checkBlankEditorIsEmpty()
	{
		return (blankEditor.getComponentValueObj() != null);
	}
	
	private void removeTimeOption(int index)
	{
		LoggingMessages.printOut(index + "");
		int remIndex = scheduledCommandEditors.indexOf(commandEditorDeleteButtonList.get(index).getComponent(1));
		JPanel p = commandEditorDeleteButtonList.get(index);
		scheduledCommandEditors.remove(remIndex);//2nd.
		commandEditorDeleteButtonList.remove(index);
		
		this.remove(p);
		this.validate();
		this.getRootPane().getParent().validate();
	}
	
	private void addDeleteButton(ScheduledCommandEditor sde)
	{
		JPanel scPanel = new JPanel();
		scPanel.setLayout(new BorderLayout());
		JButton deleteButton = new JButton("X");
		int count = indexCount;
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeTimeOption(count);
			}
		});
		commandEditorDeleteButtonList.put(count, scPanel);
		
		scPanel.add(deleteButton, BorderLayout.WEST);
		scPanel.add(sde, BorderLayout.CENTER);
		this.add(scPanel);
		
		indexCount++;
	}
	
	private void clearEditor()
	{
		scie.clearArrayList();
		scheduledCommandEditors.clear();
		scheduledCommandEditors.add(blankEditor);
		clearWidgets();
		this.add(blankEditor);
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().getParent().validate();
	}
	
	@Override
	public void performNewFile() 
	{
		clearEditor();
		this.getRootPane().getParent().validate();
	}

	@Override
	public void performSave() 
	{
		String xml = scie.toXmlFromEditorList(scheduledCommandEditors);
		scie.performSave(this, xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void performOpen() 
	{
		ArrayList<ScheduledCommand> tmp = (ArrayList<ScheduledCommand>) scie.openXml(
				this, 
				scie.getFileTypeTitle(), 
				scie.getFileTypeFilter(), 
				scie.getDefaultDirectoryRelative()
		);
		if(tmp == null)
			return;
		clearEditor();
		this.remove(blankEditor);
		clearWidgets();
		buildWidgets(tmp);
		this.add(blankEditor);//reposition.
		this.getRootPane().getParent().validate();
	}

	@Override
	public void notifyEditorChanged(ParameterEditor editor) 
	{
		ScheduledCommandEditor sde = (ScheduledCommandEditor) editor;
		if(sde.equals(blankEditor))
		{
			if(checkEditorFilledState(sde))
			{
				addDeleteButton(sde);
			}
			addBlankEditor(sde);
		}
		else
		{
			if(!checkEditorFilledState(sde))
			{
				this.remove(sde);
				blankEditor.getEditorStateChangedDistributor().removeEditorChangeListener(this);
//				blankEditor.destroy();//TODO.
				this.remove(blankEditor);
				blankEditor = sde;
				this.add(blankEditor);//reposition
			}
		}
		this.getRootPane().getParent().validate();
	}

	@Override
	public ArrayList<ScheduledCommand> getScheduledCommands() 
	{
		ArrayList<ScheduledCommand> tmp = new ArrayList<ScheduledCommand>();
		for(ScheduledCommandEditor sce : scheduledCommandEditors)
		{
			ScheduledCommand sc = (ScheduledCommand) sce.getComponentValueObj();
			if(sc != null)
			{
				tmp.add(sc);
			}
		}
		return tmp;
	}

}
