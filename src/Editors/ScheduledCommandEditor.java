package Editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import Actions.ScheduledCommand;
import Graphics2D.GraphicsUtil;
import Params.ParameterEditor;
import WidgetComponents.ScheduledCommandExecutor;

public class ScheduledCommandEditor extends JButton implements ParameterEditor
{
	
	private static final long serialVersionUID = 1L;
	
	private ScheduledCommand sc = null;
	
	public ScheduledCommandEditor()
	{
		if(sc == null || sc.getCommandBuild() == null)
		{
			this.setText("Edit Command");
		}
		else
		{
			this.setText(sc.getCommandBuild().getCommandXmlString());
		}
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				ScheduledCommandExecutor sce = new ScheduledCommandExecutor();//TODO.
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setMinimumSize(new Dimension(400, 550));
				f.setLayout(new BorderLayout());
				GraphicsUtil.rightEdgeTopWindow(ScheduledCommandEditor.this, f);
				f.setLocation(new Point(300,300));
				f.setVisible(true);
				f.add(sce, BorderLayout.NORTH);
				sce.setLayout(new GridLayout(0,1));
				sce.postExecute();
			}
		});
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		if(value instanceof String)
			return;
		this.sc = (ScheduledCommand) value;
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {this.sc.toString()};//TODO.
	}

	@Override
	public Object getComponentValueObj() 
	{
		return sc;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return ScheduledCommand.class.getName();
	}
	
}
