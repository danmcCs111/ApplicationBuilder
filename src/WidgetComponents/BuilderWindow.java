package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ActionListeners.OpenDetailsActionListener;
import ClassDefinitions.ClassAndSetters;
import Params.XmlToWidgetGenerator;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetCreatorProperty;

public class BuilderWindow extends RedrawableFrame 
{
	private static final long serialVersionUID = 1886L;
	private static final String 
		TITLE = "Add Property: ",
		ACTION_BUTTON_TEXT = "Add Property";
	private static final Dimension 
		WINDOW_LOCATION = new Dimension(100, 50),
		WINDOW_SIZE = new Dimension(480, 640);
	
	private JScrollPane scrPane = null;
	private JPanel innerPanel2 = new JPanel();
	private JList<String> componentMethods = null;
	private JButton addMethodParamAction;
	private ArrayList<String> methods = new ArrayList<String>(); 
	private OpenDetailsActionListener openDetailsActionListener;
	private WidgetCreatorProperty wcp;
	
	public BuilderWindow(RedrawableFrame rParentFrame, WidgetCreatorProperty wcp)
	{
		setTitle(TITLE);
		setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		ArrayList<ClassAndSetters> classAndSetters = WidgetAttributes.getClassAndSetters();
		
		for(ClassAndSetters cs : classAndSetters)//TODO garbage
		{
			if(cs.getClazz().equals(wcp.getInstance().getClass()))
			{
				methods = cs.getSupportedSetters();
				break;
			}
		}
		
		componentMethods = new JList<String>();
		Collections.sort(methods);//Sort list...
		componentMethods.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting())
				{
					ListSelectionModel lsm = componentMethods.getSelectionModel();
					addMethodParamAction.setEnabled(!lsm.isSelectionEmpty());
				}
			}
		});
		BorderLayout bl = new BorderLayout();
		scrPane = new JScrollPane(componentMethods);
		BorderLayout bl2 = new BorderLayout();
		innerPanel2.setLayout(bl2);
		innerPanel2.add(scrPane, BorderLayout.CENTER);
		this.setLayout(bl);
		this.add(innerPanel2, BorderLayout.CENTER);
		
		//setup add Property button
		addMethodParamAction = new JButton(ACTION_BUTTON_TEXT);
		this.add(addMethodParamAction, BorderLayout.SOUTH);
		openDetailsActionListener = new OpenDetailsActionListener(rParentFrame, BuilderWindow.this);
		addMethodParamAction.addActionListener(openDetailsActionListener);
		openDetailsActionListener.setComponentMethods(componentMethods, wcp);
		addMethodParamAction.setEnabled(false);
		
		setWidgetCreatorProperty(wcp);
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
	}
	
	public void setComboSelection(String compSelect)
	{
		this.setTitle(TITLE + compSelect);
	}
	
	public void setWidgetCreatorProperty(WidgetCreatorProperty wcp)
	{
		this.wcp = wcp;
		refreshComponentMethods(wcp);
		openDetailsActionListener.setComponentMethods(componentMethods, wcp);
	}
	
	public String getComponentSelected()
	{
		return componentMethods.getSelectedValue().toString();
	}
	
	public void refreshComponentMethods(WidgetCreatorProperty wcp)
	{
		ArrayList<String> objs = new ArrayList<String>();
		
		nextSel:
		for(String s : methods)
		{
			for(XmlToWidgetGenerator xwg : wcp.getXmlToWidgetGenerators())
			{
				if(s.contains(xwg.getMethodName()))
				{
					continue nextSel;
				}
			}
			objs.add(s);
		}
		componentMethods.removeAll();
		componentMethods.setListData(objs.toArray(new String[objs.size()]));
	}
	
	@Override
	public void clearInnerPanels()
	{
		innerPanel2.removeAll();
		scrPane.removeAll();
		this.remove(innerPanel2);
	}
	
	@Override
	public void rebuildInnerPanels() 
	{
		setWidgetCreatorProperty(wcp);
		
		scrPane = new JScrollPane(componentMethods);
		innerPanel2.add(scrPane, BorderLayout.CENTER);innerPanel2.setOpaque(rootPaneCheckingEnabled);
		BuilderWindow.this.add(innerPanel2, BorderLayout.CENTER);
		
		BuilderWindow.this.validate();
	}
	
}
