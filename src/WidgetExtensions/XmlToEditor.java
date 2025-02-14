package WidgetExtensions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import ActionListeners.RemoveEditorTabActionListener;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.TabbedPanel;
import Params.ParameterEditor;
import Params.XmlToWidgetGenerator;
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
				JPanel outP = new JPanel(new BorderLayout());
				JPanel p = new JPanel(new GridLayout(0,1));//rows, columns
				JScrollPane js = new JScrollPane(p);
				js.getVerticalScrollBar().setUnitIncrement(15);
				
				String compName = wcp.getRef() + COMPONENT_SUFFIX + count;
				for(XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
				{
					String metName = xwg.getMethodName();
					ArrayList<Object> convObjs = xwg.getParamEditorObjects();
					Label l = new Label();
					Component [] cs = new Component [xwg.getParameterEditors().size()];
					JPanel pi = new JPanel(new BorderLayout());
					JButton del = new JButton();
					
					l.setText(metName);
					l.setForeground(Color.BLUE);
					del.setText(DELETE_BUTTON_TEXT);
					pi.add(del, BorderLayout.WEST);
					pi.add(l, BorderLayout.CENTER);
					del.setForeground(Color.RED);
					del.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							for(Component c : cs) if(c != null) p.remove(c);
							pi.remove(l);
							pi.remove(del);
							p.repaint();
							editorFrame.validate();
							wcp.getXmlToWidgetGenerators().remove(xwg);//remove from generators for saving removal
							
							editorFrame.updateDependentWindow();
						}
					});
					
					p.add(pi);
					
					for(int i = 0; i < xwg.getParameterEditors().size(); i++)
					{
						ParameterEditor pe = xwg.getParameterEditors().get(i);
						if(pe == null)
							continue;
						
						cs[i] = pe.getComponentEditor();
						pe.setComponentValue(convObjs.get(i));
						p.add(cs[i]);
					}
				}
				outP.add(js, BorderLayout.CENTER);
				this.add(outP);
				this.setTitleAt(count++, compName);
			}
		}
		editorFrame.add(this, BorderLayout.CENTER);
		editorFrame.validate();
	}

}
