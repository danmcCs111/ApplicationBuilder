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
import EditorAbstract.EditorState;
import EditorAbstract.EditorStateChangedDistributor;
import Graphics2D.GraphicsUtil;
import Params.ParameterEditor;
import WidgetComponents.ScheduledCommandExecutionEditor;

public class ScheduledCommandEditor extends JButton implements ParameterEditor, EditorState
{
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_TEXT = "Edit Command";
	
	private ScheduledCommand sc = null;
	private EditorStateChangedDistributor editorStateChangedDistributor = new EditorStateChangedDistributor(this);
	
	public ScheduledCommandEditor()
	{
		if(sc == null || sc.getCommandBuild() == null)
		{
			this.setText(DEFAULT_TEXT);
		}
		else
		{
			this.setText(sc.getCommandBuild().getCommandXmlString());
		}
		this.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JFrame f = new JFrame(ScheduledCommandEditor.this.getText());
				ScheduledCommandExecutionEditor sce = new ScheduledCommandExecutionEditor();//TODO.
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
				f.addWindowListener(new WindowAdapter() 
				{
					@Override
					public void windowClosed(WindowEvent e) 
					{
						setComponentValue(sce.getScheduledCommand());
						editorStateChangedDistributor.notifyEditorChangeListener();
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
		if(sc != null && sc.getCommandBuild() != null) this.setText(sc.getCommandBuild().getCommandXmlString());
		else this.setText(DEFAULT_TEXT);
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

	@Override
	public EditorStateChangedDistributor getEditorStateChangedDistributor() 
	{
		return editorStateChangedDistributor;
	}
	
}
