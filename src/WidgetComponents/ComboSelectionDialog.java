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
	private static final String 
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Save Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Which to Save: ",
		ADD_BUTTON_TEXT = "Add",
		ADD_ALL_BUTTON_TEXT = "Add All",
		CLOSE_BUTTON_TEXT = "Close";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(300,150);
	
	private JList<String> componentMethods = new JList<String>();
	
	public void buildAndShow(List<String> selectables, ComboListDialogSelectedListener cdsl, DialogParentReferenceContainer refLocContainer)
	{
		JLabel messageLabel = new JLabel();
		componentMethods.setListData(selectables.toArray(new String[selectables.size()]));
		
		messageLabel.setText(DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE);
		this.setTitle(DIALOG_SELECT_CHILD_COMPONENTS_TITLE);
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
		
		JButton save = new JButton(ADD_BUTTON_TEXT);
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
		JButton saveAll = new JButton(ADD_ALL_BUTTON_TEXT);
		saveAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//save all in list
				cdsl.selectionChosen(selectables);
				ComboSelectionDialog.this.removeWindowListener(wl);
				ComboSelectionDialog.this.dispose();
			}
		});
		JButton cancel = new JButton(CLOSE_BUTTON_TEXT);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//cancel
				cdsl.selectionChosen(null);
				ComboSelectionDialog.this.removeWindowListener(wl);
				ComboSelectionDialog.this.dispose();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(messageLabel, BorderLayout.NORTH);
		this.add(componentMethods, BorderLayout.CENTER);
		JPanel southPane = new JPanel(new BorderLayout());
		JPanel eastPane = new JPanel(new GridLayout(1,2));
		eastPane.add(save);
		eastPane.add(saveAll);
		eastPane.add(cancel);
		southPane.add(eastPane, BorderLayout.EAST);
		this.add(southPane, BorderLayout.SOUTH);
		this.setVisible(true);
		this.pack();
	}
	
}
