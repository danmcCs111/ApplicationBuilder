package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import Graphics2D.GraphicsUtil;
import WidgetExtensions.TextDialogSelectedListener;

public class TextDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	public static final String 
		ADD_BUTTON_DEFAULT_TEXT = "Add",
		CLOSE_BUTTON_DEFAULT_TEXT = "Close";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(300,150);
	private static final int CHARACTER_LEN = 20;
	
	private JTextField characterEntry;
	
	public TextDialog()
	{
		
	}
	
	public void buildAndShow(
			String dialogTitle,
			String dialogMessage,
			TextDialogSelectedListener tdsl,
			Container refLocContainer)
	{
		buildAndShow(dialogTitle, dialogMessage, ADD_BUTTON_DEFAULT_TEXT, CLOSE_BUTTON_DEFAULT_TEXT, tdsl, refLocContainer);
	}
	
	public void buildAndShow(
			String dialogTitle,
			String dialogMessage,
			String addButtonText,
			String closeButtonText,
			TextDialogSelectedListener tdsl,
			Container refLocContainer)
	{
	
		JLabel messageLabel = new JLabel();
		characterEntry = new JTextField();
		characterEntry.setColumns(CHARACTER_LEN);
		
		messageLabel.setText(dialogMessage);
		this.setTitle(dialogTitle);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		GraphicsUtil.rightEdgeTopWindow(refLocContainer, this);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		
		WindowListener wl = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				tdsl.textEntered(null);
				TextDialog.this.dispose();
			}
		};
		this.addWindowListener(wl);
		this.setLayout(new BorderLayout());
		this.add(messageLabel, BorderLayout.NORTH);
		this.add(characterEntry, BorderLayout.CENTER);
		
		JPanel southPane = new JPanel(new BorderLayout());
		JPanel eastPane = new JPanel(new GridLayout(1,2));
		
		if(addButtonText != null)
		{
			JButton save = new JButton(addButtonText);
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tdsl.textEntered(characterEntry.getText());
					TextDialog.this.removeWindowListener(wl);
					TextDialog.this.dispose();
				}
			});
			eastPane.add(save);
		}
		
		if(closeButtonText != null)
		{
			JButton cancel = new JButton(closeButtonText);
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tdsl.textEntered(null);
					TextDialog.this.removeWindowListener(wl);
					TextDialog.this.dispose();
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
