package WidgetComponents;

import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetUtility.WidgetBuildController;

public class ApplicationLayoutEditor extends DependentRedrawableFrame implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1897L;
	
	private BuilderWindow builderWindow;
	private XmlToEditor xe;
	
	public XmlToEditor getXmlToEditor()
	{
		return xe;
	}
	
	public void setBuilderWindow(BuilderWindow builderWindow)
	{
		this.builderWindow = builderWindow;
	}
	
	@Override
	public void updateDependentWindow()
	{
		if(builderWindow != null)
		{
			builderWindow.clearInnerPanels();
			builderWindow.rebuildInnerPanels();
		}
	}

	@Override
	public void clearInnerPanels() 
	{
		xe.destroyPanel();
	}

	@Override
	public void rebuildInnerPanels() 
	{
		if(xe == null)
		{
			xe = new XmlToEditor();
			xe.setDependentRedrawableFrame(this);
			xe.rebuildPanel();
		}
		else if(xe != null)
		{
			xe.rebuildPanel();
		}
	}

	@Override
	public void postExecute() 
	{
		//advance to remove focus from layout editor.
		WidgetBuildController.getInstance().newWidgetBuild();
	}
	
}
