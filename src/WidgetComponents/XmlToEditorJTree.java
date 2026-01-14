package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import ActionListenersImpl.RemoveEditorTabActionListener;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetComponentInterfaces.WidgetBuildIndexSelector;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class XmlToEditorJTree extends JPanel implements RedrawableFrameListener, WidgetBuildIndexSelector
{
	private static final long serialVersionUID = 1L;

	private static final String 
		MENU_ITEM_REMOVE_TEXT = "remove",
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	private static int
		SCROLL_UNIT_INC = 15;
	
	private RedrawableFrame editorFrame;
	private JTree tree;
	private JPanel
		treePanel,
		viewPanel;
	private JScrollPane
		treeJs,
		js;
	
	public XmlToEditorJTree()
	{
		this.setLayout(new BorderLayout());
	}

	@Override
	public void setRedrawableFrame(RedrawableFrame editorFrame)
	{
		this.editorFrame = editorFrame;
	}

	@Override
	public void rebuildPanel()
	{
//		int select = getSelectedIndex();
		destroyPanel();
		buildEditors();
//		setSelectedIndex(select);
	}
	
	@Override
	public void destroyPanel()
	{
		this.removeAll();
		editorFrame.remove(this);
		editorFrame.validate();
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
			String topName = PathUtility.filterPathToFilename(WidgetBuildController.getInstance().getFilename());
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(topName);
			
			for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
			{
				String compName = wcp.getRef() + COMPONENT_SUFFIX + count;
				
				WidgetPropertiesPanelArray pArray = new WidgetPropertiesPanelArray(); 
				
				pArray.buildPropertiesArray(wcp);
				pArray.setRedrawableFrame(editorFrame);
				
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(pArray);
				top.add(dmtn);
			}
			viewPanel = new JPanel();
			viewPanel.setLayout(new BorderLayout());
			
			treePanel = new JPanel();
			treePanel.setLayout(new BorderLayout());
			treeJs = new JScrollPane(treePanel);
			treeJs.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
			
			
			tree = new JTree(top);
			tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			tree.addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) 
				{
					int index = getSelectedIndex();
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
					viewPanel.removeAll();
					if(node.getUserObject() instanceof Component)
					{
						Component c = (Component) node.getUserObject();
						if(js != null) viewPanel.remove(js);
						setScrollableComponent(c);
						viewPanel.add(js, BorderLayout.CENTER);
					}
					viewPanel.validate();
					viewPanel.repaint();
					XmlToEditorJTree.this.revalidate();
					editorFrame.validate();
				}
			});
		}
		treePanel.add(tree, BorderLayout.CENTER);
		this.add(treeJs, BorderLayout.WEST);
		this.add(viewPanel, BorderLayout.CENTER);
		
		editorFrame.add(this, BorderLayout.CENTER);
		editorFrame.validate();
	}
	
	private void setScrollableComponent(Component c)
	{
		JPanel outI = new JPanel();
		outI.setLayout(new BorderLayout());
		outI.add(c, BorderLayout.NORTH);
		js = new JScrollPane(outI);
		js.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
	}
	
	private void setSelectedIndex(int selIndex)
	{
		tree.setSelectionRow(selIndex);
	}

	@Override
	public int getSelectedIndex() 
	{
		int[] selRow = tree.getSelectionRows();
		if(selRow != null && selRow.length != 0)
		{
			LoggingMessages.printOut(selRow[0] + "");
			return selRow[0];
		}
		return 0;
	}

	@Override
	public String getTitleAt(int index) 
	{
		Component c = (Component)tree.getComponent(index);
		return c.toString();
	}
}
