package WidgetComponentDialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.MouseDragScrollListener;
import WidgetUtility.WidgetBuildController;

public class MouseDragScrollUnitAdjust extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE = "Mouse Drag Speed",
		RESET_TEXT = "Reset",
		LABEL_TEXT = "Scroll increment adjustment: ";
	
	private static int
		MOUSE_DRAG_DEFAULT = -1,
		MOUSE_UNIT_MAX = 200;
	
	private static Dimension
		MIN_SIZE = new Dimension(250, 125);
	
	private JSlider 
		slider;
	private JButton 
		reset;
	
	public MouseDragScrollUnitAdjust(Container parent)
	{
		buildWidgets(parent);
	}
	
	private void buildWidgets(Container parent)
	{
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		
		Image img = WidgetBuildController.getInstance().getFrame().getIconImage();
		this.setIconImage(img);
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel sliderLabel = new JLabel(LABEL_TEXT);
		JLabel display = new JLabel();
		if(MOUSE_DRAG_DEFAULT == -1)
		{
			MOUSE_DRAG_DEFAULT = MouseDragScrollListener.getUnitIncrementAdjustment();
		}
		slider = new JSlider(1, MOUSE_UNIT_MAX, MouseDragScrollListener.getUnitIncrementAdjustment());
		slider.addChangeListener(new ChangeListener() 
		{
			@Override
			public void stateChanged(ChangeEvent e) 
			{
				MouseDragScrollListener.setUnitIncrementAdjustment(slider.getValue());
				display.setText(slider.getValue()+"");
			}
		});
		display.setText(slider.getValue()+"");
		reset = new JButton(RESET_TEXT);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				slider.setValue(MOUSE_DRAG_DEFAULT);
			}
		});
		
		sliderPanel.add(sliderLabel);
		sliderPanel.add(slider);
		sliderPanel.add(display);
		sliderPanel.add(reset);
		this.add(sliderPanel);
		
		if(parent == null)
		{
			GraphicsUtil.centerOnScreen(this);
		}
		else
		{
			GraphicsUtil.rightEdgeCenterWindow(parent, this);
		}
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main(String [] args)
	{
		MouseDragScrollListener.setUnitIncrementAdjustment(50);
		MouseDragScrollUnitAdjust mdsu = new MouseDragScrollUnitAdjust(null);
	}

}
