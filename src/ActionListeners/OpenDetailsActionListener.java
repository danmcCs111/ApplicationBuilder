package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import ApplicationBuilder.LoggingMessages;
import ApplicationBuilder.RedrawableFrame;
import Params.XmlToWidgetGenerator;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetCreatorProperty;

public class OpenDetailsActionListener implements ActionListener
{
	private JList<?> componentMethods;
	private WidgetCreatorProperty wcp;
	private RedrawableFrame 
		rFrameParent,
		rFrame;
	
	public OpenDetailsActionListener(RedrawableFrame rFrameParent, RedrawableFrame rFrame)
	{
		this.rFrameParent = rFrameParent;
		this.rFrame = rFrame;
	}
	
	public void setComponentMethods(JList<?> componentMethods, WidgetCreatorProperty wcp)
	{
		this.componentMethods = componentMethods;
		this.wcp = wcp;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		ListSelectionModel lsm = componentMethods.getSelectionModel();
		if(!lsm.isSelectionEmpty())
		{
			for(Object o : componentMethods.getSelectedValuesList())
			{
				LoggingMessages.printOut(o.toString());
				XmlToWidgetGenerator xmlG = WidgetAttributes.setAttribute(wcp.getClassType(), o+"");
				wcp.addXmlToWidgetGenerator(xmlG);
				
				rFrameParent.clearInnerPanels();
				rFrameParent.rebuildInnerPanels();
				rFrame.clearInnerPanels();
				rFrame.rebuildInnerPanels();
				LoggingMessages.printOut(xmlG.toString());
			}
			
		}
	}

}
