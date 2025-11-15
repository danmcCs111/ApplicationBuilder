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
import javax.swing.JPanel;

import Actions.ScheduledCommand;
import EditorAbstract.EditorState;
import EditorAbstract.EditorStateChangedDistributor;
import Graphics2D.ColorTemplate;
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
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		if(sc == null || sc.getCommandBuild() == null)
		{
			this.setText(DEFAULT_TEXT);
		}
		else
		{
			this.setText(sc.getCommandBuild().getCommandXmlString());
		}
		GraphicsUtil.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		GraphicsUtil.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		this.addActionListener(new ActionListener() 
		{
			JFrame f = null;
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(f == null)//TODO
				{
					f = new JFrame(ScheduledCommandEditor.this.getText());
					JPanel innerPanel = new JPanel();
					innerPanel.setLayout(new BorderLayout());
					ScheduledCommandExecutionEditor sce = new ScheduledCommandExecutionEditor();//TODO.
					f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					f.setMinimumSize(new Dimension(400, 550));
					f.setLayout(new BorderLayout());
					GraphicsUtil.rightEdgeTopWindow(ScheduledCommandEditor.this.getRootPane().getParent(), f);
					innerPanel.add(sce, BorderLayout.NORTH);
					f.add(innerPanel, BorderLayout.CENTER);
					f.setVisible(true);
					
					GraphicsUtil.setBackgroundColorPanel(f, ColorTemplate.getPanelBackgroundColor());
					
					sce.setLayout(new GridLayout(0,1));
					sce.postExecute();
					if(sc != null)
					{
						sce.setScheduledCommand(sc);
					}
					f.addWindowListener(new WindowAdapter() 
					{
						@Override
						public void windowClosed(WindowEvent e) 
						{
							setComponentValue(sce.getScheduledCommand());
							editorStateChangedDistributor.notifyEditorChangeListener();
							f = null;
						}
					});
					
				}
			}
		});
		GraphicsUtil.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
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
