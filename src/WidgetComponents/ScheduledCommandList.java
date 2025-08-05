package WidgetComponents;

import java.util.ArrayList;

import javax.swing.JPanel;

import ActionListeners.EditorStateChangeListener;
import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.OpenActionExtension;
import WidgetExtensions.SaveActionExtension;

public class ScheduledCommandList extends JPanel implements PostWidgetBuildProcessing, SaveActionExtension, OpenActionExtension, EditorStateChangeListener
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
		addBlankEditor();
	}
	
	private void buildWidgets(ArrayList<ScheduledCommand> scs)
	{
		for(ScheduledCommand sc : scs)
		{
			ScheduledCommandEditor scEditor = new ScheduledCommandEditor();
			scEditor.setComponentValue(sc);
			scheduledCommandEditors.add(scEditor);
			this.add(scEditor);
		}
		this.getRootPane().getParent().validate();
	}
	
	private void addBlankEditor()
	{
		if( blankEditor == null || 
			(((ScheduledCommand)blankEditor.getComponentValueObj()) != null && 
			((ScheduledCommand)blankEditor.getComponentValueObj()).getCommandBuild() != null)
		) //filled requirements
		{
			if(blankEditor != null)
			{
				blankEditor.removeEditorChangeListener(this);
			}
			blankEditor = new ScheduledCommandEditor();
			blankEditor.addEditorChangeListener(this);
			scheduledCommandEditors.add(blankEditor);
			this.add(blankEditor);
		}
		//else not filled requirements and already active
		
	}
	
	public boolean checkBlankEditorIsEmpty()
	{
		return (blankEditor.getComponentValueObj() != null);
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().getParent().validate();
	}

	@Override
	public void performSave() {
		String xml = scie.toXmlFromEditorList(scheduledCommandEditors);
		scie.performSave(this, xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void performOpen() 
	{
		scs = (ArrayList<ScheduledCommand>) scie.openXml(
				this, 
				scie.getFileTypeTitle(), 
				scie.getFileTypeFilter(), 
				scie.getDefaultDirectoryRelative()
		);
		this.remove(blankEditor);
		buildWidgets(scs);
		this.add(blankEditor);//reposition.
		this.getRootPane().getParent().validate();
	}

	@Override
	public void notifyEditorChanged() 
	{
		addBlankEditor();//TODO.
		this.getRootPane().getParent().validate();
	}

}
