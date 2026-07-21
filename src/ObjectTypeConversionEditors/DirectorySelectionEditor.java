package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.ValueChangedListener;
import Params.ParameterEditor;
import Properties.PathUtility;

public class DirectorySelectionEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 2002L;

	private static final String
		NULL_VALUE_TEXT = "<Select Directory>",
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select Directory",
		END_DIRECTORY_SUFFIX = "/ ";
	protected JFileChooser 
		jcc;
	private ArrayList<ValueChangedListener> 
		vcls;
	private boolean 
		isRelativePath = true;
	private DirectorySelection
		directorySelection = null;

	public DirectorySelectionEditor()
	{
		buildWidgets();
	}
	
	public void addValueChangedListener(ValueChangedListener vcl)
	{
		if(vcls == null)
		{
			vcls = new ArrayList<ValueChangedListener>();
		}
		vcls.add(vcl);
	}
	
	public void setIsRelativePath(boolean relativePath)
	{
		this.isRelativePath = relativePath; 
	}
	
	private void buildWidgets()
	{
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.DIRECTORIES_ONLY);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					if(isRelativePath)
					{
						String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
						
						DirectorySelection ds = new DirectorySelection(
								replPath.replaceAll(PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), "") + 
								END_DIRECTORY_SUFFIX);
						setComponentValue(ds);
					}
					else
					{
						DirectorySelection ds = new DirectorySelection(chosenFile.getAbsolutePath(), false);
						setComponentValue(ds);
					}
				}
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
		
		if(value == null)
		{
			directorySelection = null;
			this.setText(NULL_VALUE_TEXT);
			jcc.setSelectedFile(new File(PathUtility.getCurrentDirectory()));
		}
		else
		{
			directorySelection = (DirectorySelection) value;
			this.setText(
				(isRelativePath)
				? directorySelection.getRelativePath()
				: directorySelection.getFullPath()
			);
			jcc.setSelectedFile(new File(directorySelection.getFullPath()));
		}
		if(vcls != null)
		{
			for(ValueChangedListener vcl : vcls)
			{
				vcl.valueChanged(directorySelection);
			}
		}
	}

	@Override
	public String[] getComponentValue() 
	{
		if(directorySelection != null)
		{
			return new String[] {DirectorySelectionEditor.this.getText()};
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getComponentValueObj() 
	{
		return directorySelection;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return DirectorySelection.class.getName();
	}

}
