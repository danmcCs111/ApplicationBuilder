package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ApplicationLayoutEditor extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String TITLE = "Application Layout Editor";
	private static final Dimension WINDOW_LOCATION = new Dimension(750, 250);
	private static final Dimension WINDOW_SIZE = new Dimension(480, 640);
	
	JButton openParameterButton;
	
	public ApplicationLayoutEditor()
	{
		this.setTitle(TITLE);
		this.setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		buildOpenParmeterEditorButton();
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void buildOpenParmeterEditorButton()
	{
		//setup add Property button
		openParameterButton = new JButton("Add Widget");
		openParameterButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BuilderWindow bw = new BuilderWindow();
				bw.setVisible(true);
			}
		});
		this.add(openParameterButton, BorderLayout.SOUTH);
	}
	
}
