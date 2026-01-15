package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import Params.ParameterEditor;
import Params.ParameterUtility;
import Params.XmlToWidgetGenerator;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetUtility.WidgetCreatorProperty;

public class WidgetPropertiesPanelArray extends JPanel implements RedrawableFrameListener
{
	public static final String 
		MENU_ITEM_REMOVE_TEXT = "remove",
		TITLE_TEXT_SEPERATOR = " <-> ",
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	private static final long serialVersionUID = 1891L;
	
	private RedrawableFrame redrawableFrame;
	private WidgetCreatorProperty wcp;
	
	public WidgetPropertiesPanelArray()
	{
		super();
		this.setLayout(new GridLayout(0,1));
	}
	
	public void buildLayoutPropertiesArray(ActionListener al)
	{
		//TODO
	}
	
	public WidgetCreatorProperty getWidgetCreatorProperty()
	{
		return this.wcp;
	}
	
	public void buildPropertiesArray(WidgetCreatorProperty wcp)
	{
		this.wcp = wcp;
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
					for(Component c : cs) if(c != null) WidgetPropertiesPanelArray.this.remove(c);
					pi.remove(l);
					pi.remove(del);
					WidgetPropertiesPanelArray.this.repaint();
					wcp.getXmlToWidgetGenerators().remove(xwg);//remove from generators for saving removal
				}
			});
			
			this.add(pi);
			
			for(int i = 0; i < xwg.getParameterEditors().size(); i++)
			{
				String titleText = wcp.getRefWithID() + TITLE_TEXT_SEPERATOR + metName;
				ParameterEditor pe = xwg.getParameterEditors().get(i);
				cs[i] = pe.getComponentEditor();
				ParameterUtility.setTitleText(pe, titleText);
				pe.setComponentValue(convObjs.get(i));
				this.add(cs[i]);
			}
		}
	}

	@Override
	public void setRedrawableFrame(RedrawableFrame RedrawableFrame) 
	{
		this.redrawableFrame = RedrawableFrame;
	}
	
	@Override
	public String toString()
	{
		return this.wcp.getRefWithID();
	}
}
