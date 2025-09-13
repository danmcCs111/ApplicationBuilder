package Editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import EditorAbstract.PathArrayChangeListener;
import EditorAbstract.PathArrayProcessingFrame;
import EditorAbstract.PostProcess;
import ObjectTypeConversion.PathArrayProcessing;
import Params.ParameterEditor;
import Properties.LoggingMessages;

public class PathArrayProcessingEditor extends JButton implements ParameterEditor, PostProcess, PathArrayChangeListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select Directories";
	protected JFileChooser jcc;

	private PathArrayProcessing pa;
	
	public PathArrayProcessingEditor() 
	{
		
	}
	
	public void buildWidgets()
	{
		this.setText(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PathArrayProcessingFrame papf = new PathArrayProcessingFrame(
						PathArrayProcessingEditor.this, PathArrayProcessingEditor.this);
				papf.setOpen((PathArrayProcessing)getComponentValueObj());
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
		PathArrayProcessing paTmp = (PathArrayProcessing) value;
		pa = paTmp;
		
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {pa.toString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return pa;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return PathArrayProcessing.class.getName();
	}

	@Override
	public void postWidgetGenerationProcessing() 
	{
		buildWidgets();
	}

	@Override
	public void setPathArrayProcessingValue(PathArrayProcessing pap) 
	{
		setComponentValue(pap);
		LoggingMessages.printOut(pa.toString());
	}

}
