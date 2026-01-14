package WidgetComponents;

import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetUtility.WidgetBuildController;

public class ApplicationLayoutEditor extends RedrawableFrame implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1897L;
	
	private BuilderWindow builderWindow;
	private XmlToEditorJTree xe;
	
	public XmlToEditorJTree getXmlToEditor()
	{
		return xe;
	}
	
	public void setBuilderWindow(BuilderWindow builderWindow)
	{
		this.builderWindow = builderWindow;
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
			xe = new XmlToEditorJTree();
			xe.setRedrawableFrame(this);
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
