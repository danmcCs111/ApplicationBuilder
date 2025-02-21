package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class ComboSelectionDialog extends JDialog 
{
	private static final long serialVersionUID = 2001L;
	public static final String 
		ADD_BUTTON_DEFAULT_TEXT = "Add",
		ADD_ALL_BUTTON_DEFAULT_TEXT = "Add All",
		CLOSE_BUTTON_DEFAULT_TEXT = "Close";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(300,150);
	
	private JList<String> componentMethods = new JList<String>();
	
	public void buildAndShow(List<String> selectables,
			String dialogTitle,
			String dialogMessage,
			ComboListDialogSelectedListener cdsl,
			DialogParentReferenceContainer refLocContainer)
	{
		buildAndShow
		(
				selectables,
				dialogTitle, dialogMessage,
				ADD_BUTTON_DEFAULT_TEXT, ADD_ALL_BUTTON_DEFAULT_TEXT, CLOSE_BUTTON_DEFAULT_TEXT,
				cdsl,refLocContainer
		);
	}
	
	public void buildAndShow(List<String> selectables,
			String dialogTitle,
			String dialogMessage,
			String addButtonText,
			String addAllButtonText,
			String closeButtonText,
			ComboListDialogSelectedListener cdsl,
			DialogParentReferenceContainer refLocContainer)
	{
	
		JLabel messageLabel = new JLabel();
		componentMethods.setListData(selectables.toArray(new String[selectables.size()]));
		
		messageLabel.setText(dialogMessage);
		this.setTitle(dialogTitle);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.setLocation((int)refLocContainer.getContainerCenterLocationPoint().x - (MIN_DIMENSION_DIALOG.width/2), 
				(int)refLocContainer.getContainerCenterLocationPoint().y - (MIN_DIMENSION_DIALOG.height /2));
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		
		WindowListener wl = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				cdsl.selectionChosen(null);//cancel
				ComboSelectionDialog.this.dispose();
			}
		};
		this.addWindowListener(wl);
		this.setLayout(new BorderLayout());
		this.add(messageLabel, BorderLayout.NORTH);
		this.add(componentMethods, BorderLayout.CENTER);
		
		JPanel southPane = new JPanel(new BorderLayout());
		JPanel eastPane = new JPanel(new GridLayout(1,2));
		
		if(addButtonText != null)
		{
			JButton save = new JButton(addButtonText);
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<String> selected = componentMethods.getSelectedValuesList();
					if(selected != null && !selected.isEmpty())
					{
						//save only selected
						cdsl.selectionChosen(selected);
						ComboSelectionDialog.this.removeWindowListener(wl);
						ComboSelectionDialog.this.dispose();
					}
				}
			});
			eastPane.add(save);
		}
		
		if(addAllButtonText != null)
		{
			JButton saveAll = new JButton(addAllButtonText);
			saveAll.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//save all in list
					cdsl.selectionChosen(selectables);
					ComboSelectionDialog.this.removeWindowListener(wl);
					ComboSelectionDialog.this.dispose();
				}
			});
			eastPane.add(saveAll);
		}
		
		if(closeButtonText != null)
		{
			JButton cancel = new JButton(closeButtonText);
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//cancel
					cdsl.selectionChosen(null);
					ComboSelectionDialog.this.removeWindowListener(wl);
					ComboSelectionDialog.this.dispose();
				}
			});
			eastPane.add(cancel);
		}
		
		southPane.add(eastPane, BorderLayout.EAST);
		this.add(southPane, BorderLayout.SOUTH);
		this.setVisible(true);
		this.pack();
	}
	
}
