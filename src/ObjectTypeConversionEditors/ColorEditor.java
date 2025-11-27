package ObjectTypeConversionEditors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import Graphics2D.GraphicsUtil;
import Params.ParameterEditor;

public class ColorEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 1992L;
	
	public static final String 
		SAVE_BUTTON_TEXT = "Save",
		CANCEL_BUTTON_TEXT = "Cancel";
	
	private String titleText = "";
	private JColorChooser jcc = null;
	private JDialog d;
	
	public ColorEditor()
	{
		super();
		jcc = new JColorChooser();
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(d == null)
				{
					d = new JDialog();
					GraphicsUtil.centerWindow(ColorEditor.this.getRootPane().getParent(), d);
					d.setTitle(titleText);
					d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					d.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							jcc.setColor(ColorEditor.this.getForeground());
							d.dispose();
						}
					});
					d.setLayout(new BorderLayout());
					d.add(jcc, BorderLayout.CENTER);
					JButton save = new JButton(SAVE_BUTTON_TEXT);
					save.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							ColorEditor.this.setForeground((Color)jcc.getColor());
							d.dispose();
						}
					});
					JButton cancel = new JButton(CANCEL_BUTTON_TEXT);
					cancel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							jcc.setColor(ColorEditor.this.getForeground());
							d.dispose();
						}
					});
					JPanel southPane = new JPanel(new BorderLayout());
					JPanel eastPane = new JPanel(new GridLayout(1,2));
					eastPane.add(save);
					eastPane.add(cancel);
					southPane.add(eastPane, BorderLayout.EAST);
					d.add(southPane, BorderLayout.SOUTH);
				}
				d.setVisible(true);
				d.pack();
			}
		});
	}
	
	public void setTitleText(String titleText)
	{
		this.titleText = titleText;
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public String getComponentXMLOutput()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return "java.awt.color";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		this.setForeground((Color)value);
		this.setText("[-Color Select-]");
		jcc.setColor((Color)value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return jcc == null
			? new String [] {""}
			: new String [] {jcc.getColor().getRed()+"", 
				jcc.getColor().getGreen()+"",
				jcc.getColor().getBlue()+""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return jcc.getColor();
	}

	@Override
	public void destroy() 
	{
		if(d != null)d.dispose();
	}
}
