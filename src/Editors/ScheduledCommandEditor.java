package Editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
				JFrame f = new JFrame(ScheduledCommandEditor.this.getText());
				ScheduledCommandExecutor sce = new ScheduledCommandExecutor();//TODO.
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				f.setMinimumSize(new Dimension(400, 550));
				f.setLayout(new BorderLayout());
				f.setVisible(true);
				f.add(sce, BorderLayout.NORTH);
				sce.setLayout(new GridLayout(0,1));
				sce.postExecute();
				if(sc != null)
				{
					sce.setScheduledCommand(sc);
				}
				GraphicsUtil.rightEdgeTopWindow(ScheduledCommandEditor.this.getRootPane().getParent(), f);
				f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						setComponentValue(sce.getScheduledCommand());
					}
				});
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
		this.setText(sc.getCommandBuild().getCommandXmlString());
	}

	@Override
	public String[] getComponentValue() 
	{
		return (this.sc == null)
				? null
				: new String[] {this.sc.toString()};//TODO.
	}

	@Override
	public Object getComponentValueObj() 
	{
		return sc;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		return sc.getXmlAttributesString();
	}

	@Override
	public String getParameterDefintionString() 
	{
		return ScheduledCommand.class.getName();
	}
	
}
