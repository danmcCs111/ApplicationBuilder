package EditorInterfaces;

import java.awt.Container;
import java.util.List;

import javax.swing.JDialog;

import WidgetExtensionInterfaces.ComboListDialogSelectedListener;

public class PathPickerResolver extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	public void buildAndShow(List<String> selectables,
			String dialogTitle,
			String dialogMessage,
			String addButtonText,
			String addAllButtonText,
			String closeButtonText,
			ComboListDialogSelectedListener cdsl,
			Container refLocContainer)
	{
		//to unix
	}
}
