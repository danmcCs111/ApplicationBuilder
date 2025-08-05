package WidgetComponents;

import javax.swing.JPanel;

import Editors.ScheduledCommandEditor;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ScheduledCommandList extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	public ScheduledCommandList()
	{
		
	}
	
	public void buildWidgets()
	{
		ScheduledCommandImportExport scie = new ScheduledCommandImportExport();
		this.add(new ScheduledCommandEditor());
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.validate();
	}

}
