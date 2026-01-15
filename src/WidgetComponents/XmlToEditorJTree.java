package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ActionListenersImpl.RemoveEditorTabActionListener;
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
		MENU_ITEM_REMOVE_TEXT = "remove";
	private static int
		SCROLL_UNIT_INC = 15;
	
	private RedrawableFrame 
		editorFrame;
	private JTree 
		tree;
	private JPanel
		treePanel,
		viewPanel;
	private JScrollPane
		treeJs,
		js;
	private DefaultMutableTreeNode 
		top;
	private LinkedHashMap<String, DefaultMutableTreeNode>
		refIdAndTreeNode = new LinkedHashMap<String, DefaultMutableTreeNode>();
	private LinkedHashMap<DefaultMutableTreeNode, ArrayList<DefaultMutableTreeNode>> 
		parentChildTreeNodes = new LinkedHashMap<DefaultMutableTreeNode, ArrayList<DefaultMutableTreeNode>>();
	
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
		destroyPanel();
		buildEditors();
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
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties() != null)
		{
			String topName = PathUtility.filterPathToFilename(WidgetBuildController.getInstance().getFilename());
			top = new DefaultMutableTreeNode(topName);
			
			for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
			{
				WidgetPropertiesPanelArray pArray = new WidgetPropertiesPanelArray(); 
				
				pArray.buildPropertiesArray(wcp);
				pArray.setRedrawableFrame(editorFrame);
				buildTreeNode(wcp, pArray);
			}
			connectNodes(parentChildTreeNodes);
			
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
		JPopupMenu pm = new JPopupMenu();
		JMenuItem mi = new JMenuItem(MENU_ITEM_REMOVE_TEXT);
		mi.addActionListener(new RemoveEditorTabActionListener(this));
		pm.setEnabled(true);
		pm.add(mi);
		tree.setComponentPopupMenu(pm);
		expandPaths();
		
		treePanel.add(tree, BorderLayout.CENTER);
		this.add(treeJs, BorderLayout.WEST);
		this.add(viewPanel, BorderLayout.CENTER);
		
		editorFrame.add(this, BorderLayout.CENTER);
		editorFrame.validate();
	}
	
	private DefaultMutableTreeNode buildTreeNode(WidgetCreatorProperty wcp, WidgetPropertiesPanelArray pArray)
	{
		DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(pArray);
		refIdAndTreeNode.put(wcp.getRefWithID(), dmtn);
		if(wcp.getParentRefWithID() != null && !wcp.getParentRefWithID().isBlank())
		{
			DefaultMutableTreeNode parentNode = refIdAndTreeNode.get(wcp.getParentRefWithID());
			ArrayList<DefaultMutableTreeNode> nodes = parentChildTreeNodes.get(parentNode);
			if(nodes == null)
			{
				nodes = new ArrayList<DefaultMutableTreeNode>();
			}
			nodes.add(dmtn);
			parentChildTreeNodes.put(parentNode, nodes);
		}
		else
		{
			ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
			nodes.add(dmtn);
			parentChildTreeNodes.put(top, nodes);
		}
		return dmtn;
	}
	
	private void connectNodes(LinkedHashMap<DefaultMutableTreeNode, ArrayList<DefaultMutableTreeNode>> parentAndChildNodes)
	{
		for(DefaultMutableTreeNode dmtn : parentAndChildNodes.keySet())
		{
			ArrayList<DefaultMutableTreeNode> nodes = parentAndChildNodes.get(dmtn);
			for(DefaultMutableTreeNode n : nodes)
			{
				dmtn.add(n);
			}
		}
	}
	
	private void expandPaths()
	{
		for(DefaultMutableTreeNode node : parentChildTreeNodes.keySet())
		{
			TreePath tp = new TreePath(node.getPath());
			tree.expandPath(tp);
		}
	}
	
	private void setScrollableComponent(Component c)
	{
		JPanel outI = new JPanel();
		outI.setLayout(new BorderLayout());
		outI.add(c, BorderLayout.NORTH);
		js = new JScrollPane(outI);
		js.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
	}
	
//	private void setSelectedIndex(int selIndex)
//	{
//		tree.setSelectionRow(selIndex);
//	}

	@Override
	public int getSelectedIndex() 
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		node.getUserObject();
		if(node.getUserObject() instanceof WidgetPropertiesPanelArray)
		{
			WidgetPropertiesPanelArray pArray = (WidgetPropertiesPanelArray) node.getUserObject();
			WidgetCreatorProperty wcp = pArray.getWidgetCreatorProperty();
			return WidgetBuildController.getInstance().getWidgetCreatorProperties().indexOf(wcp);
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
