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
import javax.swing.JPanel;

import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import Params.ParameterEditor;
import Params.XmlToWidgetGenerator;
import WidgetUtility.WidgetCreatorProperty;

public class WidgetPropertiesPanelArray extends JPanel implements DependentRedrawableFrameListener
{
	public static final String 
		MENU_ITEM_REMOVE_TEXT = "remove",
		COMPONENT_SUFFIX = "@",
		COMPONENT_REGEX = COMPONENT_SUFFIX + "[0-9]*",
		DELETE_BUTTON_TEXT = "X";
	private static final long serialVersionUID = 1891L;
	
	private DependentRedrawableFrame dependentRedrawableFrame;
	
	public WidgetPropertiesPanelArray()
	{
		super();
		this.setLayout(new GridLayout(0,1));
	}
	
	public void buildPropertiesArray(WidgetCreatorProperty wcp)
	{
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
					dependentRedrawableFrame.validate();
					wcp.getXmlToWidgetGenerators().remove(xwg);//remove from generators for saving removal
					
					dependentRedrawableFrame.updateDependentWindow();
				}
			});
			
			this.add(pi);
			
			for(int i = 0; i < xwg.getParameterEditors().size(); i++)
			{
				ParameterEditor pe = xwg.getParameterEditors().get(i);
				cs[i] = pe.getComponentEditor();
				pe.setComponentValue(convObjs.get(i));
				this.add(cs[i]);
			}
		}
	}

	@Override
	public void setDependentRedrawableFrame(DependentRedrawableFrame dependentRedrawableFrame) 
	{
		this.dependentRedrawableFrame = dependentRedrawableFrame;
	}
}
