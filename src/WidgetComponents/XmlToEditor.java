package WidgetComponents;

import java.awt.BorderLayout;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import ActionListeners.RemoveEditorTabActionListener;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class XmlToEditor extends TabbedPanel implements DependentRedrawableFrameListener
{
	private static final long serialVersionUID = 1888L;

	public static final String 
		MENU_ITEM_REMOVE_TEXT = "remove",
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	
	private DependentRedrawableFrame editorFrame;
	
	@Override
	public void setDependentRedrawableFrame(DependentRedrawableFrame editorFrame)
	{
		this.editorFrame = editorFrame;
	}
	
	@Override
	public void rebuildPanel()
	{
		int select = getSelectedIndex();
		destroyPanel();
		buildEditors();
		setSelectedIndex(select);
	}
	
	@Override
	public void destroyPanel()
	{
		this.removeAll();
		editorFrame.remove(this);
		editorFrame.repaint();
		editorFrame.validate();
	}
	
	@Override
	public void setSelectedIndex(int index)
	{
		if(this.getTabCount()-1 >= index)
			super.setSelectedIndex(index);
		else if(this.getTabCount() != 0)
			super.setSelectedIndex(0);
	}
	
	private void buildEditors()
	{
		int count = 0;
		
		JPopupMenu pm = new JPopupMenu();
		JMenuItem mi = new JMenuItem(MENU_ITEM_REMOVE_TEXT);
		mi.addActionListener(new RemoveEditorTabActionListener(this));
		pm.setEnabled(true);
		pm.add(mi);
		this.setComponentPopupMenu(pm);
		
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties() != null)
		{
			for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
			{
				String compName = wcp.getRef() + COMPONENT_SUFFIX + count;
				
				JPanel outP = new JPanel(new BorderLayout());
				JPanel outI = new JPanel(new BorderLayout());
				WidgetPropertiesPanelArray pArray = new WidgetPropertiesPanelArray();
				outI.add(pArray, BorderLayout.NORTH);
				JScrollPane js = new JScrollPane(outI);
				js.getVerticalScrollBar().setUnitIncrement(15);
				
				pArray.buildPropertiesArray(wcp);
				pArray.setDependentRedrawableFrame(editorFrame);
				outP.add(js, BorderLayout.CENTER);
				this.add(outP);
				this.setTitleAt(count++, compName);
			}
		}
		editorFrame.add(this, BorderLayout.CENTER);
		editorFrame.validate();
	}

}
