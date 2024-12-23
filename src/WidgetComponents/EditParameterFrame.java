package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EditParameterFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String TITLE_NAME_PREFIX = "Add method: ";
	private static final Dimension 	
		WINDOW_LOCATION = new Dimension(350, 150),
		WINDOW_SIZE = new Dimension(650, 550);
	
	public EditParameterFrame(String methodName)
	{
		setTitle(TITLE_NAME_PREFIX + methodName);
		setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		setVisible(true);
	}
	
	public void addComponent(Component comp)
	{
		this.add(comp);
	}
	
	public void addSaveAndCancelButtons()
	{
		JButton saveButton, cancelButton;
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EditParameterFrame.this.setVisible(false);
				EditParameterFrame.this.dispose();
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EditParameterFrame.this.setVisible(false);
				EditParameterFrame.this.dispose();
			}
		});
		JPanel bottomButtonsPanel = new JPanel();
		bottomButtonsPanel.setLayout(new GridLayout(0,2));
		bottomButtonsPanel.add(saveButton);
		bottomButtonsPanel.add(cancelButton);
		this.add(bottomButtonsPanel, BorderLayout.SOUTH);
	}
}
