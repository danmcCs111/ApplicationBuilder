package WidgetComponents;

import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetComponentInterfaces.RedrawablePanel;

public class XmlToEditorJTree implements RedrawableFrameListener, RedrawablePanel
{
	private static final String 
		MENU_ITEM_REMOVE_TEXT = "remove",
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	
	private RedrawableFrame editorFrame;

	@Override
	public void setRedrawableFrame(RedrawableFrame editorFrame)
	{
		this.editorFrame = editorFrame;
	}

	@Override
	public void rebuildPanel() 
	{
		
	}

	@Override
	public void destroyPanel() 
	{
		
	}
	
	private void buildEditors()
	{
		int count = 0;
		
//		JPopupMenu pm = new JPopupMenu();
//		JMenuItem mi = new JMenuItem(MENU_ITEM_REMOVE_TEXT);
//		mi.addActionListener(new RemoveEditorTabActionListener(this));
//		pm.setEnabled(true);
//		pm.add(mi);
//		this.setComponentPopupMenu(pm);
//		
//		if(WidgetBuildController.getInstance().getWidgetCreatorProperties() != null)
//		{
//			for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
//			{
//				String compName = wcp.getRef() + COMPONENT_SUFFIX + count;
//				
//				JPanel outI = new JPanel(new BorderLayout());
//				WidgetPropertiesPanelArray pArray = new WidgetPropertiesPanelArray();
//				outI.add(pArray, BorderLayout.NORTH);
//				JScrollPane js = new JScrollPane(outI);
//				js.getVerticalScrollBar().setUnitIncrement(15);
//				
//				pArray.buildPropertiesArray(wcp);
//				pArray.setDependentRedrawableFrame(editorFrame);
//				this.add(js);
//				this.setTitleAt(count++, compName);
//			}
//		}
//		editorFrame.add(this, BorderLayout.CENTER);
//		editorFrame.validate();
	}
}
