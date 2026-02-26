package MouseListenersImpl;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversionEditors.TimestampEditor;

public class VideoUpdateTimespanDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final String
		TIMESTAMP_LABEL = "Update Begin Date : ",
		RUN_BUTTON_TEXT = "Run",
		CANCEL_BUTTON_TEXT = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(350, 125);
	
	private AbstractButton
		ab;
	private LookupOrCreateYoutube
		lcy;
	
	public VideoUpdateTimespanDialog(Container refContainer, AbstractButton ab, LookupOrCreateYoutube lcy, Date d)
	{
		this.ab = ab;
		this.lcy = lcy;
		
		buildWidgets(d);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(MIN_DIMENSION_DIALOG);
		
		GraphicsUtil.rightEdgeCenterWindow(refContainer, this);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		
		this.setVisible(true);
	}
	
	private void buildWidgets(Date d)
	{
		this.setLayout(new BorderLayout());
		
		JPanel
			runCancelPanel = new JPanel(),
			controlPanel = new JPanel();
		JLabel
			timestampLabel = new JLabel(TIMESTAMP_LABEL);
		TimestampEditor 
			de = new TimestampEditor();
		JButton
			applyButton = new JButton(RUN_BUTTON_TEXT),
			cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		
		de.setComponentValue(new Timestamp(d.getTime()));
		controlPanel.add(timestampLabel);
		controlPanel.add(de);
		
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Timestamp ts = (Timestamp) de.getComponentValueObj();
				Date d = new Date(ts.getTime());
				lcy.update(ab.getText(), ab.getName(), d);
				dispose();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		runCancelPanel.setLayout(new FlowLayout());
		runCancelPanel.add(applyButton);
		runCancelPanel.add(cancelButton);
		
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(runCancelPanel, BorderLayout.SOUTH);
	}

}
