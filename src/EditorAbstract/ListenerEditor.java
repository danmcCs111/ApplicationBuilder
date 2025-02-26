package EditorAbstract;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Params.ParameterEditor;

public abstract class ListenerEditor extends JPanel implements ParameterEditor
{

	private static final long serialVersionUID = 1990L;

	protected JComboBox<String> comboSelection;
	protected JButton editButton;
	
	public ListenerEditor()
	{
		comboSelection = new JComboBox<String>(
				ParameterEditor.loadClassExtensionsAsString(
				getEditorDirectory(), 
				getFileExtension(), 
				getPackagePrefix(), 
				getFileFilter()).toArray(new String[] {})
		);
		editButton = new JButton();
		editButton.setToolTipText("ToDo.");
		editButton.setText(getEditButtonText());
		this.setLayout(new BorderLayout());
		this.add(comboSelection, BorderLayout.CENTER);
		this.add(editButton,BorderLayout.EAST);
	}
	
	public abstract String getEditorDirectory();
	public abstract String getPackagePrefix();
	public abstract String getFileExtension();
	public abstract String getFileFilter();
	public abstract String getEditButtonText();
	
	public abstract void setEditorDirectory(String editorDirectory);
	public abstract void setPackagePrefix(String packagePrefix);
	public abstract void setFileExtension(String fileExtension);
	public abstract void setFileFilter(String fileFilter);
	public abstract void setEditButtonText(String editButtonText);
	
	@Override
	public void setComponentValue(Object value) 
	{
		if(value != null)
			comboSelection.setSelectedItem(value.getClass().getName());//using class name
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
			? new String [] {""}
			: new String [] {comboSelection.getSelectedItem().toString()};
	}

	@Override
	public String getComponentXMLOutput() 
	{
		return null;
	}

	@Override
	public Object getComponentValueObj() 
	{
		return comboSelection.getSelectedItem();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
