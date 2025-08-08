package WidgetComponents;

import java.util.ArrayList;

import javax.swing.JPanel;

import ActionListeners.EditorStateChangeListener;
import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
import Params.ParameterEditor;
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
	private ArrayList<ScheduledCommand> scs; 
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
			scheduledCommandEditors.add(scEditor);
			this.add(scEditor);
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
	
	private void clearEditor()
	{
		scie.clearArrayList();
		scheduledCommandEditors.clear();
		if(scs != null)
		{
			scs.clear();
			scs.add((ScheduledCommand) blankEditor.getComponentValueObj());
		}
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
		clearEditor();
		scs = (ArrayList<ScheduledCommand>) scie.openXml(
				this, 
				scie.getFileTypeTitle(), 
				scie.getFileTypeFilter(), 
				scie.getDefaultDirectoryRelative()
		);
		this.remove(blankEditor);
		clearWidgets();
		buildWidgets(scs);
		this.add(blankEditor);//reposition.
		this.getRootPane().getParent().validate();
	}

	@Override
	public void notifyEditorChanged(ParameterEditor editor) 
	{
		ScheduledCommandEditor sde = (ScheduledCommandEditor) editor;
		if(sde.equals(blankEditor))
		{
			addBlankEditor(sde);
		}
		else
		{
			//check
			if(!checkEditorFilledState(sde))
			{
				//remove blank and make current blank
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
		return scs;
	}

}
